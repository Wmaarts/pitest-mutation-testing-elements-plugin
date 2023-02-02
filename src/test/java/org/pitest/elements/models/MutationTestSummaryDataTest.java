package org.pitest.elements.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.*;
import org.pitest.coverage.ClassLines;
import org.pitest.elements.testutils.MockClassLines;
import org.pitest.mutationtest.MutationResult;

public class MutationTestSummaryDataTest {

  private MutationTestSummaryData testee;

  private static final String FILE_NAME = "foo.java";

  @Test
  public void shouldAddTestSummaryProperly() {
    // Assemble
    final ClassLines classLines = makeClassLines();
    this.testee = buildSummaryData(classLines);
    int nrOfClassLines = 5;
    int nrOfResults = 10;

    // Act
    this.testee.addTestSummary(buildSummaryDataMutators(nrOfClassLines, nrOfResults));

    // Assert
    assertEquals(1, testee.getClassLines().size());
    assertEquals(nrOfResults, testee.getResults().size());
  }

  private ClassLines makeClassLines() {
    return MockClassLines.create(FILE_NAME);
  }

  private MutationResult makeResult() {
    return new MutationResult(null, null);
  }

  private MutationTestSummaryData buildSummaryData(final ClassLines classLines) {
    final Collection<ClassLines> classLiness = Collections.singletonList(classLines);
    final Collection<MutationResult> results = Collections.emptyList();
    final Collection<String> mutators = Collections.emptyList();
    return new MutationTestSummaryData(FILE_NAME, results, classLiness);
  }

  private MutationTestSummaryData buildSummaryDataMutators() {
    final Collection<ClassLines> classLines = Collections.emptyList();
    final Collection<MutationResult> results = Collections.emptyList();
    return new MutationTestSummaryData(FILE_NAME, results, classLines);
  }

  private MutationTestSummaryData buildSummaryDataMutators(int nrOfClassLines, int nrOfResults) {
    final Collection<ClassLines> classLines = Collections.nCopies(nrOfClassLines, makeClassLines());
    final Collection<MutationResult> results = Collections.nCopies(nrOfResults, makeResult());

    return new MutationTestSummaryData(FILE_NAME, results, classLines);
  }
}
