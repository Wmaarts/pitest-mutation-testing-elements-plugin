package org.pitest.elements.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import org.junit.jupiter.api.*;
import org.pitest.elements.models.Line;
import org.pitest.elements.testutils.MutationResultBuilder;
import org.pitest.mutationtest.MutationResult;

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

    final List<Line> lines = testee.convert(createReaderWithNrOfLines(nrOfLines));

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
