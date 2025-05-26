package org.pitest.elements;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.pitest.coverage.ClassLines;
import org.pitest.coverage.ReportCoverage;
import org.pitest.elements.models.MutationTestSummaryData;
import org.pitest.elements.models.PackageSummaryMap;
import org.pitest.elements.utils.JsonParser;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.util.ResultOutputStrategy;

public class MutationReportListener implements MutationResultListener {

  private final ResultOutputStrategy outputStrategy;

  private final JsonParser jsonParser;

  private final ReportCoverage coverage;
  private final PackageSummaryMap packageSummaryData = new PackageSummaryMap();
  private final ReportType reportType;

  public enum ReportType {
    HTML,
    JSON
  }

  private static final String HTML_PAGE =
      "<!DOCTYPE html>\n"
          + "<html lang=\"en\">\n"
          + "<head>\n"
          + "  <meta charset=\"UTF-8\">\n"
          + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
          + "  <script src=\"mutation-test-elements.js\"></script>\n"
          + "</head>\n"
          + "<body>\n"
          + "  <mutation-test-report-app title-postfix=\"Pit Test Coverage Report\">\n"
          + "    Your browser doesn't support <a href=\"https://caniuse.com/#search=custom%20elements\">custom elements</a>.\n"
          + "    Please use a latest version of an evergreen browser (Firefox, Chrome, Safari, Opera, etc).\n"
          + "  </mutation-test-report-app>\n"
          + "  <script>\n"
          + "    const app = document.getElementsByTagName('mutation-test-report-app').item(0);\n"
          + "    function updateTheme() {\n"
          + "    document.body.style.backgroundColor = app.themeBackgroundColor;\n"
          + "    }\n"
          + "    app.addEventListener('theme-changed', updateTheme);\n"
          + "    updateTheme();\n"
          + "  </script>\n"
          + "  <script src=\"report.js\"></script>\n"
          + "</body>\n"
          + "</html>";

  public MutationReportListener(
      final ReportType reportType,
      final ReportCoverage coverage,
      final ResultOutputStrategy outputStrategy,
      final SourceLocator... locators) {
    this.reportType = reportType;
    this.coverage = coverage;
    this.outputStrategy = outputStrategy;
    this.jsonParser = new JsonParser(new HashSet<>(Arrays.asList(locators)));
  }

  private String loadMutationTestElementsJs() throws IOException, URISyntaxException {
    final String htmlReportResource = "elements/mutation-test-elements.js";
    Path path = Path.of(this.getClass().getClassLoader().getResource(htmlReportResource).toURI());
    return Files.readString(path);
  }

  private void createHtml() {
    final String content = HTML_PAGE;
    final Writer writer =
        this.outputStrategy.createWriterForFile("html2" + File.separatorChar + "index.html");
    try {
      writer.write(content);
      writer.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void createJs(final String json) {
    final String content = "document.querySelector('mutation-test-report-app').report = " + json;
    final Writer writer =
        this.outputStrategy.createWriterForFile("html2" + File.separatorChar + "report.js");
    try {
      writer.write(content);
      writer.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void createJsonReport(final String json) {
    final Writer writer =
        this.outputStrategy.createWriterForFile("json" + File.separatorChar + "report.json");
    try {
      writer.write(json);
      writer.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void createMutationTestingElementsJs() {
    final Writer writer =
        this.outputStrategy.createWriterForFile(
            "html2" + File.separatorChar + "mutation-test-elements.js");
    try {
      final String content = this.loadMutationTestElementsJs();
      writer.write(content);
      writer.close();
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private MutationTestSummaryData createSummaryData(
      final ReportCoverage coverage, final ClassMutationResults data) {
    Set<ClassLines> classLines =
        Collections.singleton(coverage.getCodeLinesForClass(data.getMutatedClass()));
    return new MutationTestSummaryData(data.getFileName(), data.getMutations(), classLines);
  }

  private void updatePackageSummary(final ClassMutationResults mutationMetaData) {
    final String packageName = mutationMetaData.getPackageName();

    this.packageSummaryData.update(packageName, createSummaryData(this.coverage, mutationMetaData));
  }

  @Override
  public void runStart() {
    // Nothing to do
  }

  @Override
  public void handleMutationResult(ClassMutationResults metaData) {
    updatePackageSummary(metaData);
  }

  @Override
  public void runEnd() {
    try {
      String json = jsonParser.toJson(this.packageSummaryData);
      if (reportType == ReportType.HTML) {
        createHtml();
        createMutationTestingElementsJs();
        createJs(json);
      } else if (reportType == ReportType.JSON) {
        createJsonReport(json);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
