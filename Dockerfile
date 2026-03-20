# ----- Stage 1: Build -----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# ----- Stage 2: Runtime -----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Install Python + pip for mlflow CLI (needed for build-docker)
RUN apt-get update && \
    apt-get install -y --no-install-recommends python3 python3-pip python3-venv && \
    python3 -m venv /opt/mlflow-venv && \
    /opt/mlflow-venv/bin/pip install --no-cache-dir mlflow boto3 && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENV PATH="/opt/mlflow-venv/bin:${PATH}"

COPY --from=build /app/target/controllers-tmf915-*.jar app.jar

EXPOSE 13082

ENTRYPOINT ["java", "-jar", "app.jar"]
