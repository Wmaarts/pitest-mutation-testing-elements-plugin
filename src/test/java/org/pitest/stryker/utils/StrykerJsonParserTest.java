/*
 * Copyright 2010 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.stryker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.pitest.classinfo.ClassInfo;
import org.pitest.classinfo.ClassName;
import org.pitest.classinfo.MockClassInfoBuilder;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.stryker.models.MutationTestSummaryData;
import org.pitest.stryker.models.PackageSummaryMap;
import org.pitest.stryker.testutils.MutationResultBuilder;
import org.pitest.stryker.testutils.StrykerJsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class StrykerJsonParserTest {

  private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  @Test
  public void shouldParseToSkeletonOfJsonModel() throws IOException {
    final StrykerJsonParser testee = createTestee();
    final String json = testee.toJson(createPackageSummaryMap());
    final String expected = new StrykerJsonBuilder().build();
    assertEquals(expected, json);
  }

  @Test
  public void shouldParseAFileToJson() throws IOException {
    final String fileName = "Foo";
    final StrykerJsonParser testee = createTestee();
    final String json = testee
        .toJson(createPackageSummaryMap(Arrays.asList(fileName + ".java")));
    final String expected = new StrykerJsonBuilder().addFile(fileName).build();
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
    final StrykerJsonParser testee = createTestee(sourceLocator);
    final String json = testee.toJson(createPackageSummaryMap(map));
    final String expected = new StrykerJsonBuilder()
        .addFile(fileName, sourceLocator.getSource(), mutationResults).build();
    assertEquals(expected, json);
  }

  private PackageSummaryMap createPackageSummaryMap(
      final Map<String, List<MutationResult>> mutantsByFile) {
    final PackageSummaryMap map = new PackageSummaryMap();
    for (final String fileName : mutantsByFile.keySet()) {
      final ClassInfo classInfo = new MockClassInfo(fileName);
      final MutationTestSummaryData data = new MutationTestSummaryData(fileName,
          mutantsByFile.get(fileName), Collections.singletonList(classInfo));
      map.update("package", data);
    }
    return map;
  }

  private PackageSummaryMap createPackageSummaryMap(List<String> files) {
    final PackageSummaryMap map = new PackageSummaryMap();
    for (String fileName : files) {
      final ClassInfo classInfo = new MockClassInfo(fileName);
      final MutationTestSummaryData data = new MutationTestSummaryData(fileName,
          Collections.emptyList(), Collections.singletonList(classInfo));
      map.update("package", data);
    }
    return map;
  }

  private PackageSummaryMap createPackageSummaryMap() {
    return new PackageSummaryMap();
  }

  private StrykerJsonParser createTestee() {
    return createTestee(new MockSourceLocator());
  }

  private StrykerJsonParser createTestee(
      final MockSourceLocator sourceLocator) {
    final Collection<SourceLocator> sourceLocators = Collections
        .singletonList(sourceLocator);
    return new StrykerJsonParser(sourceLocators);
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

class MockClassInfo extends ClassInfo {
  final private String fileName;

  MockClassInfo(final String fileName) {
    super(null, null, new MockClassInfoBuilder());
    this.fileName = fileName;
  }

  @Override
  public ClassName getName() {
    return ClassName.fromString("package." + fileName);
  }
}