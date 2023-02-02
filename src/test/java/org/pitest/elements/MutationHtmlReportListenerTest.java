package org.pitest.elements;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pitest.coverage.ClassLines;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.coverage.ReportCoverage;
import org.pitest.elements.testutils.MockClassLines;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.util.ResultOutputStrategy;

import java.io.File;
import java.io.Writer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MutationHtmlReportListenerTest {

  private MutationReportListener testee;

  @Mock
  private ReportCoverage coverage;

  @Mock
  private ResultOutputStrategy outputStrategy;

  @Mock
  private SourceLocator sourceLocator;

  @Mock
  private Writer writer;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    
    when(this.outputStrategy.createWriterForFile(any(String.class)))
    .thenReturn(this.writer);
    ClassLines classLines = MockClassLines.create(("foo"));
    when(this.coverage.getCodeLinesForClass(any())).thenReturn(
        classLines);

    this.testee = new MutationReportListener(this.coverage,
        this.outputStrategy, this.sourceLocator);
  }

  @Test
  public void shouldCreateAnIndexFile() {
    this.testee.runEnd();
    verify(this.outputStrategy).createWriterForFile("html2" + File.separatorChar + "index.html");
  }

  @Test
  public void shouldCreateAJsFile() {
    this.testee.runEnd();
    verify(this.outputStrategy).createWriterForFile("html2"+ File.separatorChar + "report.js");
  }

}
