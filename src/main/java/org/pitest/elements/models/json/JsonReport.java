package org.pitest.elements.models.json;

import java.util.Map;

public class JsonReport {
  private String                schemaVersion = "1";
  private JsonThresholds        thresholds    = new JsonThresholds(60, 80);
  private Map<String, JsonFile> files;

  public JsonReport(Map<String, JsonFile> files) {
    this.files = files;
  }
}

class JsonThresholds {
  private int high, low;

  public JsonThresholds(int high, int low) {
    this.high = high;
    this.low = low;
  }
}
