package org.pitest.elements.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.pitest.coverage.ClassLines;
import org.pitest.elements.models.Line;
import org.pitest.elements.models.MutationTestSummaryData;
import org.pitest.elements.models.PackageSummaryData;
import org.pitest.elements.models.PackageSummaryMap;
import org.pitest.elements.models.json.*;
import org.pitest.functional.FCollection;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.SourceLocator;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class JsonParser {
  private final Collection<SourceLocator> sourceRoots;

  public JsonParser(final Collection<SourceLocator> sourceRoots) {
    this.sourceRoots = sourceRoots;
  }

  private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  private final MutationIdCounter mutationIdCounter = new MutationIdCounter();

  public String toJson(final PackageSummaryMap packageSummaryMap)
      throws IOException {
    mutationIdCounter.reset();
    final Map<String, JsonFile> collectedJsonFiles = new HashMap<>();

    List<PackageSummaryData> sortedPackageData = packageSummaryMap.valuesList();
    Collections.sort(sortedPackageData);

    for (PackageSummaryData packageData : sortedPackageData) {
      for (MutationTestSummaryData testData : packageData.getSummaryData()) {
        this.addToJsonFiles(collectedJsonFiles, testData);
      }
    }
    final JsonReport report = new JsonReport(collectedJsonFiles);
    return gson.toJson(report, JsonReport.class);
  }

  private void addToJsonFiles(final Map<String, JsonFile> collectedJsonFiles,
      final MutationTestSummaryData data) throws IOException {
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

  private List<JsonMutant> getMutantsFromLines(final List<Line> lines,
      final MutationTestSummaryData data) {
    final List<JsonMutant> jsonMutants = new ArrayList<>();
    if (lines.isEmpty()) {
      // If there are no lines, add the mutants anyway, without source
      for (MutationResult mutationResult : data.getResults()) {
        jsonMutants
            .add(this.mapToJsonMutant(mutationResult, JsonLocation.empty()));
      }
    } else {
      for (final Line line : lines) {
        for (MutationResult mutationResult : line.getMutations()) {
          jsonMutants.add(
              this.mapToJsonMutant(mutationResult, JsonLocation.ofLine(line)));
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

  private List<Line> getLines(final MutationTestSummaryData summaryData)
      throws IOException {
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
    for (final SourceLocator each : this.sourceRoots) {
      final Optional<Reader> maybe = each
          .locate(this.classLinesToNames(classLines), fileName);
      if (maybe.isPresent())
        return maybe;
    }
    return Optional.empty();
  }

  private Collection<String> classLinesToNames(
      final Collection<ClassLines> classLines) {
    return FCollection.map(classLines, a -> a.name().asJavaName());
  }

  private JsonMutant mapToJsonMutant(final MutationResult mutation,
      final JsonLocation location) {
    final String fullMutatorName = mutation.getDetails().getMutator();
    // Only show the class name
    final String mutatorName = fullMutatorName
        .substring(fullMutatorName.lastIndexOf(".") + 1);

    final JsonMutantStatus status = JsonMutantStatus
        .fromPitestStatus(mutation.getStatus());
    return new JsonMutant(Integer.toString(mutationIdCounter.next()), mutatorName,
        mutation.getDetails().getDescription(), location, status);
  }
}
