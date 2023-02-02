package org.pitest.elements.models.json;

public class JsonMutant {
  private String id;
  private String mutatorName;
  private String description;
  private JsonLocation location;
  private JsonMutantStatus status;

  public JsonMutant(
      String id,
      String mutatorName,
      String description,
      JsonLocation location,
      JsonMutantStatus status) {
    this.id = id;
    this.mutatorName = mutatorName;
    this.description = description;
    this.location = location;
    this.status = status;
  }
}
