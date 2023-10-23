package com.chowbus.qa.handler;


import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.datamodel.http.HttpRequest;
import com.chowbus.qa.datamodel.http.HttpResponse;
import com.chowbus.qa.datamodel.http.HttpTestCase;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;
import com.chowbus.qa.utility.TestCaseUtilities;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class HttpRequestHandler extends Handler {

  @Override
  public void doHandle(WorkflowStep workflowStep, TestContext testContext) {

    HttpTestCase httpTestCase = buildHttpTestcase(workflowStep, testContext.getGlobalData());
    HttpResponse httpResponse = executeHttpRequest(httpTestCase.getRequest());
    workflowStep.setStepResult(httpResponse.getBody());
    System.out.println(" httpResponse.getBody:" + httpResponse.getBody());
//    next.doHandle(workflowStep, testContext);
  }


  private HttpTestCase buildHttpTestcase(WorkflowStep workflowStep,
      Map<String, String> globalData) {
    String testContent = workflowStep.getTestContent();
    // 全局变量非空替换请求的内容
    if (globalData != null && globalData.size() > 0) {
      // 遍历全局变量,替换请求中的变量信息
      for (Map.Entry<String, String> entry : globalData.entrySet()) {
        testContent = testContent
            .replaceAll(String.format("\\{\\{%s\\}\\}", entry.getKey()), entry.getValue());
      }
    }

    HttpTestCase httpTestCase = TestCaseUtilities.fromYamlString(testContent, HttpTestCase.class);
    if (!TestCaseUtilities.isJson(httpTestCase.getRequest().getApi().getBody())) {
      Assert.fail("接口:" + httpTestCase.getRequest().getApi().getUrl() + " .请求数据不是标准json格式: "
          + httpTestCase.getRequest().getApi().getBody());
    }

    return httpTestCase;
  }

  /**
   * 执行http请求
   *
   * @return
   */
  HttpResponse executeHttpRequest(HttpRequest httpRequest) {
    //创建一个可关闭的HttpClient对象

    CloseableHttpClient httpclient = HttpClients.createDefault();
    String url = httpRequest.getApi().getUrl();
    Map<String, String> header = httpRequest.getApi().getHeader();
    System.out.println("header：" + header);
    String methodType = httpRequest.getApi().getMethod();
    String body = httpRequest.getApi().getBody();
    HttpPost httppost = new HttpPost(url);
    for (Map.Entry<String, String> entry : header.entrySet()) {
      httppost.setHeader(entry.getKey(), entry.getValue());
      System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
    }
    StringEntity entity = null;
    entity = new StringEntity(body, "utf-8");

    httppost.setEntity(entity);
    //发送post请求
    CloseableHttpResponse response = null;

    try {
      response = httpclient.execute(httppost);
    } catch (IOException e) {
      e.printStackTrace();
    }

    HttpEntity entity1 = response.getEntity();
    String reqBody = null;
    try {
      reqBody = EntityUtils.toString(entity1);
    } catch (IOException e) {
      e.printStackTrace();
    }
//      JSONObject jsonObject = JSON.parseObject(reqBody);
//      String state = (String) jsonObject.getString("state");
//      String data = (String) jsonObject.getString("data");

//      httpResponse.setData(data);
//      System.out.println(state);
//      System.out.println(data);
//      System.out.println(data);

    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setBody(reqBody);

    return httpResponse;


  }


}





