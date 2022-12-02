package org.pitest.elements.models;

import org.pitest.coverage.ClassLines;
import org.pitest.mutationtest.MutationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MutationTestSummaryData {

  private final String                     fileName;
  private final Collection<MutationResult> mutations  = new ArrayList<>();
  private final Set<ClassLines>            classLines = new HashSet<>();

  public MutationTestSummaryData(final String fileName,
      final Collection<MutationResult> results, final Collection<ClassLines> classLines) {
    this.fileName = fileName;
    this.mutations.addAll(results);
    this.classLines.addAll(classLines);
  }

  public String getPackageName() {
    // Name needs to be in slashes instead of dots for mutation-testing-elements
    final String packageName = this.classLines.iterator().next().name()
        .asJavaName().replace(".", "/");
    final int lastSlash = packageName.lastIndexOf('/');
    return lastSlash > 0 ? packageName.substring(0, lastSlash) : "default";
  }

  public void addTestSummary(final MutationTestSummaryData data) {
    this.mutations.addAll(data.mutations);
    this.classLines.addAll(data.classLines);
  }

  public String getFileName() {
    return this.fileName;
  }

  public Collection<MutationResult> getResults() {
    return this.mutations;
  }

  public Collection<ClassLines> getClassLines() {
    return this.classLines;
  }

}
