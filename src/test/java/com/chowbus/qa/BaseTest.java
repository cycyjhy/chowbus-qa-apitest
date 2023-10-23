package com.chowbus.qa;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.chowbus.qa.datamodel.TestContext;
import com.chowbus.qa.utility.TestCaseUtilities;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.yaml.snakeyaml.Yaml;

@Slf4j
public class BaseTest {
    static {
        // rest客户端不设置project.name会报错
        System.setProperty("project.name", "apitest");
    }

    private final String GLOBALDATA_YAML_FILE = "globaldata.yaml";
    protected Map<String, String> globalData = new HashMap<>(16);
    protected TestContext testContext = new TestContext();
    @BeforeSuite
    public void beforeSuite() {
//        initSid();
        initGlobalData();
        initGlobalDataFromYaml();
        testContext.setGlobalData(globalData);
        getUsername();
    }

    private void initGlobalDataFromYaml() {
        File globalDataYamlFile = TestCaseUtilities.getFileInResource(GLOBALDATA_YAML_FILE);
        String globalDataYamlFileContent = TestCaseUtilities.readFileContent(globalDataYamlFile).trim();
        Map<String, String> globalDataFromYaml = TestCaseUtilities.fromYamlString(
                globalDataYamlFileContent, Map.class);
        log.info("从文件 {} 初始化全局变量为: {}", GLOBALDATA_YAML_FILE, globalDataFromYaml.toString());
        globalData.putAll(globalDataFromYaml);
    }

    /**
     * 可以用来测试前初始化一些全局的变量,例如时间戳,7天前时间戳等等,按需添加
     */
    private void initGlobalData() {
        log.info("初始化全局变量");
        Map<String, String> param = new HashMap<>();
        long ts = System.currentTimeMillis();
        param.put("ts", String.valueOf(ts));
        String substring = String.valueOf(ts).substring(String.valueOf(ts).length() - 4);
        param.put("ts4", substring);
        String phonenum = String.valueOf(ts).substring(0,11);
        param.put("phonenum", phonenum);
        globalData.putAll(param);

    }



    private void getUsername() {
        Map<String, String> param = new HashMap<>();
        param.put("osUserName", String.valueOf(System.getProperty("user.name")));
        globalData.putAll(param);

    }


}
