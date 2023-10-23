package com.chowbus.qa;


import com.chowbus.qa.constant.CommonConstants;
import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.datamodel.workflow.Workflow;
import com.chowbus.qa.utility.TestCaseUtilities;
import lombok.extern.slf4j.Slf4j;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.util.*;

@Slf4j
public class MainTest {

  public static void main(String[] args) {
    // 以下为通过代码形式驱动testng的运行
    // 遍历test/resources/workflow下的文件,如果没有后缀为workflow.yaml文件,返回空list,不执行测试
    List<File> workflowFiles = TestCaseUtilities.getFilesInResource(
        CommonConstants.WORKFLOW_ROOT_PATH, ".workflow.yaml");

    // 记录workflow文件的所有tag
    Set<String> tagSet = new LinkedHashSet<>();

    // 遍历workflow.yaml文件
    for (File workflowFile : workflowFiles) {
      // 获取总控文件内容
      String workflowFileContent = TestCaseUtilities.readFileContent(workflowFile).trim();
      // 读取file内容转换成workflow对象
      Workflow workflow = TestCaseUtilities.fromYamlString(workflowFileContent, Workflow.class);
      workflow.setWorkflowFile(workflowFile.getName());
      tagSet.addAll(workflow.getTags());
    }

    List<String> tagList = new ArrayList<>(tagSet);

    for (int i = 0; i < tagList.size(); i++) {
      log.info("输入" + (i + 1) + ",执行测试用例: " + tagList.get(i));
    }
    log.info("请输入用例编号...");
    Scanner s = new Scanner(System.in);
    int caseId = s.nextInt();
    System.setProperty("tag", tagList.get(caseId - 1));
    doTestng();

  }

  public static void doTestng() {
    TestNG testNG = new TestNG();
    XmlSuite xmlSuite = new XmlSuite();
    XmlTest xmlTest = new XmlTest(xmlSuite);
    XmlClass xmlClass = new XmlClass();
    Class<?> tagTestClass = null;

    try {
      tagTestClass = Class.forName("com.chowbus.qa.ApiTest");
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    }
    xmlClass.setClass(tagTestClass);
    TestContext testContext = new TestContext();
    xmlTest.setParameters(testContext.getGlobalData());
    xmlTest.setXmlClasses(Arrays.asList(xmlClass));
    testNG.setXmlSuites(Arrays.asList(xmlSuite));
    testNG.run();
  }
}


