package org.pitest.elements.utils;

import org.junit.Test;
import org.pitest.elements.models.MutationTestSummaryData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MutationTestSummaryDataFileNameComparatorTest {

  @Test
  public void shouldSortDataByFileName() {
    final MutationTestSummaryDataFileNameComparator testee = new MutationTestSummaryDataFileNameComparator();
    final MutationTestSummaryData ab = makeSummaryData("ab");
    final MutationTestSummaryData aa = makeSummaryData("aa");
    final MutationTestSummaryData z = makeSummaryData("z");
    final List<MutationTestSummaryData> list = Arrays.asList(z, aa, ab);
    Collections.sort(list, testee);
    final List<MutationTestSummaryData> expected = Arrays.asList(aa, ab, z);
    assertEquals(expected, list);
  }

  private MutationTestSummaryData makeSummaryData(final String fileName) {
    return new MutationTestSummaryData(fileName,
        Collections.emptyList(),
        Collections.emptyList());
  }

}
