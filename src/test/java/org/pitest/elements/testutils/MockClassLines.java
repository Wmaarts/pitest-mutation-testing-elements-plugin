package org.pitest.elements.testutils;

import java.util.Collections;
import org.pitest.classinfo.ClassName;
import org.pitest.coverage.ClassLines;

public class MockClassLines {

  public static ClassLines create(final String fileName) {
    return new ClassLines(ClassName.fromString("package." + fileName), Collections.emptySet());
  }
}
