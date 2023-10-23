package com.chowbus.qa.datamodel.http;


import com.chowbus.qa.datamodel.commom.ResponseParam;
import com.chowbus.qa.datamodel.commom.ResponseValidation;
import lombok.Data;


@Data
public class HttpTestCase {

  private HttpRequest request;
  private ResponseValidation validation;
  private ResponseParam responseParam;
  private String caseType;
  private String role;
  private int waitTime;
  private String desc;

}
