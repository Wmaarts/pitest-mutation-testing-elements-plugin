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

import org.junit.Test;
import org.pitest.mutationtest.MutationResult;
import org.pitest.stryker.models.Line;
import org.pitest.stryker.testutils.MutationResultBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class LineFactoryTest {

  @Test
  public void shouldCreateALineForEachLineInString() throws IOException {
    int nrOfLines = 100;
    LineFactory testee = createTestee();

    List<Line> lines = testee.convert(createReaderWithNrOfLines(nrOfLines));

    assertEquals(nrOfLines, lines.size());
  }

  @Test
  public void shouldAssignMutatorsToCorrectLine() throws IOException {
    int nrOfLines = 100;
    List<Integer> mutatorLocations = Arrays.asList(1, 5, 10, 15, 50, 80);
    final LineFactory testee = createTestee(mutatorLocations);

    final List<Line> lines = testee
        .convert(createReaderWithNrOfLines(nrOfLines));

    for (final Line line : lines) {
      if (mutatorLocations.contains((int) line.getNumber())) {
        assertEquals(1, line.getMutations().size());
      }
    }
  }

  private Reader createReaderWithNrOfLines(int nrOfLines) {
    final StringBuilder sourceString = new StringBuilder();
    for (int i = 0; i < nrOfLines; i++) {
      sourceString.append(" \n");
    }
    return new StringReader(sourceString.toString());
  }

  private LineFactory createTestee() {
    return createTestee(Collections.singletonList(1));
  }

  private LineFactory createTestee(List<Integer> lineNumbers) {
    Collection<MutationResult> mutationResults = new ArrayList<>();
    for (int line : lineNumbers) {
      final MutationResultBuilder builder = new MutationResultBuilder();
      builder.lineNumber(line);
      mutationResults.add(builder.build());
    }
    return new LineFactory(mutationResults);
  }

}
