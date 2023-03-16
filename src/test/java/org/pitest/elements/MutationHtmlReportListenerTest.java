package org.pitest.elements;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.Writer;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pitest.coverage.ClassLines;
import org.pitest.coverage.ReportCoverage;
import org.pitest.elements.testutils.MockClassLines;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.util.ResultOutputStrategy;

public class MutationHtmlReportListenerTest {

  @Mock private ReportCoverage coverage;

  @Mock private ResultOutputStrategy outputStrategy;

  @Mock private SourceLocator sourceLocator;

  @Mock private Writer writer;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    when(this.outputStrategy.createWriterForFile(any(String.class))).thenReturn(this.writer);
    ClassLines classLines = MockClassLines.create(("foo"));
    when(this.coverage.getCodeLinesForClass(any())).thenReturn(classLines);
  }

  private MutationReportListener testeeFactory(final MutationReportListener.ReportType reportType) {
    return new MutationReportListener(
        reportType, this.coverage, this.outputStrategy, this.sourceLocator);
  }

  @Test
  public void shouldCreateAnIndexFile() {
    testeeFactory(MutationReportListener.ReportType.HTML).runEnd();
    verify(this.outputStrategy).createWriterForFile("html2" + File.separatorChar + "index.html");
  }

  @Test
  public void shouldCreateAJsonFile() {
    testeeFactory(MutationReportListener.ReportType.JSON).runEnd();
    verify(this.outputStrategy).createWriterForFile("json" + File.separatorChar + "report.json");
  }
}
