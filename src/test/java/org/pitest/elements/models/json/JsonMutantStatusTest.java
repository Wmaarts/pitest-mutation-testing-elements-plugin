package org.pitest.elements.models.json;

import org.junit.jupiter.api.Test;
import org.pitest.mutationtest.DetectionStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonMutantStatusTest {

  @Test
  public void returnsKilledWhenPitestStatusIsKilled() {
    final DetectionStatus status = DetectionStatus.KILLED;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.Killed, mutantStatus);
  }

  //Test the other enum values
  @Test
  public void returnsSurvivedWhenPitestStatusIsSurvived() {
    final DetectionStatus status = DetectionStatus.SURVIVED;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.Survived, mutantStatus);
  }

  @Test
  public void returnsNoCoverageWhenPitestStatusIsNoCoverage() {
    final DetectionStatus status = DetectionStatus.NO_COVERAGE;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.NoCoverage, mutantStatus);
  }

  @Test
  public void returnsRuntimeErrorWhenPitestStatusIsMemoryError() {
    final DetectionStatus status = DetectionStatus.MEMORY_ERROR;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.RuntimeError, mutantStatus);
  }

  @Test
  public void returnsRuntimeErrorWhenPitestStatusIsRunError() {
    final DetectionStatus status = DetectionStatus.RUN_ERROR;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.RuntimeError, mutantStatus);
  }

  @Test
  public void returnsTimeoutWhenPitestStatusIsTimedOut() {
    final DetectionStatus status = DetectionStatus.TIMED_OUT;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.Timeout, mutantStatus);
  }

  @Test
  public void returnsPendingWhenPitestStatusIsNotStarted() {
    final DetectionStatus status = DetectionStatus.NOT_STARTED;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.Pending, mutantStatus);
  }

  @Test
  public void returnsIgnoredWhenPitestStatusIsNonViable() {
    final DetectionStatus status = DetectionStatus.NON_VIABLE;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.Ignored, mutantStatus);
  }

  @Test
  public void returnsRuntimeErrorWhenPitestStatusIsStarted() {
    final DetectionStatus status = DetectionStatus.STARTED;
    final JsonMutantStatus mutantStatus = JsonMutantStatus.fromPitestStatus(status);
    assertEquals(JsonMutantStatus.RuntimeError, mutantStatus);
  }
}