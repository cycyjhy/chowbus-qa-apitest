package com.chowbus.qa.handler;


import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.datamodel.commom.ResponseParam;
import com.chowbus.qa.datamodel.commom.ResponseParamExpression;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;
import com.chowbus.qa.utility.TestCaseUtilities;
import com.jayway.jsonpath.JsonPath;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 根据jsonpath,提取变量,主要作用是前置接口给后续接口提供变量参数,提取的变量都保存在全局的HashMap中
 */
@Slf4j
public class ResponseParamHandler extends Handler {

  @Override
  public void doHandle(WorkflowStep workflowStep, TestContext testContext) {

    String testContent = workflowStep.getTestContent();
    RespParam respParam = TestCaseUtilities.fromYamlStringIgnoreMissingField(testContent,
        RespParam.class);
    ResponseParam responseParam = respParam.getResponseParam();
    if (responseParam == null) {
      return;
    }
    List<ResponseParamExpression> responseParamExpressions = responseParam.getExpressions();
    if (responseParamExpressions == null || responseParamExpressions.size() == 0) {
      return;
    }
    for (ResponseParamExpression expression : responseParamExpressions) {
      String jsonPath = "$." + expression.getPath();
      String respBody = workflowStep.getStepResult();
      String value = JsonPath.read(respBody, jsonPath).toString();
      String key = expression.getValue();
      log.info("根据jsonpath[{}]提取变量[{}]的值为: {}", jsonPath, key, value);
      testContext.getGlobalData().put(key, value);
    }
//    next.doHandle(workflowStep, testContext);
  }

  @Data
  public static class RespParam {

    private ResponseParam responseParam;

  }
}
