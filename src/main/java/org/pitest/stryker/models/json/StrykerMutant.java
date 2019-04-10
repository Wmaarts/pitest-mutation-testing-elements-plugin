package org.pitest.stryker.models.json;

public class StrykerMutant {
    private int id;
    private String mutatorName;
    private String replacement;
    private StrykerLocation location;
    private StrykerMutantStatus status;

    public StrykerMutant(int id, String mutatorName, String replacement, StrykerLocation location, StrykerMutantStatus status) {
        this.id = id;
        this.mutatorName = mutatorName;
        this.replacement = replacement;
        this.location = location;
        this.status = status;
    }

    public void setId(int id){
        this.id = id;
    }
    
//    public String toJson(){
//        return "{" +
//                " \"id\": \"" + this.id + "\"," +
//                " \"mutatorName\": \"" + this.mutatorName + "\"," +
//                " \"replacement\": \"" + this.replacement + "\"," +
//                " \"location\": {" + location.toJson() +
//                " }," +
//                " \"status\": \"" + this.status + "\"" +
//                "}";
//    }
}
