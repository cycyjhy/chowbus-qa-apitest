package com.chowbus.qa.datamodel.http;

import lombok.Data;

import java.util.Map;

@Data
public class HttpApi {

  private String url;
  private String method;
  private String body;
  private Map<String, String> header;

}
