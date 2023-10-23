package com.chowbus.qa.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 可以忽略
 */
@Slf4j
public class TestResultListener implements ITestListener {

  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

  private long testStartTime;
  private long testEndTime;

  @Override
  public void onTestStart(ITestResult result) {
  }

  @Override
  public void onTestSuccess(ITestResult result) {
  }

  @Override
  public void onTestFailure(ITestResult result) {
  }

  @Override
  public void onTestSkipped(ITestResult result) {
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
  }

  @Override
  public void onTestFailedWithTimeout(ITestResult result) {
  }

  @Override
  public void onStart(ITestContext context) {
    testStartTime = System.currentTimeMillis();
  }

  @Override
  public void onFinish(ITestContext context) {
    testEndTime = System.currentTimeMillis();
    log.info("测试开始时间: {}, 用例结束时间: {}, 用时{}秒", dateFormat.format(testStartTime),
        dateFormat.format(testEndTime),
        decimalFormat.format((float) (testEndTime - testStartTime) / 1000));
  }
}
