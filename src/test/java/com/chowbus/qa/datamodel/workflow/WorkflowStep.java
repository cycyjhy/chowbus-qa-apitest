package com.chowbus.qa.datamodel.workflow;


import lombok.Data;

/**
 * 单个测试用例,对应mtop.taobao.xx.json
 */
@Data
public class WorkflowStep {

  // 总控的name
  private String workflowName;
  // 测试文件名
  private String test;
  // 测试文件内容
  private String testContent;
  // 测试脚本名
  private String script;
  // 测试脚本内容
  private String scriptContent;
//  // .java文件实例
//  private ScriptBase scriptInstance;
  // step的测试结果
  private String stepResult;

}