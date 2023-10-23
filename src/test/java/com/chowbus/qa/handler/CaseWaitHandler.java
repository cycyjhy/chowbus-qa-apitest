package com.chowbus.qa.handler;


import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;
import com.chowbus.qa.utility.TestCaseUtilities;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CaseWaitHandler extends Handler {

  /**
   * 等待时间,异步、或者上游的接口需要等待N秒后接口可以使用
   *
   * @param workflowStep
   * @param testContext
   */
  @Override
  public void doHandle(WorkflowStep workflowStep, TestContext testContext) {

    String testContent = workflowStep.getTestContent();
    CaseWaitTime caseWaitTime = TestCaseUtilities.fromYamlStringIgnoreMissingField(
        testContent, CaseWaitTime.class);
    try {
      log.info("在用例执行前等待{}秒", caseWaitTime.getWaitTime());
      TimeUnit.SECONDS.sleep(caseWaitTime.getWaitTime());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    next.doHandle(workflowStep, testContext);
  }

  @Data
  public static class CaseWaitTime {

    private int waitTime;
  }
}
