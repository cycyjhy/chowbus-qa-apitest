package com.chowbus.qa;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
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

        Map<String, String> param = new HashMap<>();
        long ts = System.currentTimeMillis();
        param.put("ts", String.valueOf(ts));
        String substring = String.valueOf(ts).substring(String.valueOf(ts).length() - 4);
        param.put("ts4", substring);
        String phonenum = String.valueOf(ts).substring(0,11);
        param.put("phonenum", phonenum);

        String order_id = UUID.randomUUID().toString().toLowerCase();
        String device_id = UUID.randomUUID().toString().toLowerCase();
        String dish_item_id1=UUID.randomUUID().toString().toLowerCase();
        String dish_item_id2=UUID.randomUUID().toString().toLowerCase();
        String line_items_id=UUID.randomUUID().toString().toLowerCase();
        String payment_id=UUID.randomUUID().toString().toLowerCase();
        String check_id=UUID.randomUUID().toString().toLowerCase();
        String customized_id1=UUID.randomUUID().toString().toLowerCase();
        String customized_id2=UUID.randomUUID().toString().toLowerCase();
        String guidLower=UUID.randomUUID().toString().toLowerCase();
        String cart_number= String.valueOf(new Random().nextInt(99000)+1000);
        long nowTime =new Date().getTime()/1000;
        long timestamp =new Date().getTime()/1000;
        param.put("order_id",order_id);
        param.put("device_id",device_id);
        param.put("dish_item_id1",dish_item_id1);
        param.put("dish_item_id2",dish_item_id2);
        param.put("line_items_id",dish_item_id2);
        param.put("payment_id",payment_id);
        param.put("check_id",check_id);
        param.put("customized_id1",customized_id1);
        param.put("customized_id2",customized_id2);
        param.put("cart_number",cart_number);
        param.put("guidLower",guidLower);
        param.put("nowTime",String.valueOf(nowTime));
        param.put("timestamp",String.valueOf(timestamp));



        SimpleDateFormat dateFormat= new SimpleDateFormat("MM-dd-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK,-1);
        String yesterday=dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_WEEK,1);
        String today=dateFormat.format(calendar.getTime());



        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String monday=dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_WEEK,6);
        String sunday=dateFormat.format(calendar.getTime());


        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_WEEK,-1);
        String lastsunday=dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_WEEK,-6);
        String lastmonday=dateFormat.format(calendar.getTime());


        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-1);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String lastmonth_firstday=dateFormat.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastmonth_lastday=dateFormat.format(calendar.getTime());


        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String thismonth_firstday=dateFormat.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String thismonth_lastday=dateFormat.format(calendar.getTime());
        param.put("yesterday",yesterday);
        param.put("today",today);


        log.info("初始化生成的全局变量 {} 为:",param);


        globalData.putAll(param);




    }



    private void getUsername() {
        Map<String, String> param = new HashMap<>();
        param.put("osUserName", String.valueOf(System.getProperty("user.name")));
        globalData.putAll(param);

    }


}
