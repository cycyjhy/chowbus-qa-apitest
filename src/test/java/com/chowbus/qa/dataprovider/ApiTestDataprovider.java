package com.chowbus.qa.dataprovider;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.chowbus.qa.constant.CommonConstants;
import com.chowbus.qa.datamodel.workflow.Workflow;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;
import com.chowbus.qa.utility.TestCaseUtilities;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;


@Slf4j
public class ApiTestDataprovider {
    private final String PROPERTY_TAG = "tag";

    @DataProvider(name = "workflow")
    public Iterator<Workflow> creatData() {
        List<Workflow> workflows = getWorkflows();
        return workflows.iterator();
    }

    /**
     * 获取符合tag标记的workflow.yaml文件列表
     *
     * @return
     */
    private List<Workflow> getWorkflows() {

        ArrayList<Workflow> workflows = new ArrayList<>();
        String testTag = System.getProperty(PROPERTY_TAG);

////         获取mvn命令传入的tag参数,如果没有,返回空list不执行测试
//        if (StringUtil.isEmpty(testTag)) {
//            log.error("missing variable tag. cmd is: mvn clean test -Dtag=xxx");
//            return workflows;
//        }

        // 遍历test/resources/workflow下的文件,如果没有后缀为workflow.yaml文件,返回空list,不执行测试
        List<File> workflowFiles = TestCaseUtilities.getFilesInResource(
                CommonConstants.WORKFLOW_ROOT_PATH,
                ".workflow.yaml");
        if (workflowFiles.size() == 0) {
            return workflows;
        }

        // 遍历workflow.yaml文件
        for (File workflowFile : workflowFiles) {
            // 获取总控文件内容
            String workflowFileContent = TestCaseUtilities.readFileContent(workflowFile).trim();
            // 读取file内容转换成workflow对象
            Workflow workflow = TestCaseUtilities.fromYamlString(workflowFileContent, Workflow.class);
            workflow.setWorkflowFile(workflowFile.getName());

            // 检查wokflow对象的tag,符合的加入待测试的workflows中
            if (workflow.getTags().contains(testTag)) {
                List<WorkflowStep> steps = workflow.getSteps();
                for (WorkflowStep step : steps) {
                    String stepFileContent = getTestContent(workflow.getName(), step.getTest());
                    step.setTestContent(stepFileContent);
                    step.setWorkflowName(workflow.getName());
                }
                workflows.add(workflow);
            }
        }
        return workflows;
    }

    /**
     * 获取每个测试用例的文本内容,如:
     *
     * @param workflowName 每个总控中定义的name,根据总控的name得到测试用例文件夹
     * @param testFileName 文件名称,如mtop.taobao.ihome.customer.distribute.yaml
     * @return
     */
    private String getTestContent(String workflowName, String testFileName) {

        //  测试用例文件: workflow/xxx/mtop.taobao.xxx.yaml
        String testFilePath =
                CommonConstants.WORKFLOW_ROOT_PATH + "/" + workflowName + "/" + testFileName;
        File testFile = TestCaseUtilities.getFileInResource(testFilePath);
        // 如果总控里定义的文件找不到,说明待测试的总控配置错误,终止测试
        if (testFile == null) {
            log.error(testFilePath + " : can not be found,terminator program.");
            System.exit(1);
        }
        return TestCaseUtilities.readFileContent(testFile).trim();
    }

}
