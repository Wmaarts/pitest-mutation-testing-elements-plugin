package org.pitest.elements.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.pitest.elements.models.Line;
import org.pitest.mutationtest.MutationResult;

public class LineFactory {

  private final Collection<MutationResult> mutations;

  public LineFactory(final Collection<MutationResult> mutations) {
    this.mutations = mutations;
  }

  public List<Line> convert(final Reader source) throws IOException {
    try {
      final InputStreamLineIterable lines = new InputStreamLineIterable(source);
      ArrayList<Line> result = new ArrayList<>();
      Function<String, Line> f = stringToAnnotatedLine();
      for (String line : lines) {
        result.add(f.apply(line));
      }
      return result;
    } finally {
      source.close();
    }
  }

  private Function<String, Line> stringToAnnotatedLine() {
    return new Function<String, Line>() {
      private int lineNumber = 1;

      @Override
      public Line apply(final String a) {
        final Line l = new Line(this.lineNumber, a, getMutationsForLine(this.lineNumber));
        this.lineNumber++;
        return l;
      }
    };
  }

  private List<MutationResult> getMutationsForLine(final int lineNumber) {
    return this.mutations.stream().filter(isAtLineNumber(lineNumber)).collect(Collectors.toList());
  }

  private Predicate<MutationResult> isAtLineNumber(final int lineNumber) {
    return result -> result.getDetails().getLineNumber() == lineNumber;
  }
}
