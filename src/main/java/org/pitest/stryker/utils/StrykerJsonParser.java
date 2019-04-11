package org.pitest.stryker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.pitest.classinfo.ClassInfo;
import org.pitest.functional.FCollection;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.stryker.models.Line;
import org.pitest.stryker.models.MutationTestSummaryData;
import org.pitest.stryker.models.PackageSummaryData;
import org.pitest.stryker.models.PackageSummaryMap;
import org.pitest.stryker.models.json.*;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class StrykerJsonParser {
  private final Collection<SourceLocator> sourceRoots;

  public StrykerJsonParser(final Collection<SourceLocator> sourceRoots) {
    this.sourceRoots = sourceRoots;
  }

  private final Gson              gson              = new GsonBuilder()
      .disableHtmlEscaping().create();
  private final MutationIdCounter mutationIdCounter = new MutationIdCounter();

  public String toJson(final PackageSummaryMap packageSummaryMap)
      throws IOException {
    mutationIdCounter.reset();
    final Map<String, StrykerFile> collectedStrykerFiles = new HashMap<>();

    List<PackageSummaryData> sortedPackageData = packageSummaryMap.valuesList();
    Collections.sort(sortedPackageData);

    for (PackageSummaryData packageData : sortedPackageData) {
      for (MutationTestSummaryData testData : packageData.getSummaryData()) {
        this.addToStrykerFiles(collectedStrykerFiles, testData);
      }
    }
    final StrykerReport report = new StrykerReport(collectedStrykerFiles);
    return gson.toJson(report, StrykerReport.class);
  }

  private void addToStrykerFiles(
      final Map<String, StrykerFile> collectedStrykerFiles,
      final MutationTestSummaryData data) throws IOException {
    // Step 1: Map mutations to lines
    final List<Line> lines = this.getLines(data);

    // Step 2: Create or retrieve Stryker file
    final String fullPath = data.getPackageName() + "/" + data.getFileName();
    if (collectedStrykerFiles.get(fullPath) == null) {
      collectedStrykerFiles.put(fullPath, new StrykerFile());
    }
    final StrykerFile file = collectedStrykerFiles.get(fullPath);

    // Step 3: Add source and mutants to file
    file.addSource(this.getSourceFromLines(lines));
    file.addMutants(this.getMutantsFromLines(lines, data));
  }

  private List<StrykerMutant> getMutantsFromLines(final List<Line> lines,
      final MutationTestSummaryData data) {
    final List<StrykerMutant> strykerMutants = new ArrayList<>();
    if (lines.isEmpty()) {
      // If there are no lines, add the mutants anyway, without source
      for (MutationResult mutationResult : data.getResults()) {
        strykerMutants.add(this.mapToStrykerMutant(mutationResult, StrykerLocation.empty()));
      }
    } else {
      for (final Line line : lines) {
        for (MutationResult mutationResult : line.getMutations()) {
          strykerMutants.add(this.mapToStrykerMutant(mutationResult, StrykerLocation.ofLine(line)));
        }
      }
    }
    return strykerMutants;
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
    final Collection<ClassInfo> classes = summaryData.getClasses();
    final Optional<Reader> reader = findReaderForSource(classes, fileName);
    if (reader.isPresent()) {
      final LineFactory lineFactory = new LineFactory(summaryData.getResults());
      return lineFactory.convert(reader.get());
    }
    return Collections.emptyList();
  }

  private Optional<Reader> findReaderForSource(
      final Collection<ClassInfo> classes, final String fileName) {
    for (final SourceLocator each : this.sourceRoots) {
      final Optional<Reader> maybe = each
          .locate(this.classInfoToNames(classes), fileName);
      if (maybe.isPresent())
        return maybe;
    }
    return Optional.empty();
  }

  private Collection<String> classInfoToNames(
      final Collection<ClassInfo> classes) {
    return FCollection.map(classes, a -> a.getName().asJavaName());
  }

  private StrykerMutant mapToStrykerMutant(final MutationResult mutation,
      final StrykerLocation location) {
    final String fullMutatorName = mutation.getDetails().getMutator();
    // Only show the class name
    final String mutatorName = fullMutatorName
        .substring(fullMutatorName.lastIndexOf(".") + 1);

    final StrykerMutantStatus status = StrykerMutantStatus
        .fromPitestStatus(mutation.getStatus());
    return new StrykerMutant(mutationIdCounter.next(), mutatorName,
        mutation.getDetails().getDescription(), location, status);
  }
}
