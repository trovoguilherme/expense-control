FROM openjdk:17-jdk

COPY build/libs/expense-control*.jar expense-control.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "expense-control.jar"]