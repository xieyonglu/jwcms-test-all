# startup.sh 启动项目
#!/bin/sh
echo "授予当前用户权限"
chmod 777 /usr/local/software/jenkins/jenkins-shell/jwcms-test-startup.jar
echo "执行....."
java -jar /usr/local/software/jenkins/jenkins-shell/jwcms-test-startup.jar