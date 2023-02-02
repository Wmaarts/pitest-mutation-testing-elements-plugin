package org.pitest.elements.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringReader;
import org.junit.jupiter.api.*;
import org.pitest.functional.FCollection;

public class InputStreamLineIterableTest {

  private InputStreamLineIterable testee;

  @BeforeEach
  public void setUp() {
    final StringReader input = new StringReader("1\n2\n3\n");
    this.testee = new InputStreamLineIterable(input);
  }

  @Test
  public void shouldReadAllInput() {
    assertThat(FCollection.map(testee, s -> s)).containsExactly("1", "2", "3");
  }
}
