package org.pitest.elements.models.json;

public class JsonMutant {
    private int              id;
    private String           mutatorName;
    private String           replacement;
    private JsonLocation     location;
    private JsonMutantStatus status;

    public JsonMutant(int id, String mutatorName, String replacement, JsonLocation location, JsonMutantStatus status) {
        this.id = id;
        this.mutatorName = mutatorName;
        this.replacement = replacement;
        this.location = location;
        this.status = status;
    }
}
