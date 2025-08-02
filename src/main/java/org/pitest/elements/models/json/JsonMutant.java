package org.pitest.elements.models.json;

public class JsonMutant {
  private String id;
  private String mutatorName;
  private String description;
  private JsonLocation location;
  private JsonMutantStatus status;
  private String[] coveredBy;
  private String[] killedBy;

  public JsonMutant(
      String id,
      String mutatorName,
      String description,
      JsonLocation location,
      JsonMutantStatus status,
      String[] coveredBy,
      String[] killedBy) {
    this.id = id;
    this.mutatorName = mutatorName;
    this.description = description;
    this.location = location;
    this.status = status;
    this.coveredBy = coveredBy;
    this.killedBy = killedBy;
  }
}
