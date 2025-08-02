package org.pitest.elements.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import org.pitest.coverage.ClassLines;
import org.pitest.elements.models.Line;
import org.pitest.elements.models.MutationTestSummaryData;
import org.pitest.elements.models.PackageSummaryData;
import org.pitest.elements.models.PackageSummaryMap;
import org.pitest.elements.models.json.*;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.SourceLocator;

public class JsonParser {
  private final Collection<SourceLocator> sourceRoots;

  public JsonParser(final Collection<SourceLocator> sourceRoots) {
    this.sourceRoots = sourceRoots;
  }

  private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  private final IdCounter mutationIdCounter = new IdCounter();
  private final IdCounter testIdCounter = new IdCounter();
  private final Map<String, String> testNamesWithId = new HashMap<>();

  public String toJson(final PackageSummaryMap packageSummaryMap) throws IOException {
    mutationIdCounter.reset();
    testIdCounter.reset();
    final Map<String, JsonFile> collectedJsonFiles = new HashMap<>();
    final Map<String, JsonTestFile> collectedJsonTestFiles = new HashMap<>();

    List<PackageSummaryData> sortedPackageData = packageSummaryMap.valuesList();
    Collections.sort(sortedPackageData);

    for (PackageSummaryData packageData : sortedPackageData) {
      for (MutationTestSummaryData testData : packageData.getSummaryData()) {
        if (areCoveringAndKillingTestsSupported()) {
          this.addToJsonTestFiles(collectedJsonTestFiles, testData);
        }
        this.addToJsonFiles(collectedJsonFiles, testData);
      }
    }
    final JsonReport report = new JsonReport(collectedJsonFiles, collectedJsonTestFiles);
    return gson.toJson(report, JsonReport.class);
  }

  private void addToJsonFiles(
      final Map<String, JsonFile> collectedJsonFiles, final MutationTestSummaryData data)
      throws IOException {
    // Step 1: Map mutations to lines
    final List<Line> lines = this.getLines(data);

    // Step 2: Create or retrieve JsonFile
    final String fullPath = data.getPackageName() + "/" + data.getFileName();
    if (collectedJsonFiles.get(fullPath) == null) {
      collectedJsonFiles.put(fullPath, new JsonFile());
    }
    final JsonFile file = collectedJsonFiles.get(fullPath);

    // Step 3: Add source and mutants to file
    file.addSource(this.getSourceFromLines(lines));
    file.addMutants(this.getMutantsFromLines(lines, data));
  }

  private void addToJsonTestFiles(
      final Map<String, JsonTestFile> collectedJsonTestFiles, final MutationTestSummaryData data) {

    data.getResults()
        .forEach(
            mutationResult -> {
              mutationResult
                  .getCoveringTests()
                  .forEach(
                      test -> {
                        if (!testNamesWithId.containsKey(test)) {
                          // Mark the test class name as file name
                          // class name is all the text before ".["
                          String testFileName = "";
                          if (test.contains(".[")) {
                            testFileName = test.substring(0, test.indexOf(".["));
                          }

                          JsonTestFile testFile;
                          if (collectedJsonTestFiles.get(testFileName) == null) {
                            testFile = new JsonTestFile();
                            collectedJsonTestFiles.put(testFileName, testFile);
                          } else {
                            // If the test file already exists, use it
                            testFile = collectedJsonTestFiles.get(testFileName);
                          }

                          String testId = Integer.toString(testIdCounter.next());
                          testNamesWithId.put(test, testId);

                          JsonTestDefinition jsonTestDefinition = new JsonTestDefinition();
                          jsonTestDefinition.setId(testId);
                          jsonTestDefinition.setName(test);
                          testFile.addTest(jsonTestDefinition);
                        }
                      });
            });
  }

  private List<JsonMutant> getMutantsFromLines(
      final List<Line> lines, final MutationTestSummaryData data) {
    final List<JsonMutant> jsonMutants = new ArrayList<>();
    if (lines.isEmpty()) {
      // If there are no lines, add the mutants anyway, without source
      for (MutationResult mutationResult : data.getResults()) {
        jsonMutants.add(this.mapToJsonMutant(mutationResult, JsonLocation.empty()));
      }
    } else {
      for (final Line line : lines) {
        for (MutationResult mutationResult : line.getMutations()) {
          jsonMutants.add(this.mapToJsonMutant(mutationResult, JsonLocation.ofLine(line)));
        }
      }
    }
    return jsonMutants;
  }

  private String getSourceFromLines(final List<Line> lines) {
    if (lines.isEmpty()) {
      return "   ";
    }
    StringBuilder builder = new StringBuilder();
    for (final Line line : lines) {
      builder.append(line.getText());
      builder.append("\n");
    }
    return builder.toString();
  }

  private List<Line> getLines(final MutationTestSummaryData summaryData) throws IOException {
    final String fileName = summaryData.getFileName();
    final Collection<ClassLines> classLines = summaryData.getClassLines();
    final Optional<Reader> reader = findReaderForSource(classLines, fileName);
    if (reader.isPresent()) {
      final LineFactory lineFactory = new LineFactory(summaryData.getResults());
      return lineFactory.convert(reader.get());
    }
    return Collections.emptyList();
  }

  private Optional<Reader> findReaderForSource(
      final Collection<ClassLines> classLines, final String fileName) {
    Collection<String> names = this.classLinesToNames(classLines);
    for (final SourceLocator each : this.sourceRoots) {
      final Optional<Reader> maybe = each.locate(names, fileName);
      if (maybe.isPresent()) return maybe;
    }
    return Optional.empty();
  }

  private Collection<String> classLinesToNames(final Collection<ClassLines> classLines) {
    return classLines.stream().map(a -> a.name().asJavaName()).collect(Collectors.toList());
  }

  private JsonMutant mapToJsonMutant(final MutationResult mutation, final JsonLocation location) {
    final String fullMutatorName = mutation.getDetails().getMutator();
    // Only show the class name
    final String mutatorName = fullMutatorName.substring(fullMutatorName.lastIndexOf(".") + 1);
    String[] coveredBy = null;
    String[] killedBy = null;
    if (areCoveringAndKillingTestsSupported()) {
      coveredBy =
          mutation.getKillingTests().stream()
              .map(test -> testNamesWithId.getOrDefault(test, test))
              .toArray(String[]::new);

      killedBy =
          mutation.getKillingTests().stream()
              .map(test -> testNamesWithId.getOrDefault(test, test))
              .toArray(String[]::new);
    }
    final JsonMutantStatus status = JsonMutantStatus.fromPitestStatus(mutation.getStatus());
    return new JsonMutant(
        Integer.toString(mutationIdCounter.next()),
        mutatorName,
        mutation.getDetails().getDescription(),
        location,
        status,
        coveredBy,
        killedBy);
  }

  private boolean areCoveringAndKillingTestsSupported() {
    // check if getCoveringTests method exists
    try {
      Class<?> mutationResultClass = Class.forName("org.pitest.mutationtest.MutationResult");
      mutationResultClass.getMethod("getCoveringTests");
      mutationResultClass.getMethod("getKillingTests");
      return true;
    } catch (NoSuchMethodException e) {
      // If the method does not exist, return false
      return false;
    } catch (ClassNotFoundException e) {
      // If the class is not found, return false
      return false;
    }
  }
}
