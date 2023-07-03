FROM #{java_11_alpine}#
LABEL version="1.0.0" maintainer="jaamurci@bancolombia.com.co" description="MS Alphanumeric Password - Desencolar"
ENV TZ America/Bogota
EXPOSE 8080
COPY *.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=#{env}#"
USER #{java_user}#
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
