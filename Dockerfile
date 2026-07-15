FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY . .

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the project
RUN mvn clean package -DskipTests

# Run the jar file (replace with the actual jar name in target folder)
CMD ["java", "-jar", "target/portfolio-backend-1.0.0.jar"]




