package com.chowbus.qa.constant;

import java.util.HashMap;
import java.util.Map;

public enum Condition {
  /**
   * 大于
   */
  ABOVE("above"),
  /**
   * 小于
   */
  BELOW("below"),
  LENGTH("length"),
  EQUALS("equals"),
  REGEX("regex"),
  NOTNULL("not_null"),
  CONTAINS("contains"),
  SCHEMA("schema");

  private static final Map<String, Condition> TYPE_MAP = new HashMap<>();

  static {
    for (Condition type : values()) {
      TYPE_MAP.put(type.condition, type);
    }
  }

  private String condition;

  Condition(String condition) {
    this.condition = condition;
  }

  public static Condition typeof(String condition) {
    return TYPE_MAP.get(condition);
  }

  public String value() {
    return condition;
  }
}
