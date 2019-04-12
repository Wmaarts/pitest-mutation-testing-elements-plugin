package org.pitest.elements.models.json;

import org.pitest.elements.models.Line;

public class JsonLocation {
  private LineAndColumn start, end;

  public JsonLocation(final LineAndColumn start, final LineAndColumn end) {
    this.start = start;
    this.end = end;
  }

  public static JsonLocation ofLine(Line line) {
    final LineAndColumn start = new LineAndColumn((int) line.getNumber(), 1);
    final LineAndColumn end = new LineAndColumn((int) line.getNumber(),
        line.getText().length());
    return new JsonLocation(start, end);
  }

  public static JsonLocation empty(){
    return new JsonLocation(new LineAndColumn(1, 1), new LineAndColumn(1, 2));
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
