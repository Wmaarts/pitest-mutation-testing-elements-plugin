package org.pitest.stryker.models.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StrykerFile {
  private String              source  = "";
  private List<StrykerMutant> mutants = new ArrayList<>();

  public void addMutants(final Collection<StrykerMutant> mutants) {
    mutants.forEach(this::addMutant);
  }

  public void addMutant(final StrykerMutant mutant) {
    this.mutants.add(mutant);
  }

  public void addSource(String source) {
    if (this.source.isEmpty()) {
      this.source = source;
    }
  }
}
