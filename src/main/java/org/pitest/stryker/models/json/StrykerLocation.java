package org.pitest.stryker.models.json;

import org.pitest.stryker.models.Line;

public class StrykerLocation {
  private LineAndColumn start, end;

  public StrykerLocation(final LineAndColumn start, final LineAndColumn end) {
    this.start = start;
    this.end = end;
  }

  public static StrykerLocation ofLine(Line line) {
    if(line == null){
      return new StrykerLocation(new LineAndColumn(1, 1), new LineAndColumn(1, 2));
    } else {
      final LineAndColumn start = new LineAndColumn((int) line.getNumber(), 1);
      final LineAndColumn end = new LineAndColumn((int) line.getNumber(),
          line.getText().length());
      return new StrykerLocation(start, end);
    }
  }
}

class LineAndColumn {
  int line;
  int column;

  public LineAndColumn(final int line, final int column) {
    this.line = line;
    this.column = column;
  }
}
