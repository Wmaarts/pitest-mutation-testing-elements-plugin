package org.pitest.stryker.models.json;

import java.util.Map;

public class StrykerReport {
  private int                      schemaVersion = 1;
  private StrykerThresholds        thresholds = new StrykerThresholds(60, 80);
  private Map<String, StrykerFile> files;

  public StrykerReport(Map<String, StrykerFile> files) {
    this.files = files;
  }
}

class StrykerThresholds {
  private int high, low;

  public StrykerThresholds(int high, int low) {
    this.high = high;
    this.low = low;
  }
}