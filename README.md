# Stocks
This is super simple stocks calculator Application. This Stock application built up using spring boot 
and provide a LIST of API which will are as follow. 

# Library Used
-----------------------------------------------------------------------------------------
| Library                        | Version         | Description                        |
-----------------------------------------------------------------------------------------
| spring-boot-starter-parent     | 1.5.7.RELEASE    | To enable spring boot application |
| spring-boot-starter-data-jpa   | 1.5.7.RELEASE    |     -                             |
| spring-boot-starter-web        | 1.5.7.RELEASE    |     -                             |
| spring-boot-starter-data-rest  | 1.5.7.RELEASE    |     -                             |
| spring-boot-starter-actuator   | 1.5.7.RELEASE    | For health check                  |
| h2database                     | 1.4.182          | For in-memory database            |
| mockito-all                    | 1.10.8           | Mocking Framework                 |
| log4j                          | 1.2.17           | For logging                       |
| junit                          | 4.12             | For unit testing, followed TDD    |
| swagger                        | 1.5.16           | For API Documentation             |
-----------------------------------------------------------------------------------------

# Assumption
The stock data will be loaded first before starting for trade. Only listed stock can be traded. And also stock data will be 
", " i.e. comma separated. like below.
StockSymbol,Type, LastDividend, FixDividend,ParValue
TEA,Common,0,,100

# API Exposed:

Method= GET, URL:http://localhost:8080/health
Description: This will help to get the complete health of system example: status, memory, and external interface status like h2 database.

Method= GET, URL:/v1/stocks/welcome
Description: This API will provide welcome screen and may be used for just to check if application is reachable or not.

Method= GET, URL: /v1/stocks/load
Description: This might be the first operation you would like to do. Based on sample data it loads the Stock data.
              For your testing you might be interested for more complex data, you can edit sample-stock.csv file available in classpath and call load API.
              You can check the loaded stock data with /v1/stocks API.

Method= POST, URL: /v1/stocks
Description: This API will store/add stock in system. This is POST request through POSTMAN or REST template
             you can calling this API with below Json as example. 

{
"stockSymbol":"SBI",
"type":"Common",
"lastDividend":125,
"fixedDividendPercentage":8,
"parValue": 328
}

Method= GET, URL: /v1/stocks
Description: Find all available stocks, which we can traded.

Method= GET, URL: /v1/stocks/stockSymbol/{stockSymbol}  -- verify again
Description: Get the stock data for given stockSymbol, if stock not available in system will throw a StockException.

Method= GET, URL: /v1/stocks/dividend/stockSymbol/{stockSymbol}/marketPrice/{marketPrice}
Description: API will calculate Dividend for given stockSymbol and marketPrice should be pass as path variable.
             If stocks not found it will throw a StockException.

Method= GET, URL: /v1/stocks/PERatio/stockSymbol/{stockSymbol}/marketPrice/{marketPrice}
Description: API will calculate Profit and Ratio(PERatio) for given stockSymbol and marketPrice should be pass as path variable.
             If stocks not found it will throw a StockException.


Method= POST, URL: /v1/stocks/trade
Description: This API will execute the trade.It assume that stock already present in system. If given stock not available
              It will throw a StockException. This POST request one can execute Trade by call this API through POSTMAN or REST template
              by passing below json in body of request.

NOTE- stockSymbol and type is mandatory and composite unique constraint, other parameter of stock object could be null.

{
"stock":{
"stockSymbol":"TEA",
"type": null,
"lastDividend": null,
"fixedDividendPercentage":null,
"parValue": null
},
"tradePrice": 125.25,
"quantity": 120,
"indicator": "BUY"
}

Method= GET, URL: /v1/stocks/VWS/{stockSymbol}
Description: This API will calculate Volume Weighted Stock Price based on trades in past 15 minutes for given stock.

Method= GET, URL: /v1/stocks/GBCE
Description: This API will calculate the GBCE All Share Index using the geometric mean of prices for all stocks

# In-Memory DB

To see the data state, open URL: http://localhost:8080/console 

