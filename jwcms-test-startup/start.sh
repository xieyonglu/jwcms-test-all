# startup.sh 启动项目
#!/bin/sh
echo "授予当前用户权限"
chmod 777 C:/Users/yonglu.xie/.jenkins/workspace/platform/jwcms-test-startup/jwcms-test-startup.jar
echo "执行....."
java -jar C:/Users/yonglu.xie/.jenkins/workspace/platform/jwcms-test-startup/jwcms-t est-startup.jar