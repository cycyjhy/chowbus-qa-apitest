1. 环境/学习准备
   安装jdk，maven，学习testNG
2. 运行方式
   方式一：
   terminal使用mvn命令运行接口脚本，例如运行pos app下单支付的脚本
   mvn clean test -Dtag=pos_app_stripe_pay
   指定settings.xml请加参数
   mvn clean test -Dtag=tmp -s /User/zhangsan/xxx/.m2/settings.xml
   方式二：
   运行MainTest，看到如下提示：
   2023-10-24 10:07:26 INFO  输入1,执行测试用例: pos_app_stripe_pay
   2023-10-24 10:07:26 INFO  请输入用例编号... 
   根据提示在下面输入数字
3. 运行机制
   1.执行mvn clean test -Dtag=xxx，找到testng.yaml,从而运行com.chowbus.qa.ApiTest
   2.ApiTestDataprovider里@DataProvider使用迭代器，tag总控找到测试用例（以work.yaml结尾）,从而找到worlflow里面的文件夹和对应的yaml文件
   3.ApiTest获取dataProvider的yaml文件
   3.根据yaml里caseType找到对应的step。例如：http，就会执行HttpStep
4. 如何编写测试用例workflow（数据驱动）
   步骤1.在resource-workflow下新建以workflow.yaml结尾的文件，文件包含name，steps，tags，desc
   步骤2.在resource-workflow下新建文件夹，文件夹名称=workflow.yaml里面的name
   步骤3.在步骤二的文件夹添加yaml文件，yaml文件为workflow.yaml里面steps里的yaml文件
   yaml文件里包含api，断言，提取参数，caseType
   yaml转义方式
   1.使用'',与""进行区分
   2.使用>-
   3.使用\
5. 参数传递
   方式一：initGlobalData（初始化需要代码生成的全局变量,例如时间戳，order_id等）
   方式二：initGlobalDataFromYaml（初始化从配置文件globaldata.yaml里的全局变量，这类变量通常是固定不变的常量，例如餐厅ID，url）
   方式三：从responseParam提取（这类全局变量是从接口response获取，存入全局变量，供后续接口入参）
6. 日志
   @Slf4j 日志注解, 可以使用log.方法去打印日志
   日志记录：apitest.log
   idea文件列表没有此文件 解决方法 打开setting 打开File Types 选择文本文档Text，添加后缀*.log 然后就可以正常显示
   

   