package org.pitest.stryker.models.json;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StrykerFile {
  private String              source  = "";
  private List<StrykerMutant> mutants = new ArrayList<>();

  private transient int idCounter = 0;

  public StrykerFile() {
  }

  public void addMutants(final Collection<StrykerMutant> mutants) {
    mutants.forEach(this::addMutant);
  }

  public void addMutant(final StrykerMutant mutant) {
    mutant.setId(idCounter++);
    this.mutants.add(mutant);
  }

  public void addSource(String source) {
    if (this.source.isEmpty()) {
      this.source = source;
    }
  }

  public List<StrykerMutant> getMutants(){
    return this.mutants;
  }

}
