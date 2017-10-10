# 基于Spring-Boot和Spring-Cloud的微服务架构
这是个基于Spring-Boot和Spring-Cloud的微服务架构，项目采用项目依赖的分层架构

1. jwcms-test-all<br/>
所有工程的父类，包含所有的子工程

2. jwcms-test<br/>
需要对外暴露API的工程，包括需要暴露的service和model

3. jwcms-test-common<br/>
公共类工程

4. jwcms-test-dao<br/>
dao层工程

5. jwcms-test-service<br/>
service层工程

6. jwcms-test-controller<br/>
controller层工程

7. jwcms-test-startup<br/>
启动工程，包括启动配置和Application启动类