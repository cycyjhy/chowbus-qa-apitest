package com.chowbus.qa.listener;

import lombok.Data;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过注解控制测试用例的优先级和启用/禁用状态,testng贼好用的监听器
 */
@Data
public class AnnotationTransformer implements IAnnotationTransformer {

  private Map<String, Integer> casePriorityMap = new HashMap<>();

  public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor,
      Method method) {

    Integer integer = casePriorityMap.get(method.getName());
    if (integer != null) {
      iTestAnnotation.setEnabled(true);
      iTestAnnotation.setPriority(integer);
    } else {
      iTestAnnotation.setEnabled(false);
    }
  }
}
