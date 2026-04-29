# Microservice Workshop

This project demonstrates a microservice architecture using Spring Boot and Eureka Naming Server.

## Components

- **Eureka Server**: Service registry on port 8761
- **Hello Service**: Microservice providing "Hello" on port 8083
- **World Service**: Microservice providing "World" on port 8084
- **HelloWorld App**: Client app combining services on port 8082

## How to Run

1. Start Eureka Server:
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

2. Start Hello Service:
   ```bash
   cd hello-service
   mvn spring-boot:run
   ```

3. Start World Service:
   ```bash
   cd world-service
   mvn spring-boot:run
   ```

4. Start HelloWorld App:
   ```bash
   cd helloworld-app
   mvn spring-boot:run
   ```

5. Access Eureka Dashboard: http://localhost:8761

6. Test HelloWorld: http://localhost:8082/helloworld

## Learning Outcomes

- Service registration and discovery with Eureka
- Configuring multiple microservices
- Using RestTemplate with load balancing