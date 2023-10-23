package com.chowbus.qa;

import com.chowbus.qa.datamodel.workflow.Workflow;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;
import com.chowbus.qa.dataprovider.ApiTestDataprovider;
import com.chowbus.qa.factory.EnumStepFactory;
import com.chowbus.qa.listener.ExecutionListener;
import com.chowbus.qa.listener.TestResultListener;
import com.chowbus.qa.step.BaseStep;
import com.chowbus.qa.utility.TestCaseUtilities;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

@Slf4j
@Listeners({ExecutionListener.class, TestResultListener.class})
public class ApiTest extends BaseTest{
    @Test(dataProviderClass = ApiTestDataprovider.class, dataProvider = "workflow")
    public void apiTest(Workflow workflow) {
        log.info("=============== test case start ===============");

        List<WorkflowStep> workflowSteps = workflow.getSteps();
        // 循环执行单个workflow文件的脚本
        for (WorkflowStep workflowStep : workflowSteps) {
            Map<String, String> map = TestCaseUtilities.fromYamlString(workflowStep.getTestContent(),
                    Map.class);
            // 根据yaml中测试用例类型,实例化用例
            String caseType = map.get("caseType");
            EnumStepFactory stepFactory = EnumStepFactory.createFactory(caseType);
            // baseStep 最终可能是mtop、ui、rpc、rest
            BaseStep baseStep = stepFactory.create();
            // 设置用例
            baseStep.setWorkflowStep(workflowStep);
            baseStep.setTestContext(testContext);
            baseStep.exec();
        }
        log.info("=============== test case finish ===============");
        log.info("本次测试使用的全局变量为: {}", super.globalData);
    }


}
