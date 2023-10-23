package com.chowbus.qa.constant;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {
  GET("get"),
  POST("post"),
  PUT("put"),
  DELETE("delete");

  private static final Map<String, HttpMethod> TYPE_MAP = new HashMap<>();

  static {
    for (HttpMethod type : values()) {
      TYPE_MAP.put(type.method, type);
    }
  }

  private String method;

  HttpMethod(String method) {
    this.method = method;
  }

  public static HttpMethod typeof(String method) {
    return TYPE_MAP.get(method);
  }

  public String value() {
    return method;
  }
}
