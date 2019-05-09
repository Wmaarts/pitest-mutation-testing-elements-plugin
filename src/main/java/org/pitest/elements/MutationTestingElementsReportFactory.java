package org.pitest.elements;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;

import java.util.Properties;

public class MutationTestingElementsReportFactory implements MutationResultListenerFactory {

  @Override
  public MutationResultListener getListener(Properties props,
      ListenerArguments args) {
    return new MutationReportListener(args.getCoverage(),
        args.getOutputStrategy(), args.getLocator());
  }

  @Override
  public String name() {
    return "HTML2";
  }

  @Override
  public String description() {
    return "Mutation testing elements html report plugin";
  }
}
