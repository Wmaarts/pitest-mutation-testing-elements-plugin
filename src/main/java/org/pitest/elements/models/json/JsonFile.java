package org.pitest.elements.models.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonFile {
  private String source = "";
  private String language = "java";
  private List<JsonMutant> mutants = new ArrayList<>();

  public void addMutants(final Collection<JsonMutant> mutants) {
    mutants.forEach(this::addMutant);
  }

  public void addMutant(final JsonMutant mutant) {
    this.mutants.add(mutant);
  }

  public void addSource(String source) {
    if (this.source.isEmpty()) {
      this.source = source;
    }
  }
}
