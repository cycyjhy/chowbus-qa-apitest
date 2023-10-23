package com.chowbus.qa.listener;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

/**
 * 可以忽略
 */
public class InvokeListener implements IInvokedMethodListener {

  private int failure = 0;

  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    // 如果上一个测试失败,failure会+1
    // 如果failure+1,则使后面所有的测试都跳过
    if (failure != 0) {
      throw new SkipException("Crossed the failure rate");
    }
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    // 如果自动化测试结果为失败,则给failure+1
    if (testResult.getStatus() == ITestResult.FAILURE) {
      failure++;
    }
  }
}