package com.simple.stock.utils;

import com.simple.stock.model.Indicator;
import com.simple.stock.model.Stock;
import com.simple.stock.model.Trade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class TradeCalculatorTest {

    private final String fileName = "sample-stock-test.csv";
    private List<Stock> stockList;
    private List<Trade> trades;

    @Before
    public void init() {

        stockList = DataLoader.loadStock(fileName);
        trades = preapreTradeDataForTest();
    }


    @Test
    public void shouldCalculateDividendForGivenStockForMarketPrice() {
        double marketPrice = 225;
        double expectedDividendForCommonType = 0;
        Stock stock = stockList.get(0);

        double actualDividend = TradeCalculator.calculateDividendPerStock(stock, marketPrice);

        assert (expectedDividendForCommonType == actualDividend);

        Stock stockPreferred = stockList.get(3);
        double expectedDividend = 0.008888888888888889;
        double actualDividendPreferredType = TradeCalculator.calculateDividendPerStock(stockPreferred, marketPrice);
        assert (actualDividendPreferredType == expectedDividend);


    }

    @Test
    public void shouldCalculateThePERatioForGivenStockForMarketPrice() {

        double marketPrice = 225;

        double expectedPERatio = 25312.5;
        double actualPERatio = TradeCalculator.calculateThePERation(stockList.get(3), marketPrice);
        assert (expectedPERatio == actualPERatio);


    }

    //ToDO need to check after refactor
    @Test
    public void shouldCalculateTheGeometricMeanOfAllStock() {
        //List<Double> stockPriceList = Arrays.asList(2.0,8.0,2.0,4.0,8.0);

        double expectedGeoMean = 4.0;

        double actualGeoMean = TradeCalculator.calculateTheGeometricMeanOfPriceForAllStocks(trades);
        assert (actualGeoMean == expectedGeoMean);
    }

    //TODO after refactor
    @Test
    public void shouldCalculateVolumeWeightedStockPriceForPastFifteenMinute() {
        String stockSymbol = "TEA";
        List<Stock> filterStockBySymbol = stockList.stream().filter(s -> s.getStockSymbol().equals(stockSymbol)).collect(Collectors.toList());

        double expectedValue = (2.0 * 120 + 8.0 * 100 + 4.0 * 100 + 2.0 * 500 + 8.0 * 550) / (120 + 100 + 100 + 500 + 550);

        double actualValue = TradeCalculator.calculateVolumeWeightedStockPriceForPastFifteenMinute(stockSymbol, trades);
        assert (actualValue == expectedValue);

    }

    private List<Trade> preapreTradeDataForTest() {
        Trade trade1 = new Trade(stockList.get(0), 2.0, 120, Indicator.BUY);
        Trade trade2 = new Trade(stockList.get(1), 8.0, 100, Indicator.BUY);
        Trade trade3 = new Trade(stockList.get(2), 4.0, 100, Indicator.BUY);
        Trade trade4 = new Trade(stockList.get(3), 2.0, 500, Indicator.BUY);
        Trade trade5 = new Trade(stockList.get(4), 8.0, 550, Indicator.BUY);

        return Arrays.asList(trade1, trade2, trade3, trade4, trade5);

    }
}