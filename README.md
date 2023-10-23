1. 说明
2. 运行方式
   测试demo脚本
   使用mvn命令运行演示脚本
   mvn clean test -Dtag=http
   指定settings.xml请加参数
   mvn clean test -Dtag=tmp -s /User/zhangsan/xxx/.m2/settings.xml
   负责给@Test方法提供测试数据: ApiTestDataprovider.java
   测试入口类: ApiTest.java
3. 测试文件说明
   3.1 总控文件: template.workflow.yaml
   用来控制执行哪些http接口脚本
   文件命名规则: xxx.workflow.yaml
   