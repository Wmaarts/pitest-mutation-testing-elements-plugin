package org.pitest.stryker.models.json;

import org.pitest.mutationtest.*;

public enum StrykerMutantStatus {
  Killed, Survived, NoCoverage, CompileError, RuntimeError, Timeout;

  public static StrykerMutantStatus fromPitestStatus(
      DetectionStatus status) {
    switch (status) {
    case KILLED:
      return StrykerMutantStatus.Killed;
    case MEMORY_ERROR:
      return StrykerMutantStatus.RuntimeError;
    case NO_COVERAGE:
      return StrykerMutantStatus.NoCoverage;
    case RUN_ERROR:
      return StrykerMutantStatus.RuntimeError;
    case SURVIVED:
      return StrykerMutantStatus.Survived;
    case TIMED_OUT:
      return StrykerMutantStatus.Timeout;
    // If there's an internal state at the end, something probably went wrong
    case NON_VIABLE:
    case STARTED:
    case NOT_STARTED:
    default:
      return StrykerMutantStatus.RuntimeError;
    }
  }
}


