package com.chowbus.qa.handler;


import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;

/**
 * 责任链
 */
public abstract class Handler {

  protected Handler next;

  public void next(Handler next) {
    this.next = next;
  }

  public abstract void doHandle(WorkflowStep workflowStep, TestContext testContext);

  public static class Builder {

    private Handler head;
    private Handler tail;

    public Builder addHandler(Handler handler) {
      if (this.head == null) {
        this.head = this.tail = handler;
        return this;
      }
      this.tail.next(handler);
      this.tail = handler;
      return this;
    }

    public Handler build() {
      return this.head;
    }
  }
}