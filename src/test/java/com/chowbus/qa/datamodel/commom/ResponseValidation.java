package com.chowbus.qa.datamodel.commom;

import lombok.Data;

import java.util.List;

/**
 * mtop.xx.yaml的validation param部分
 */
@Data
public class ResponseValidation {

  private List<ResponseValidationExpression> expressions;
}
