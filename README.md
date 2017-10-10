<h1>基于Spring-Boot和Spring-Cloud的微服务架构</h1>
<h5>1.简介</h5>
这是个基于Spring-Boot和Spring-Cloud的微服务架构，项目采用项目依赖的分层架构

(1) jwcms-test-all<br/>
所有工程的父类，包含所有的子工程

(2) jwcms-test<br/>
需要对外暴露API的工程，包括需要暴露的service和model

(3) jwcms-test-common<br/>
公共类工程

(4) jwcms-test-dao<br/>
dao层工程

(5) jwcms-test-service<br/>
service层工程

(6) jwcms-test-controller<br/>
controller层工程

(7) jwcms-test-startup<br/>
启动工程，包括启动配置和Application启动类

<h5>2.使用</h5>
(1) 下载载项目工程<br/>
(2) 导入到Eclipse（或者其它IDE工具），并编译好工程<br/>
(3) 在MySQL中执行sql包下的脚本<br/>
(4) 根据需要调整application.yml中的相关配置<br/>
(5) 启动Application.java<br/>

<h6>3.测试</h6>
接口测试：http://localhost:8080/jwcms/user/query_user_by_id/1<br/>
Swagger-UI测试：http://localhost:8080/swagger-ui.html
