package com.simple.stock.utils;


import com.simple.stock.model.Stock;
import com.simple.stock.model.StockType;
import com.simple.stock.model.Trade;

import java.util.List;
import java.util.stream.Collectors;

public class TradeCalculator {

    private TradeCalculator(){

    }

    public static double calculateDividendPerStock(Stock stock, double marketPrice) {
        double dividend = 0;

        if (stock.getType().equals(StockType.Common)) {
            dividend = stock.getLastDividend() / marketPrice;
        } else {
            dividend = (stock.getFixedDividendPercentage() * stock.getParValue()) / (marketPrice * 100);
        }
        return dividend;
    }

    public static double calculateThePERation(Stock stock, double marketPrice) {
        double dividend = calculateDividendPerStock(stock, marketPrice);
        return marketPrice / dividend;

    }

    public static double calculateVolumeWeightedStockPriceForPastFifteenMinute(String stockSymbol, List<Trade> trades) {
        double totalTradePrice = trades.stream().map(s -> s.getTradePrice() * s.getQuantity()).reduce(0.0, Double::sum);
        long totalQuantity = trades.stream().map(s -> s.getQuantity()).reduce((long) 0, Long::sum);
        return totalTradePrice / totalQuantity;
    }

    public static double calculateTheGeometricMeanOfPriceForAllStocks(List<Trade> trades) {
        double product = 1.0;
        List<Double> tradesPrice = trades.stream().map(stock -> stock.getTradePrice()).collect(Collectors.toList());
        for (Double d : tradesPrice) {
            product = product * d;
        }

        return Math.pow(product, 1.0 / tradesPrice.size());
    }
}
