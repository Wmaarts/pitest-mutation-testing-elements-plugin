package org.pitest.elements;

import java.util.Properties;
import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;

public class MutationTestingElementsJsonReportFactory implements MutationResultListenerFactory {

  @Override
  public MutationResultListener getListener(Properties props, ListenerArguments args) {
    return new MutationReportListener(
        MutationReportListener.ReportType.JSON,
        args.getCoverage(),
        args.getOutputStrategy(),
        args.getLocator());
  }

  @Override
  public String name() {
    return "JSON";
  }

  @Override
  public String description() {
    return "Mutation testing elements JSON file report plugin";
  }
}
