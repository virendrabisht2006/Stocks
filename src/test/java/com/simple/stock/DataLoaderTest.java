package com.simple.stock;


import com.simple.stock.model.Stock;
import com.simple.stock.model.StockType;
import com.simple.stock.utils.DataLoader;
import org.junit.Test;

import java.util.List;

public class DataLoaderTest {

    private final String fileName = "sample-stock-test.csv";

    @Test
    public void shouldLoadDataFromFile() {
        Stock expectedStock = new Stock("TEA", StockType.Common, 0, 0.0, 100);

        List<Stock> actualStocks = DataLoader.loadStock(fileName);
        assert (actualStocks.get(0)).equals(expectedStock);
    }
}