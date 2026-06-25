# Maven සහ Java තියෙන image එකක් තෝරා ගැනීම
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Run කිරීමට අවශ්‍ය Java image එක
FROM eclipse-temurin:17-jre
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]