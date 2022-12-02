package org.pitest.elements.utils;

import org.junit.jupiter.api.*;
import org.pitest.coverage.ClassLines;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.elements.models.MutationTestSummaryData;
import org.pitest.elements.models.PackageSummaryMap;
import org.pitest.elements.testutils.MutationResultBuilder;
import org.pitest.elements.testutils.JsonBuilder;
import org.pitest.elements.testutils.MockClassLines;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {

  @Test
  public void shouldParseToSkeletonOfJsonModel() throws IOException {
    final JsonParser testee = createTestee();
    final String json = testee.toJson(createPackageSummaryMap());
    final String expected = new JsonBuilder().build();
    assertEquals(expected, json);
  }

  @Test
  public void shouldParseTwiceCorrectly() throws IOException {
    final JsonParser testee = createTestee();
    testee.toJson(createPackageSummaryMap());
    final String json = testee.toJson(createPackageSummaryMap());
    final String expected = new JsonBuilder().build();
    assertEquals(expected, json);
  }

  @Test
  public void shouldParseAFileToJson() throws IOException {
    final String fileName = "Foo";
    final JsonParser testee = createTestee();
    final String json = testee
        .toJson(createPackageSummaryMap(Collections.singletonList(fileName + ".java")));
    final String expected = new JsonBuilder().addFile(fileName).build();
    assertEquals(expected, json);
  }

  @Test
  public void shouldParseAFileWithMutantsToJson() throws IOException {
    final String fileName = "Foo";
    final List<Integer> mutantLocations = Arrays.asList(1, 10, 15);
    final Map<String, List<MutationResult>> map = new HashMap<>();
    final MutationResultBuilder builder = new MutationResultBuilder()
        .className(fileName);

    final List<MutationResult> mutationResults = new ArrayList<>();
    for (Integer line : mutantLocations) {
      mutationResults.add(builder.lineNumber(line).build());
    }
    map.put(fileName + ".java", mutationResults);

    final MockSourceLocator sourceLocator = new MockSourceLocator(20);
    final JsonParser testee = createTestee(sourceLocator);
    final String json = testee.toJson(createPackageSummaryMap(map));
    final String expected = new JsonBuilder()
        .addFile(fileName, sourceLocator.getSource(), mutationResults).build();
    assertEquals(expected, json);
  }

  private PackageSummaryMap createPackageSummaryMap(
      final Map<String, List<MutationResult>> mutantsByFile) {
    final PackageSummaryMap map = new PackageSummaryMap();
    for (final String fileName : mutantsByFile.keySet()) {
      final ClassLines classLines = MockClassLines.create(fileName);
      final MutationTestSummaryData data = new MutationTestSummaryData(fileName,
          mutantsByFile.get(fileName), Collections.singletonList(classLines));
      map.update("package", data);
    }
    return map;
  }

  private PackageSummaryMap createPackageSummaryMap(List<String> files) {
    final PackageSummaryMap map = new PackageSummaryMap();
    for (String fileName : files) {
      final ClassLines classLines = MockClassLines.create(fileName);
      final MutationTestSummaryData data = new MutationTestSummaryData(fileName,
          Collections.emptyList(), Collections.singletonList(classLines));
      map.update("package", data);
    }
    return map;
  }

  private PackageSummaryMap createPackageSummaryMap() {
    return new PackageSummaryMap();
  }

  private JsonParser createTestee() {
    return createTestee(new MockSourceLocator());
  }

  private JsonParser createTestee(
      final MockSourceLocator sourceLocator) {
    final Collection<SourceLocator> sourceLocators = Collections
        .singletonList(sourceLocator);
    return new JsonParser(sourceLocators);
  }
}

class MockSourceLocator implements SourceLocator {
  private final String source;

  MockSourceLocator() {
    this.source = "This is some code";
  }

  MockSourceLocator(int nrOfLines) {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < nrOfLines; i++) {
      builder.append("Line ");
      builder.append(i % 10); // % 10 so the lines are equal length
      builder.append(" \n");
    }
    this.source = builder.toString();
  }

  String getSource() {
    final String s = this.source.replace("\n", "\\n");
    return s.substring(0, s.length() - 2);
  }

  @Override
  public Optional<Reader> locate(Collection<String> collection, String s) {
    return Optional.of(new StringReader(source));
  }
}
