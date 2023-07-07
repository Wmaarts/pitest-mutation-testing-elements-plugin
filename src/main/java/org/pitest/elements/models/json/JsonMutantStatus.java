package org.pitest.elements.models.json;

import org.pitest.mutationtest.*;

public enum JsonMutantStatus {
  Killed,
  Survived,
  NoCoverage,
  CompileError,
  RuntimeError,
  Timeout,
  Pending;

  public static JsonMutantStatus fromPitestStatus(DetectionStatus status) {
    switch (status) {
      case KILLED:
        return JsonMutantStatus.Killed;
      case MEMORY_ERROR:
        return JsonMutantStatus.RuntimeError;
      case NO_COVERAGE:
        return JsonMutantStatus.NoCoverage;
      case RUN_ERROR:
        return JsonMutantStatus.RuntimeError;
      case SURVIVED:
        return JsonMutantStatus.Survived;
      case TIMED_OUT:
        return JsonMutantStatus.Timeout;
      case NOT_STARTED:
        return JsonMutantStatus.Pending;
        // If there's an internal state at the end, something probably went wrong
      case NON_VIABLE:
      case STARTED:
      default:
        return JsonMutantStatus.RuntimeError;
    }
  }
}
