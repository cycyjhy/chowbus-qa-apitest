package com.chowbus.qa.handler;


import com.chowbus.qa.constant.Condition;
import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.datamodel.commom.ResponseValidation;
import com.chowbus.qa.datamodel.commom.ResponseValidationExpression;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;
import com.chowbus.qa.utility.TestCaseUtilities;
import com.jayway.jsonpath.JsonPath;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
public class ResponseValidateHandler extends Handler {

  /**
   * 根据jsonpath和预期值判断响应结果是否准确
   *
   * @param workflowStep
   * @param testContext
   */
  @Override
  public void doHandle(WorkflowStep workflowStep, TestContext testContext) {

    String testContent = workflowStep.getTestContent();

    // 从testcontent中取需要断言的表达式list（validation.expression）
    RespValidation respValidation = TestCaseUtilities.fromYamlStringIgnoreMissingField(
        testContent, RespValidation.class);

    ResponseValidation validation = respValidation.getValidation();
    if (validation == null) {
      return;
    }
    List<ResponseValidationExpression> expressions = validation.getExpressions();
    if (expressions == null || expressions.size() == 0) {
      return;
    }
    for (ResponseValidationExpression responseValidationExpression : respValidation.getValidation()
        .getExpressions()) {
      Condition condition = Condition.typeof(responseValidationExpression.getCondition());
      // 判断表达不能为空
      Assert.assertNotNull(condition,
          workflowStep.getTest() + " : resp validation condition is null");
      String value = JsonPath.read(workflowStep.getStepResult(),
          "$." + responseValidationExpression.getPath()).toString();
      log.info("value的值为"+value);
      Assert.assertNotNull(value);
      String val = responseValidationExpression.getValue();
      if(val.startsWith("{{")&&val.endsWith("}}")){
        String val1 = val.replaceAll("\\{|\\}","");
        Map<String, String> globalData= testContext.getGlobalData();
        val = globalData.get(val1);
        Assert.assertNotNull(val, "Assert value is null");
      }
      log.info("val的值"+val);

      switch (condition) {
        // 相等
        case EQUALS:

          Assert.assertEquals(value, val);
          log.info("断言相等");


          break;
        // 正则
        case REGEX:
          Assert.assertTrue(
              TestCaseUtilities.isRegMatch(value, responseValidationExpression.getValue()));
          break;
        // 包含
        case CONTAINS:
          Assert.assertTrue(value.contains(responseValidationExpression.getValue()));
          break;
        //非空
        case NOTNULL:
          Assert.assertNotNull(value,
                  workflowStep.getTest() + " : 关联商品未生效！");
          break;

        default:

      }
    }
    next.doHandle(workflowStep, testContext);
  }

  @Data
  public static class RespValidation {

    private ResponseValidation validation;

  }
}
