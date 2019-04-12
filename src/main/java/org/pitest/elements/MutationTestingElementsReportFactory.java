/*
 * Copyright 2010 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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
