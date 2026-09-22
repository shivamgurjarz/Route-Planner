FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY src ./src
COPY public ./public

RUN javac -d out src/*.java

EXPOSE 8080
CMD ["java", "-cp", "out", "Main"]
