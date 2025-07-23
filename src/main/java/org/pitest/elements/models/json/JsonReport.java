package org.pitest.elements.models.json;

import java.util.Map;

public class JsonReport {
  private String schemaVersion = "2";
  private JsonThresholds thresholds = new JsonThresholds(60, 80);
  private Map<String, JsonFile> files;
  private Map<String, JsonTestFile> testFiles;

  public JsonReport(Map<String, JsonFile> files, Map<String, JsonTestFile> testFiles) {
    this.files = files;
    this.testFiles = testFiles;
  }
}

class JsonThresholds {
  private int high, low;

  public JsonThresholds(int high, int low) {
    this.high = high;
    this.low = low;
  }
}
