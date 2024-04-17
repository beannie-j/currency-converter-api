# Currency Converter API

This is a Spring WebFlux application for converting currencies using external API.

## Features

- **Currency Conversion:** Convert currencies based on the latest exchange rates retrieved from an external API.

## Technologies Used

- **Spring Boot:** For building and running the application.
- **Spring WebFlux:** For handling asynchronous, non-blocking requests.
- **Springdoc OpenAPI:** For generating API documentation using OpenAPI specification.
- **Reactor Core:** For reactive programming.

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/beannie-j/currency-converter-api.git

2. **Build the project:**
   ```bash
   cd currency-converter-api
   ./gradlew build

3. **Run the application:**
   ```bash
   ./gradlew bootRun

4. **Access the API:**
    - Base URL: http://localhost:8080
    - Endpoint: /exchange-rate
    - Example: http://localhost:8080/exchange-rate?baseCurrency=USD

## API Documentation
The API documentation is generated using Springdoc OpenAPI. Once the application is running, you can access the Swagger UI at:
- http://localhost:8080/swagger-ui/index.html

## Future Plans

This project aims to grow into a comprehensive currency conversion service, providing users with the best rates of
currency exchange and more. Stay tuned for updates!