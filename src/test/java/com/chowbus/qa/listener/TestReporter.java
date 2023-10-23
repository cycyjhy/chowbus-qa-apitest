package com.chowbus.qa.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用来生成测试报告,可以忽略
 */
public class TestReporter implements IReporter {

  private static final String OUTPUT_FOLDER = "test-output/";
  private static final String FILE_NAME = "report.html";

  private ExtentReports extent;

  @Override
  public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
      String outputDirectory) {
    init();

    for (ISuite suite : suites) {
      Map<String, ISuiteResult> result = suite.getResults();

      for (ISuiteResult r : result.values()) {
        ITestContext context = r.getTestContext();
        buildTestNodes(context.getFailedTests(), Status.FAIL);
        buildTestNodes(context.getSkippedTests(), Status.SKIP);
        buildTestNodes(context.getPassedTests(), Status.PASS);
      }
    }
    for (String s : Reporter.getOutput()) {
      extent.addTestRunnerOutput(s);
    }
    extent.flush();
  }

  private void init() {
    ExtentSparkReporter htmlReporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);

    htmlReporter.config().setDocumentTitle("自动化测试报告");
    htmlReporter.config().setReportName("APITEST自动化测试报告");
    htmlReporter.config().setTheme(Theme.STANDARD);
    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);
    extent.setReportUsesManualConfiguration(true);
  }

  private void buildTestNodes(IResultMap tests, Status status) {
    ExtentTest test;

    if (tests.size() > 0) {
      for (ITestResult result : tests.getAllResults()) {
        test = extent.createTest(result.getMethod().getMethodName());
        for (String group : result.getMethod().getGroups()) {
          test.assignCategory(group);
        }
        if (result.getThrowable() != null) {
          test.log(status, result.getThrowable());
        } else {
          test.log(status, "Test " + status.toString().toLowerCase() + "ed");
        }
        test.getModel().setStartTime(getTime(result.getStartMillis()));
        test.getModel().setEndTime(getTime(result.getEndMillis()));
        test.getModel().setDescription("apitest报告！");
      }
    }
  }

  private Date getTime(long millis) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar.getTime();
  }
}