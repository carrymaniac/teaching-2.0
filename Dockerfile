FROM java:8

ADD teaching-0.0.1-SNAPSHOT.jar app.jar
# 修改这个文件的访问时间和修改时间为当前时间。
RUN bash -c 'touch /app.jar'
EXPOSE 8080
# 和CMD差不多
# 格式为命令 参数1 参数2
ENTRYPOINT ["java","-Dspring.profiles.active=pro -Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
MAINTAINER 黄冠翰<hgh3863399@gmail.com>