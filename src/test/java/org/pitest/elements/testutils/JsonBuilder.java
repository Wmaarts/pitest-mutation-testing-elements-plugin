package org.pitest.elements.testutils;

import java.util.Collections;
import java.util.List;
import org.pitest.mutationtest.MutationResult;

public class JsonBuilder {
  private final String beginJson =
      "{\"schemaVersion\":\"2\",\"thresholds\":{\"high\":60,\"low\":80},\"files\":{";
  private final String endJson = "}}";
  private final StringBuilder stringBuilder;
  private int mutantCounter = 0;

  public JsonBuilder() {
    this.stringBuilder = new StringBuilder();
    stringBuilder.append(beginJson);
  }

  public JsonBuilder addFile(final String fileName) {
    this.addFile(fileName, "This is some code", Collections.emptyList());
    return this;
  }

  public JsonBuilder addFile(final String fileName, final String source) {
    this.addFile(fileName, source, Collections.emptyList());
    return this;
  }

  public JsonBuilder addFile(
      final String fileName, final String source, final List<MutationResult> mutations) {
    String fullClassName = "\"package/" + fileName + "/" + fileName + ".java\"";
    stringBuilder.append(fullClassName);
    stringBuilder.append(":{\"source\":\"");
    stringBuilder.append(source);
    stringBuilder.append("\\n\",\"language\":\"java\"");
    stringBuilder.append(",\"mutants\":[");

    if (!mutations.isEmpty()) {
      for (int i = 0; i < mutations.size() - 1; i++) {
        addMutant(mutations.get(i));
        stringBuilder.append(",");
      }
      addMutant(mutations.get(mutations.size() - 1));
    }
    stringBuilder.append("]}");
    return this;
  }

  private void addMutant(final MutationResult result) {
    final int lineNr = result.getDetails().getLineNumber();
    stringBuilder.append("{\"id\":\"");
    stringBuilder.append(mutantCounter++);
    stringBuilder.append("\",\"mutatorName\":\"");
    stringBuilder.append(result.getDetails().getMutator());
    stringBuilder.append("\",\"description\":\"\",\"location\":");
    stringBuilder.append(locationToJson(lineNr));
    stringBuilder.append(",\"status\":\"NoCoverage\"}");
  }

  private String locationToJson(final int lineNr) {
    return locationToJson(lineNr, 1, 7);
  }

  public static String locationToJson(
      final int lineNr, final int startColumn, final int endColumn) {
    final StringBuilder locationBuilder = new StringBuilder();
    locationBuilder.append("{\"start\":{\"line\":");
    locationBuilder.append(lineNr);
    locationBuilder.append(",\"column\":");
    locationBuilder.append(startColumn);
    locationBuilder.append("},\"end\":{\"line\":");
    locationBuilder.append(lineNr);
    locationBuilder.append(",\"column\":");
    locationBuilder.append(endColumn);
    locationBuilder.append("}}");
    return locationBuilder.toString();
  }

  public String build() {
    stringBuilder.append(endJson);
    return stringBuilder.toString();
  }
}
