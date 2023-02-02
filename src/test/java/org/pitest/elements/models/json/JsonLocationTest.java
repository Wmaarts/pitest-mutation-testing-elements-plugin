package org.pitest.elements.models.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Collections;
import org.junit.jupiter.api.*;
import org.pitest.elements.models.Line;
import org.pitest.elements.testutils.JsonBuilder;

public class JsonLocationTest {

  private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  @Test
  public void whiteSpaceRemovalTest() {
    final JsonLocation location =
        JsonLocation.ofLine(new Line(1, "   This is a piece of code", Collections.emptyList()));
    final String json = gson.toJson(location, JsonLocation.class);
    assertEquals(JsonBuilder.locationToJson(1, 4, 26), json);
  }

  @Test
  public void fullWhiteSpaceLineTest() {
    final JsonLocation location = JsonLocation.ofLine(new Line(1, "   ", Collections.emptyList()));
    final String json = gson.toJson(location, JsonLocation.class);
    assertEquals(JsonBuilder.locationToJson(1, 1, 3), json);
  }
}
