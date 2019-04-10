package org.pitest.stryker.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageSummaryData implements Comparable<PackageSummaryData> {

  private final String                               packageName;
  private final Map<String, MutationTestSummaryData> fileNameToSummaryData = new HashMap<>();

  public PackageSummaryData(final String packageName) {
    this.packageName = packageName;
  }

  public void addSummaryData(final MutationTestSummaryData data) {
    final MutationTestSummaryData existing = this.fileNameToSummaryData
        .get(data.getFileName());
    if (existing == null) {
      this.fileNameToSummaryData.put(data.getFileName(), data);
    } else {
      existing.addTestSummary(data);
    }
  }

  public String getPackageName() {
    return this.packageName;
  }

  public List<MutationTestSummaryData> getSummaryData() {
    return new ArrayList<>(this.fileNameToSummaryData.values());
  }

  @Override
  public int compareTo(final PackageSummaryData arg0) {
    return this.packageName.compareTo(arg0.packageName);
  }
}