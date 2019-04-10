package org.pitest.stryker.models;

import org.junit.Test;
import org.mockito.Mockito;
import org.pitest.classinfo.ClassInfo;
import org.pitest.mutationtest.MutationResult;
import org.pitest.stryker.models.MutationTestSummaryData;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MutationTestSummaryDataTest {

  private MutationTestSummaryData testee;

  private static final String FILE_NAME = "foo.java";

  @Test
  public void shouldAddTestSummaryProperly() {
    // Assemble
    final ClassInfo clazz = makeClass();
    this.testee = buildSummaryData(clazz);
    int nrOfClasses = 5;
    int nrOfResults = 10;

    // Act
    this.testee
        .addTestSummary(buildSummaryDataMutators(nrOfClasses, nrOfResults));

    // Assert
    assertEquals(2, testee.getClasses().size());
    assertEquals(nrOfResults, testee.getResults().size());
  }

  private ClassInfo makeClass() {
    return Mockito.mock(ClassInfo.class);
  }

  private MutationResult makeResult() {
    return new MutationResult(null, null);
  }

  private MutationTestSummaryData buildSummaryData(final ClassInfo clazz) {
    final Collection<ClassInfo> classes = Collections.singletonList(clazz);
    final Collection<MutationResult> results = Collections.emptyList();
    final Collection<String> mutators = Collections.emptyList();
    return new MutationTestSummaryData(FILE_NAME, results, classes);
  }

  private MutationTestSummaryData buildSummaryDataMutators() {
    final Collection<ClassInfo> classes = Collections.emptyList();
    final Collection<MutationResult> results = Collections.emptyList();
    return new MutationTestSummaryData(FILE_NAME, results, classes);
  }

  private MutationTestSummaryData buildSummaryDataMutators(int nrOfClasses,
      int nrOfResults) {
    final Collection<ClassInfo> classes = Collections
        .nCopies(nrOfClasses, makeClass());
    final Collection<MutationResult> results = Collections
        .nCopies(nrOfResults, makeResult());

    return new MutationTestSummaryData(FILE_NAME, results, classes);
  }

}
