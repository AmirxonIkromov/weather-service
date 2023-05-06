Weather Service

The Weather Service is a REST API service that allows users to retrieve and add current weather information for different cities. The API includes methods for both clients and administrators. The service utilizes token-based authentication (JWT).

Getting Started

Prerequisites
JDK 11
Spring Boot
WebFlux
Liquibase
SQL DB (Postgres)
JUnit
Mockito


Installation
Clone the repository
Create a new database in your SQL server of choice
Configure the database connection properties in the application.properties file
Run the application using the command ./mvnw spring-boot:run or by running the main class WeatherServiceApplication

Usage

Admin Methods

/user-list: retrieves a list of all users
/user-details: retrieves details about user subscriptions
/edit-user: edits user information
/cities-list: retrieves a list of all cities
/edit-city: edits city information
/update-city-weather: updates the weather information for a city

Client Methods

/register: retrieves a permanent token using a username and password
/cities-list: retrieves a list of all available cities
/subscribe-to-city: subscribes to a specific city
/get-subscriptions: retrieves weather information for all subscribed cities (using reactive programming)

Authors

[Amirkhon]
