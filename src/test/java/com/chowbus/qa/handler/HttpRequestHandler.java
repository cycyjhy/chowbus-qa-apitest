package com.chowbus.qa.handler;


import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.datamodel.http.HttpRequest;
import com.chowbus.qa.datamodel.http.HttpResponse;
import com.chowbus.qa.datamodel.http.HttpTestCase;
import com.chowbus.qa.datamodel.workflow.WorkflowStep;
import com.chowbus.qa.utility.TestCaseUtilities;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Slf4j
public class HttpRequestHandler extends Handler {

  @Override
  public void doHandle(WorkflowStep workflowStep, TestContext testContext) {

    HttpTestCase httpTestCase = buildHttpTestcase(workflowStep, testContext.getGlobalData());
    HttpResponse httpResponse = executeHttpRequest(httpTestCase.getRequest());
    workflowStep.setStepResult(httpResponse.getBody());
    System.out.println(" httpResponse.getBody:" + httpResponse.getBody());
    next.doHandle(workflowStep, testContext);
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

    if(httpTestCase.getRequest().getApi().getMethod().equals("POST")){
    if (!TestCaseUtilities.isJson(httpTestCase.getRequest().getApi().getBody())) {
      Assert.fail("接口:" + httpTestCase.getRequest().getApi().getUrl() + " .请求数据不是标准json格式: "
          + httpTestCase.getRequest().getApi().getBody());
    }}

    return httpTestCase;
  }

  /**
   * 执行http请求
   *
   * @return
   */
  HttpResponse executeHttpRequest(HttpRequest httpRequest) {
    //创建一个可关闭的HttpClient对象
    String reqBody = null;
    CloseableHttpResponse  response = null;

    CloseableHttpClient httpclient = HttpClients.createDefault();
    String url = httpRequest.getApi().getUrl();
    String methodType = httpRequest.getApi().getMethod();
    if(methodType.equals("POST")){
      HttpPost httppost = new HttpPost(url);
      Map<String, String> header = httpRequest.getApi().getHeader();
      for (Map.Entry<String, String> entry : header.entrySet()) {
        httppost.setHeader(entry.getKey(), entry.getValue());
      }
      String body = httpRequest.getApi().getBody();
      StringEntity entity = new StringEntity(body, "utf-8");
      httppost.setEntity(entity);

      try {
        response = httpclient.execute(httppost);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
         reqBody= EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }
    else if(methodType.equals("PUT")){
      HttpPut httpPut = new HttpPut(url);
      Map<String, String> header = httpRequest.getApi().getHeader();
      for (Map.Entry<String, String> entry : header.entrySet()) {
        httpPut.setHeader(entry.getKey(), entry.getValue());
      }
      String body = httpRequest.getApi().getBody();
      StringEntity entity = new StringEntity(body, "utf-8");
      httpPut.setEntity(entity);

      try {
        response = httpclient.execute(httpPut);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
        reqBody= EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }
    else if (methodType.equals("GET")) {
      HttpGet httpGet = new HttpGet(url);
      Map<String, String> header = httpRequest.getApi().getHeader();
      for (Map.Entry<String, String> entry : header.entrySet()) {
        httpGet.setHeader(entry.getKey(), entry.getValue());
      }

      try {
        response = httpclient.execute(httpGet);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
         reqBody= EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    else if (methodType.equals("DELETE")) {
      HttpDelete httpDelete = new HttpDelete(url);

      Map<String, String> header = httpRequest.getApi().getHeader();
      for (Map.Entry<String, String> entry : header.entrySet()) {
        httpDelete.setHeader(entry.getKey(), entry.getValue());
      }

      try {
        response = httpclient.execute(httpDelete);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
        reqBody= EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setBody(reqBody);

    return httpResponse;


  }


}





