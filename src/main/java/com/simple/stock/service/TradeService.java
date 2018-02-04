package com.simple.stock.service;


import com.simple.stock.exception.StockException;
import com.simple.stock.model.Stock;
import com.simple.stock.model.Trade;
import com.simple.stock.repository.TradeRepository;
import com.simple.stock.utils.TradeCalculator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TradeService {

    private final static Logger logger = Logger.getLogger(TradeService.class);

    private final long ONE_MINUTE_IN_MILLIS = 60000;

    //TODO we may pass this from configuration, so that we can change this in future if required
    private final long NUMBER_OF_MINUTE = 15;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private StockService stockService;

    public Trade recordTrade(Trade trade) throws StockException {
        String stockSymbol = trade.getStock().getStockSymbol();
        if (null == stockSymbol) {
            throw new StockException("StockSymbol is mandatory, please enter valid stockSymbol in input request");
        }

        Stock stock = stockService.getStock(stockSymbol);

        logger.info("Got stock for stockSymbol: " + stockSymbol);
        Trade trade1 = new Trade(stock, trade.getTradePrice(), trade.getQuantity(), trade.getIndicator());
        return tradeRepository.save(trade1);
    }

    public double calculateVolumeWeightedStockPriceForPastFifteenMinute(String stockSymbol) {
        stockService.getStock(stockSymbol);
        List<Trade> trades = tradeRepository.findStockByStockSymbolForLastFifteenMinute(stockSymbol, getPastFifteenMinuteTime());
        return TradeCalculator.calculateVolumeWeightedStockPriceForPastFifteenMinute(stockSymbol, trades);
    }

    public List<Trade> findAllTrade() {
        return (List<Trade>) tradeRepository.findAll();
    }

    private Date getPastFifteenMinuteTime() {
        Date date = new Date();
        return new Date(date.getTime() - (NUMBER_OF_MINUTE * ONE_MINUTE_IN_MILLIS));

    }


    public double findGBCEForAllTrade() {
        List<Trade> trades = (List<Trade>) tradeRepository.findAll();
        return TradeCalculator.calculateTheGeometricMeanOfPriceForAllStocks(trades);
    }
}
