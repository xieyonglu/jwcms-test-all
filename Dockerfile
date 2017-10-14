FROM frolvlad/alpine-oraclejdk8:slim
COPY ./jwcms-test-startup/target/jwcms-test-startup.jar /root/startup/
WORKDIR /root/startup/
EXPOSE 8080
CMD ["java","-Xms512m","-Xmx512m","-DAPP_DOMAIN=jwcms-test","-jar", "jwcms-test-startup.jar"]