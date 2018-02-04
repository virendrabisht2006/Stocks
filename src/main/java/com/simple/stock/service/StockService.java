package com.simple.stock.service;

import com.simple.stock.exception.StockException;
import com.simple.stock.model.Stock;
import com.simple.stock.repository.StockRepository;
import com.simple.stock.utils.TradeCalculator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final static Logger logger = Logger.getLogger(StockService.class);
    @Autowired
    private StockRepository stockRepository;

    public void save(List<Stock> stocks) {
        //TODO when loaded from file, to remove duplicate stock it delete all record first, could be improved or may be soft delete
        stockRepository.deleteAll();
        stockRepository.save(stocks);
    }

    public void save(Stock stock) {
        stockRepository.save(stock);
    }

    public double calculateDividend(String stockSymbol, double marketPrice) throws StockException {
        return TradeCalculator.calculateDividendPerStock(getStock(stockSymbol), marketPrice);
    }

    public double calculatePERatio(String stockSymbol, double marketPrice) throws StockException {
        return TradeCalculator.calculateThePERation(getStock(stockSymbol), marketPrice);
    }

    public Stock getStock(String stockSymbol) {
        Stock stock = null;
        try {
            stock = stockRepository.findByStockSymbol(stockSymbol);
            if (stock == null) {
                throw new StockException("Invalid Stock :" + stockSymbol + ",Stock not available." +
                        " Kindly call API '/v1/stocks' for getting list of valid stock in the system");
            }
        } catch (Exception e) {
            logger.error("Record not found for symbol: " + stockSymbol);
            throw e;
        }
        return stock;
    }

    public List<Stock> findAllStock() {
        return (List<Stock>) stockRepository.findAll();
    }
}
