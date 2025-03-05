# Test Assignment - Solution

## The Challenge - Api Description

- As a client, I want to get a list of all available currencies
  - URL: /api/currencies
  - Method: GET
- As a client, I want to get all EUR-FX exchange rates at all available dates as a collection
  - URL: /api/exchange-rates
  - Method: GET
- As a client, I want to get the EUR-FX exchange rate at particular day
  - URL: /api/exchange-rates/{date}
  - Method: GET
  - URL Parameters: date in yyyy-mm-dd format
  - Query Parameters: 3 Digit currency code[Optional]
- As a client, I want to get a foreign exchange amount for a given currency converted to EUR on a particular day
  - URL: /api/convert
  - Method: GET
  - URL Parameters: amount, 3 Digit currency code, in yyyy-mm-dd format
 
## Setup
#### Requirements
- Java 11 (will run with OpenSDK 15 as well)
- Maven 3.x

#### Swagger
Swagger has been used for API description, After starting application click on below:

http://localhost:8080/swagger-ui/index.html


#### Project
The project was generated through the Spring initializer [1] for Java
 11 with dev tools and Spring Web as dependencies. In order to build and 
 run it, you just need to click the green arrow in the Application class in your Intellij 
 CE IDE or run the following command from your project root und Linux or ios. 


````shell script
$ mvn spring-boot:run
````

After running, the project, switch to your browser and hit http://localhost:8080/api/currencies. You should see some 
demo output. 


[1] https://start.spring.io/

[2] [Bundesbank Daily Exchange Rates](https://www.bundesbank.de/dynamic/action/en/statistics/time-series-databases/time-series-databases/759784/759784?statisticType=BBK_ITS&listId=www_sdks_b01012_3&treeAnchor=WECHSELKURSE)
