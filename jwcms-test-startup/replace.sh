#replace.sh 用于将上次构建的结果备份，然后将新的构建结果移动到合适的位置
#!/bin/bash
# 先判断文件是否存在，如果存在，则备份
file="C:/Users/yonglu.xie/.jenkins/workspace/platform/jwcms-test-startup/target/jwcms-test-startup.jar"
if [ -f "$file" ]
then
   mv C:/Users/yonglu.xie/.jenkins/workspace/platform/jwcms-test-startup/target/jwcms-test-startup.jar C:/Users/yonglu.xie/.jenkins/workspace/platform/jwcms-test-startup/target/jwcms-test-startup.jar.`date +%Y%m%d%H%M%S`
fi
mv C:/Users/yonglu.xie/.jenkins/workspace/platform/jwcms-test-startup/target/jwcms-test-startup.jar C:/Users/yonglu.xie/.jenkins/workspace/platform/jwcms-test-startup/jwcms-test-startup.jar.`