package org.pitest.elements.models.json;

import java.util.List;

public class JsonTestFile {

  private List<JsonTestDefinition> tests;

  public void addTest(JsonTestDefinition test) {
    if (this.tests == null) {
      this.tests = new java.util.ArrayList<>();
    }
    this.tests.add(test);
  }
}
