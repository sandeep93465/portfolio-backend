# Use official OpenJDK image
FROM eclipse-temurin:17-jdk


# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests

# Run the jar file
CMD ["java", "-jar", "target/portfolio-backend-0.0.1-SNAPSHOT.jar"]
