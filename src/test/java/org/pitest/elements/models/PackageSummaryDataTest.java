package org.pitest.elements.models;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PackageSummaryDataTest {

  @Test
  public void shouldReturnSummaryDataInAlphabeticOrder() {
    final PackageSummaryData testee = new PackageSummaryData("foo");
    final MutationTestSummaryData z = makeSummaryData("z");
    final MutationTestSummaryData a = makeSummaryData("a");
    testee.addSummaryData(z);
    testee.addSummaryData(a);
    assertEquals(Arrays.asList(a, z), testee.getSummaryData());
  }

  @Test
  public void shouldSortByPackageName() {
    final PackageSummaryData aa = new PackageSummaryData("aa");
    final PackageSummaryData ab = new PackageSummaryData("ab");
    final PackageSummaryData c = new PackageSummaryData("c");
    final List<PackageSummaryData> actual = Arrays.asList(c, aa, ab);
    Collections.sort(actual);
    assertEquals(Arrays.asList(aa, ab, c), actual);
  }

  @Test
  public void shouldNotAddDuplicateSummaryData(){
    final PackageSummaryData testee = new PackageSummaryData("foo");
    final MutationTestSummaryData a = makeSummaryData("a");
    final MutationTestSummaryData a2 = makeSummaryData("a");
    testee.addSummaryData(a);
    testee.addSummaryData(a2);
    assertEquals(Arrays.asList(a), testee.getSummaryData());
  }

  private MutationTestSummaryData makeSummaryData(final String fileName) {
    return new MutationTestSummaryData(fileName,
        Collections.emptyList(),
        Collections.emptyList());
  }

}
