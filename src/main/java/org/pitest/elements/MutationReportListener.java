package org.pitest.elements;

import org.pitest.coverage.CoverageDatabase;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.SourceLocator;
import org.pitest.elements.models.MutationTestSummaryData;
import org.pitest.elements.models.PackageSummaryMap;
import org.pitest.elements.utils.JsonParser;
import org.pitest.util.FileUtil;
import org.pitest.util.Log;
import org.pitest.util.ResultOutputStrategy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;

public class MutationReportListener implements MutationResultListener {

  private final ResultOutputStrategy outputStrategy;

  private final JsonParser jsonParser;

  private final CoverageDatabase  coverage;
  private final PackageSummaryMap packageSummaryData = new PackageSummaryMap();

  public MutationReportListener(final CoverageDatabase coverage,
      final ResultOutputStrategy outputStrategy, final SourceLocator... locators) {
    this.coverage = coverage;
    this.outputStrategy = outputStrategy;
    this.jsonParser = new JsonParser(
        new HashSet<>(Arrays.asList(locators)));
  }

  private String loadHtml() {
    final String startHtml = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
        + "  <mutation-test-report-app title-postfix=\"Pit Test Coverage Report\">\n"
        + "    Your browser doesn't support <a href=\"https://caniuse.com/#search=custom%20elements\">custom elements</a>.\n"
        + "    Please use a latest version of an evergreen browser (Firefox, Chrome, Safari, Opera, etc).\n"
        + "  </mutation-test-report-app>\n"
        + "  <script src=\"report.js\"></script>\n" + "  <script>";
    final String endHtml = "  </script>\n" + "</body>\n" + "</html>";
    try {
      final String htmlReportResource = "mutation-testing-elements/mutation-test-elements.js";
      final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(htmlReportResource);
      return startHtml + FileUtil.readToString(inputStream) + endHtml;
    } catch (final IOException e) {
      Log.getLogger().log(Level.SEVERE, "Error while loading css", e);
    }
    return "";
  }

  private void createHtml() {
    final String content = this.loadHtml();
    final Writer writer = this.outputStrategy
        .createWriterForFile("html2" + File.separatorChar + "index.html");
    try {
      writer.write(content);
      writer.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void createJs(final String json) {
    final String content =
        "document.querySelector('mutation-test-report-app').report = " + json;
    final Writer writer = this.outputStrategy
        .createWriterForFile("html2" + File.separatorChar + "report.js");
    try {
      writer.write(content);
      writer.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private MutationTestSummaryData createSummaryData(
      final CoverageDatabase coverage, final ClassMutationResults data) {
    return new MutationTestSummaryData(data.getFileName(),
        data.getMutations(), coverage.getClassInfo(Collections.singleton(data.getMutatedClass())));
  }

  private void updatePackageSummary(
      final ClassMutationResults mutationMetaData) {
    final String packageName = mutationMetaData.getPackageName();

    this.packageSummaryData.update(packageName,
        createSummaryData(this.coverage, mutationMetaData));
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
      createHtml();
      createJs(json);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
