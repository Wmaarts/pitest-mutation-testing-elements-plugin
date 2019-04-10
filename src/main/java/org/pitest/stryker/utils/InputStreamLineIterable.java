package org.pitest.stryker.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class InputStreamLineIterable implements Iterable<String> {

  private final BufferedReader reader;
  private       String         next;

  public InputStreamLineIterable(final Reader reader) {
    this.reader = new BufferedReader(reader);
    advance();
  }

  private void advance() {
    try {
      this.next = this.reader.readLine();
    } catch (final IOException e) {
      this.next = null;
    }
  }

  public String next() {
    final String t = this.next;
    advance();
    return t;
  }

  @Override
  public Iterator<String> iterator() {
    return new Iterator<String>() {

      @Override
      public boolean hasNext() {
        return InputStreamLineIterable.this.next != null;
      }

      @Override
      public String next() {
        return InputStreamLineIterable.this.next();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };
  }

}