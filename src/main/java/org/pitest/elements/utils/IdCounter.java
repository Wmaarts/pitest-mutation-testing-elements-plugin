package org.pitest.elements.utils;

public class IdCounter {
  private int counter = 0;

  public int next() {
    return counter++;
  }

  public void reset() {
    this.counter = 0;
  }
}
