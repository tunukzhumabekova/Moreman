FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app
COPY . ./
RUN chmod +x gradlew && ./gradlew clean build -x test || { echo "Build failed"; exit 1; }

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/Moreman-0.0.1-SNAPSHOT.jar ./
CMD ["java", "-jar", "Moreman-0.0.1-SNAPSHOT.jar"]
EXPOSE 2023
