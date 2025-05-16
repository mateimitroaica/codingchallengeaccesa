
Spring Boot application that tracks and compares product prices across multiple stores 
(e.g., Kaufland, Lidl, Profi), supports price history visualization, finds better-value product 
substitutes, and allows users to check if a product meets a target price on a given date.

On application startup, product and discount data is automatically imported from CSV files 
located in:

-- src/main/resources/data/products
-- src/main/resources/data/discounts

The import is handled by ImportService (located in utils package), it parses and processes the
CSV files
Then, DataInit component runs at startup and calls importService for each file

This application supports two Spring profiles for working with different databases:

-- h2
Uses an in-memory H2 database.
This profile is active by default via: spring.profiles.active=h2 (application.properties)

-- mysql
Uses a real MySQL database in a Docker container.

To run the MySQL container:
    docker-compose up -d
And then change the profile to mysql from application.properties or from terminal:
    ./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
