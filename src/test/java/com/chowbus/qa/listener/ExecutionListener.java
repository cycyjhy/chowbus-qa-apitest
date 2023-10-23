package com.chowbus.qa.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.IExecutionListener;

@Slf4j
public class ExecutionListener implements IExecutionListener {

  @Override
  public void onExecutionStart() {
    log.info("==================== test suite start ====================");
  }

  @Override
  public void onExecutionFinish() {
    log.info("==================== test suite finish ====================");
  }
}