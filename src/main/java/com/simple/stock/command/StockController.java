package com.simple.stock.command;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.stock.model.Stock;
import com.simple.stock.model.Trade;
import com.simple.stock.service.StockService;
import com.simple.stock.service.TradeService;
import com.simple.stock.utils.DataLoader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
@RequestMapping(value = "/v1/stocks")
@Api(value = "/v1/stocks", description = "The api manage the stocks traded, and would be used loading stocks to store " +
        "and for a given stock calculate the following " +
        "Given a market price as input, calculate the dividend yield" +
        "Given a market price as input,  calculate the P/E Ratio etc")
public class StockController {

    private static final String FILE_INSTRUCTION = "sample-stock.csv";

    private final static Logger logger = Logger.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @Autowired
    private TradeService tradeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String response = null;


    @RequestMapping(value = "/dividend/stockSymbol/{stockSymbol}/marketPrice/{marketPrice}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks/{stockSymbol}/marketPrice/{marketPrice}", notes = "This api will get the dividend yield for given stock and market price",
            responseContainer = "Return HTTP 200 code on success if stock found and dividend calculated.")
    public ResponseEntity<?> getDividendForStock(@PathVariable("stockSymbol") String stockSymbol, @PathVariable("marketPrice") double marketPrice,
                                                 UriComponentsBuilder ucBuilder) {
        logger.info("About to calculate dividend  for stock: " + stockSymbol);

        double dividendYield = stockService.calculateDividend(stockSymbol, marketPrice);
        response = "Yield Dividend: " + dividendYield;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/stocks").buildAndExpand().toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/PERatio/stockSymbol/{stockSymbol}/marketPrice/{marketPrice}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks/PERatio/{stockSymbol}/marketPrice/{marketPrice}", notes = "APi will calculate profit and earn ration for given stock for given market price",
            responseContainer = "Return HTTP 200 code on success if stock found and profit and earn ratio calculated.")
    public ResponseEntity<?> getProfitAndEarnRatioForStock(@PathVariable("stockSymbol") String stockSymbol, @PathVariable("marketPrice") double marketPrice,
                                                           UriComponentsBuilder ucBuilder) {
        logger.info("About to calculate the profit and earn ration for stock : " + stockSymbol);

        double peRation = stockService.calculatePERatio(stockSymbol, marketPrice);
        response = "P/E Ratio: " + peRation;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/stocks").buildAndExpand().toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/trade", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks/trade", notes = "APi will execute the trade and store in db",
            responseContainer = "Return HTTP 200 code on success trade executed.")
    public ResponseEntity<?> recordTrade(@RequestBody Trade trade, UriComponentsBuilder ucBuilder) {
        logger.info("About to execute the trade  for stock: " + trade);

        Trade trade1 = tradeService.recordTrade(trade);
        response = parseObjectAsString(trade1);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/trade/" + trade1.getTradeId()).buildAndExpand().toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/VWS/{stockSymbol}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks/VWS/{stockSymbol}", notes = "The API Calculate Volume Weighted Stock Price based on trades in past 15 minutes",
            responseContainer = "Return HTTP 200 code on success and all trades executed in last 15 minute")
    public ResponseEntity<?> getVolumeWeightedStock(@PathVariable("stockSymbol") String stockSymbol, UriComponentsBuilder ucBuilder) {
        logger.info("About to  get volume weighted stock");
        double vwStockPrice = tradeService.calculateVolumeWeightedStockPriceForPastFifteenMinute(stockSymbol);
        logger.debug("Calculated VWS Price: " + vwStockPrice);

        response = "Volume Weighted Stock Price for stock: " + stockSymbol + " in past 15 minutes: " + vwStockPrice;

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/GBCE", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks/GBCE", notes = "The API Calculate the GBCE All Share Index using the geometric mean of prices for all stocks",
            responseContainer = "Return HTTP 200 code on success return the GBCE for all trades")
    public ResponseEntity<?> getGBCEForAllStock(UriComponentsBuilder ucBuilder) {
        logger.info("About to  GBCE for All trade ");
        double gbceForAllTrade = tradeService.findGBCEForAllTrade();

        response = "GBCE for ALl Trade: " + gbceForAllTrade;

        logger.debug("All stock GBCE: " + gbceForAllTrade);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/stocks").buildAndExpand().toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/stockSymbol/{stockSymbol}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks/stockSymbol/{stockSymbol}", notes = "The API Calculate the GBCE All Share Index using the geometric mean of prices for all stocks",
            responseContainer = "Return HTTP 200 code on success return the GBCE for all trades")
    public ResponseEntity<?> getStock(@PathVariable("stockSymbol") String stockSymbol, UriComponentsBuilder ucBuilder) throws JsonProcessingException {
        logger.info("Get stock for given stockSymbol: " + stockSymbol);
        Stock stock = stockService.getStock(stockSymbol);
        response = parseObjectAsString(stock);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/stocks").buildAndExpand().toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks", notes = "The API Calculate the GBCE All Share Index using the geometric mean of prices for all stocks",
            responseContainer = "Return HTTP 200 code on success return the GBCE for all trades")
    public ResponseEntity<?> getAllStock(UriComponentsBuilder ucBuilder) throws JsonProcessingException {
        List<Stock> stocks = stockService.findAllStock();
        response = parseObjectAsString(stocks);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/stocks").buildAndExpand().toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ResponseBody
    @ApiOperation(value = "/v1/stocks", notes = "Add stock and store into db, this might first operation to start",
            responseContainer = "Return HTTP 200 code on success and URI to query added stock")
    public ResponseEntity<?> addStock(@RequestBody Stock stock, UriComponentsBuilder ucBuilder) {
        logger.info("About to add stocks");
        stockService.save(stock);
        Stock stock1 = stockService.getStock(stock.getStockSymbol());
        response = "Stock persisted successfully to check call API 'v1/stocks/stockSymbol/{stockSymbol}' ";
        response = parseObjectAsString(stock1);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/stockSymbol/{stockSymbol}").buildAndExpand(stock.getStockSymbol()).toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/load", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks/load", notes = "Load data from file and store into db, this might first operation to start",
            responseContainer = "Return HTTP 200 code on success and URI to query all added stocks")
    public ResponseEntity<?> loadStocks(UriComponentsBuilder ucBuilder) {
        logger.info("About to loadStock stocks from  file");
        List<Stock> stocks = DataLoader.loadStock(FILE_INSTRUCTION);
        stockService.save(stocks);
        response = "Stock persisted successfully to check call API 'v1/stocks' ";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/stocks").buildAndExpand().toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/stocks/welcome", notes = "Welcome to stock application, just for health check",
            responseContainer = "Return HTTP 200 code on success and URI for all stocks API")
    public ResponseEntity<?> welcome(UriComponentsBuilder ucBuilder) {
        logger.info("Welcome to stock application");
        response = "Welcome to stock application is up and running+\n\n\n";
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    private String parseObjectAsString(Object object) {
        try {
            response = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException jpe) {
            logger.error("Exception in parsing the object: " + jpe.getMessage());
        }
        return response;
    }
}
