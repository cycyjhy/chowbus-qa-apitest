package com.chowbus.qa.datamodel.commom;

import lombok.Data;

@Data
public class ResponseValidationExpression {

  private String condition;
  private String path;
  private String value;
}
