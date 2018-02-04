package com.simple.stock.utils;


import com.simple.stock.model.Stock;
import com.simple.stock.model.StockType;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DataLoader {

    private static final String DELIMETER = ",";

    private final static Logger logger = Logger.getLogger(DataLoader.class);

    private DataLoader() {
    }


    public static List<Stock> loadStock(String fileName) {
        List<Stock> stocks = new ArrayList<>();
        String file = String.valueOf(DataLoader.class.getClassLoader().getResource(fileName).getFile());

        try (Stream<String> lines = Files.lines(Paths.get(file)).skip(1)) {
            lines.forEach(line -> {
                String trade[] = line.split(DELIMETER);
                String stockSymbol = trade[0];
                StockType type = StockType.valueOf(trade[1]);

                Optional<String> lastDividendOptional = Optional.ofNullable(trade[2]);
                long lastDividend = lastDividendOptional.isPresent() ? Long.valueOf(lastDividendOptional.get()) : 0;
                Optional<String> fixedDividendPercentageOptional = Optional.ofNullable(trade[3]);
                Double fixedDividendPercentage = fixedDividendPercentageOptional.isPresent() && !fixedDividendPercentageOptional.get().isEmpty() ?
                        Double.valueOf(fixedDividendPercentageOptional.get()) : 0;

                Optional<String> parValueOptional = Optional.ofNullable(trade[4]);
                long parValue = parValueOptional.isPresent() ? Long.valueOf(parValueOptional.get()) : 0;

                Stock s1 = new Stock(stockSymbol, type, lastDividend, fixedDividendPercentage, parValue);
                stocks.add(s1);
            });

        } catch (IOException e) {
            logger.error("Exception occurred while reading file: ", e);
            e.printStackTrace();
        }

        return stocks;
    }
}
