FROM java:8
COPY ./target/octolook.jar /app.jar
ENV PORT=8080
ENV REPL=7888
EXPOSE 8080 7888
ENTRYPOINT ["java", "-jar", "/app.jar"]