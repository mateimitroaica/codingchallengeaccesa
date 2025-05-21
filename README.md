
Spring Boot application that tracks and compares product prices across multiple stores 
(e.g., Kaufland, Lidl, Profi), supports price history visualization, finds better-value product 
substitutes, and allows users to check if a product meets a target price on a given date.

On application startup, products and discounts data is automatically imported from CSV files 
located in:

-- src/main/resources/data/products
-- src/main/resources/data/discounts

Data import is handled by the ImportService class, located in the utils package. It is responsible 
for parsing and processing the CSV files. At application startup, the DataInit component (init/) 
is automatically executed. It calls the ImportService for each CSV file only if the database is 
empty, ensuring that data is not re-imported on every run.

This application supports two Spring profiles for working with different databases:
-- h2
Uses an in-memory H2 database.
This profile is active by default via: spring.profiles.active=h2 (inside application.properties)

-- mysql
Uses a real MySQL database in a Docker container.
To start the MySQL container, from the root of your project directory run:
    docker compose up -d    (this will read the docker-compose.yml, download the mysql image
                             if needed and create and start a container with the configured settings)
Set the active profile to 'mysql' from application.properties (change 'h2' to 'mysql')

If you want you can leave the active profile to 'h2' and work with the in-memory database.
To open the H2 Console in your browser, go to:
    http://localhost:8080/h2-console
Use the following login credentials:
JDBC URL: jdbc:h2:mem:testdb 
Username: sa
Leave password blank. Click 'Test connection' and then 'Connect'. This way you'll be able to 
view all tables, columns, types, constraints etc.

There are 6 endpoints defined in the controller, each implementing a required feature of the application. 
To access the endpoints, you can use:
    - a web browser 
    - tools like postman, bruno
The base URL is:
    http://localhost:8080/api/products
Each route under this base path serves a different purpose

1. Daily Shopping Basket Monitoring
   This feature helps users optimize their shopping list by identifying the cheapest store for 
each product on a specific date. The app determines the cheapest available price per product across all 
stores. It selects prices based on price updates (on the 1st and 8th). It returns a list of products
and associated stores and the total cost of the optimized basket.
Endpoint:
    GET /api/products/best-deals?date=

date:   date of shopping (based on it, it will look up for products that are either on the 1st or 8th)
You also have to provide in the body of the request a list with product names to search for
Ex.: ["lapte zuzu","ulei floarea-soarelui","șampon păr gras","morcovi","cașcaval","detergent lichid"]

2. Best Discounts
   This feature lists all products with the highest current percentage discounts across all 
tracked stores, allowing users to easily find the best deals available right now. It filters all discounts that are 
active on a given date, maps those discounts to their associated products and stores, sorts them
in descending order by discount percentage, returns a list of products with their discount info.
Endpoint:
   GET /api/products/top-discounts?date=

date:   the date to check for active discounts (YYYY-MM-DD)

3. New Discounts
This feature lists all discounts that were recently added, helping users discover new deals 
introduced within the past 24 hours. It filters discounts whose starting date is within the last 
24 hours (from the given date), maps each discount to its corresponding product and store and 
sorts the result by lowest discounted price first.
Endpoint:
    GET /api/products/new-discounts?date=

date:   the current date (YYYY-MM-DD) used to check for "new" discounts added in the last day

4. Dynamic Price History Graphs
This feature provides time-series pricing data for products, allowing a frontend to generate 
price history graphs and visualize trends over time. It collects historical prices for all products across all stores,
applies any active discounts at the time of each price point, returns a chronological list of price data points for each 
product and supports filtering by store, brand, product category

Endpoint:
    GET /api/products/price-history
Optional Query Parameters:
store -> filter by store name (ex.: kaufland)
category -> filter by product category (ex.: lactate)
brand -> filter by brand (ex.: Olympus)

5. Product Substitutes & Recommendations
This feature helps users identify better value alternatives to a searched product by calculating 
and comparing the value per unit (e.g., price per kg or liter). It’s especially useful when pack 
sizes differ but users want the best deal. It takes a product name as input and finds matching store entries,
calculates the value per unit for each (price ÷ quantity), identifies the best-priced match as the base 
product then finds and recommends similar products (same brand & category) that: have a lower value
per unit, are from different stores or variations of the product, returns the base product and a sorted list of cheaper 
alternatives.

Endpoint:
    GET /api/products/recommendations?productName=

productName:    the name of the product to compare

6. Custom Price Alert
This feature allows users to set a target price for a product and check whether any store is 
currently offering it at or below that price — including discounts. It accepts a product name, target price, 
and date, searches for that product across all stores on the given date, checks if any active discounts apply,
calculates final (discounted) price and compares it to the target price, returns a list of stores that match or beat the alert 
threshold. 

Endpoint:
    GET /api/products/price-alert

Request Body (JSON):
{
"productName": "banane",
"targetPrice": 6.2,
"date": "2025-05-08"
}