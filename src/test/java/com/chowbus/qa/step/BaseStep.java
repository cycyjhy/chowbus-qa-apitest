package com.chowbus.qa.step;


import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;
import lombok.Data;

/**
 * 测试阶段,用来指向各种实际的rpc、mtop等待step
 */
@Data
public abstract class BaseStep {

  public TestContext testContext = new TestContext();
  private WorkflowStep workflowStep;

  public abstract void exec();

}
