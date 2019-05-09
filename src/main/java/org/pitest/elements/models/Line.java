package org.pitest.elements.models;

import org.pitest.mutationtest.MutationResult;

import java.util.List;

public class Line {
  private final long                 number;
  private final String               text;
  private final List<MutationResult> mutations;

  public Line(final long number, final String text, final List<MutationResult> mutations) {
    this.number = number;
    this.text = text;
    this.mutations = mutations;
  }

  public long getNumber() {
    return this.number;
  }

  public String getText() {
    return this.text;
  }

  public List<MutationResult> getMutations() {
    return this.mutations;
  }

}
