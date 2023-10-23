package com.chowbus.qa.datamodel.commom;

import lombok.Data;

import java.util.List;

/**
 * mtop.xx.yaml的response param部分
 */
@Data
public class ResponseParam {

  private List<ResponseParamExpression> expressions;
}
