/*
 * Copyright 2010 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.stryker.utils;

import org.pitest.functional.FCollection;
import org.pitest.mutationtest.MutationResult;
import org.pitest.stryker.models.Line;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LineFactory {

  private final Collection<MutationResult> mutations;

  public LineFactory(final Collection<MutationResult> mutations) {
    this.mutations = mutations;
  }

  public List<Line> convert(final Reader source) throws IOException {
    try {
      final InputStreamLineIterable lines = new InputStreamLineIterable(source);
      return FCollection.map(lines, stringToAnnotatedLine());
    } finally {
      source.close();
    }
  }

  private Function<String, Line> stringToAnnotatedLine() {
    return new Function<String, Line>() {
      private int lineNumber = 1;

      @Override
      public Line apply(final String a) {
        final Line l = new Line(this.lineNumber, a,
            getMutationsForLine(this.lineNumber));
        this.lineNumber++;
        return l;
      }
    };
  }

  private List<MutationResult> getMutationsForLine(final int lineNumber) {
    return this.mutations.stream().filter(isAtLineNumber(lineNumber))
        .collect(Collectors.toList());
  }

  private Predicate<MutationResult> isAtLineNumber(final int lineNumber) {
    return result -> result.getDetails().getLineNumber() == lineNumber;
  }

}
