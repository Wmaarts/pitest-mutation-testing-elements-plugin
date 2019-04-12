package org.pitest.elements.testutils;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.engine.Location;
import org.pitest.mutationtest.engine.MethodName;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;

public class MutationResultBuilder {

  private int    lineNumber = 1;
  private String className  = "Foo";
  public MutationResultBuilder lineNumber(int lineNumber){
    this.lineNumber = lineNumber;
    return this;
  }
  public MutationResultBuilder className(String className){
    this.className = className;
    return this;
  }
  public MutationResult build() {
    final Location location = new Location(ClassName.fromString(className), MethodName.fromString(""), "constructor");
    final MutationIdentifier id = new MutationIdentifier(location, lineNumber,
        "id + " + lineNumber);
    final MutationDetails details = new MutationDetails(id, className + ".java", "",
        lineNumber, 0);
    final MutationStatusTestPair pair = new MutationStatusTestPair(0,
        DetectionStatus.NO_COVERAGE, null);
    return new MutationResult(details, pair);
  }
}
