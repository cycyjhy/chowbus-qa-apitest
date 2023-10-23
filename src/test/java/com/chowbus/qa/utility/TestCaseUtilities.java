package com.chowbus.qa.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class TestCaseUtilities {

  public static boolean isRegMatch(String content, String regExpression) {
    Pattern pattern = Pattern.compile(regExpression);
    Matcher matcher = pattern.matcher(content);
    return matcher.matches();
  }

  public static File getFileInResource(String path) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      return null;
    }
    URL url = loader.getResource(path);
    if (url == null) {
      return null;
    }
    return new File(url.getPath());
  }


  public static List<File> getFilesInResource(String folder, String extension) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      return new ArrayList<>();
    }
    URL url = loader.getResource(folder);
    if (url == null) {
      return new ArrayList<>();
    }
    String suffix = extension.toLowerCase();
    File[] files = new File(url.getPath())
        .listFiles((dir, name) -> name.toLowerCase().endsWith(suffix));
    if (files == null) {
      return new ArrayList<>();
    }
    Arrays.sort(files);
    ArrayList<File> fileList = new ArrayList<>();
    for (File file : files) {
      if (!file.isDirectory()) {
        fileList.add(file);
      }
    }
    return fileList;
  }

  public static String readFileContent(File file) {
    try (InputStream is = new FileInputStream(
        file); InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
      long length = file.length();
      char[] bytes = new char[(int) length];
      reader.read(bytes);
      return new String(bytes);
    } catch (Exception e) {
      log.error("read file {} error", file.getName(), e);
    }
    return "";
  }

  public static boolean isJson(String test) {
    try {
      new JSONObject(test);
    } catch (JSONException ex) {
      try {
        new JSONArray(test);
      } catch (JSONException ex1) {
        return false;
      }
    }
    return true;
  }

  public static <T> T fromJsonString(String jsonString, Class<T> clazz) {
    Gson gson = new GsonBuilder().create();
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString.trim()));
    jsonReader.setLenient(true);
    return gson.fromJson(jsonReader, clazz);
  }

  public static <T> T fromYamlString(String yamlString, Class<T> clazz) {

    Representer representer = new Representer();
    representer.getPropertyUtils().setSkipMissingProperties(true);

    Yaml yaml = new Yaml(new Constructor(clazz), representer);
    return yaml.loadAs(yamlString, clazz);
  }

  public static <T> T fromYamlStringIgnoreMissingField(String yamlString, Class<T> clazz) {
    Representer representer = new Representer();
    representer.getPropertyUtils().setSkipMissingProperties(true);
    Yaml yaml = new Yaml(new Constructor(clazz), representer);
    return yaml.loadAs(yamlString, clazz);
  }

  public static String getFromJsonPath(String jsonString, String jsonPath) {
    Configuration conf = Configuration.defaultConfiguration()
        .setOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext = JsonPath.using(conf).parse(jsonString);
    return documentContext.read(jsonPath);
  }

}
