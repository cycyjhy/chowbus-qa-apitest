package com.chowbus.qa.datamodel.workflow;

import lombok.Data;

import java.util.List;

/**
 * 类似总控文件xxx.workflow.json
 */
@Data
public class Workflow {

  private String workflowFile;
  private String name;
  private String desc;
  private List<WorkflowStep> steps;
  private List<String> tags;

}
