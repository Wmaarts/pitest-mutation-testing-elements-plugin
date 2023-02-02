package org.pitest.elements.models;

import java.util.*;

public class PackageSummaryMap {

  private final Map<String, PackageSummaryData> packageSummaryData = new TreeMap<>();

  private PackageSummaryData getPackageSummaryData(final String packageName) {
    if (this.packageSummaryData.containsKey(packageName)) {
      return this.packageSummaryData.get(packageName);
    } else {
      final PackageSummaryData psData = new PackageSummaryData(packageName);
      this.packageSummaryData.put(packageName, psData);
      return psData;
    }
  }

  public void update(final String packageName, final MutationTestSummaryData data) {
    final PackageSummaryData psd = getPackageSummaryData(packageName);
    psd.addSummaryData(data);
  }

  public List<PackageSummaryData> valuesList() {
    return new ArrayList<>(this.packageSummaryData.values());
  }
}
