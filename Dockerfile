FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

## Windows環境で改行コードがCRLFになっていても実行できるようにする
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

COPY src src

RUN ./gradlew clean bootJar --no-daemon \
    && JAR_FILE=$(find build/libs -maxdepth 1 -type f \
        -name "*.jar" ! -name "*-plain.jar" | head -n 1) \
    && cp "$JAR_FILE" app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
