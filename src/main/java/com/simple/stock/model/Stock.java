package com.simple.stock.model;

import javax.persistence.*;

@Entity
@Table(name = "T_STOCK",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"STOCK_SYMBOL", "TYPE"}))
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stock_id")
    private int stockId;

    @Column(name = "STOCK_SYMBOL")
    private String stockSymbol;

    @Column(name = "TYPE")
    private StockType type;

    private long lastDividend;

    private double fixedDividendPercentage;

    private long parValue;

    public Stock(String stockSymbol, StockType type, long lastDividend, double fixedDividendPercentage,
                 long parValue) {
        this.stockSymbol = stockSymbol;
        this.type = type;
        this.lastDividend = lastDividend;
        this.fixedDividendPercentage = fixedDividendPercentage;
        this.parValue = parValue;
    }

    public Stock() {
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public StockType getType() {
        return type;
    }

    public double getFixedDividendPercentage() {
        return fixedDividendPercentage;
    }

    public long getLastDividend() {
        return lastDividend;
    }

    public long getParValue() {
        return parValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;

        if (lastDividend != stock.lastDividend) return false;
        if (Double.compare(stock.fixedDividendPercentage, fixedDividendPercentage) != 0) return false;
        if (parValue != stock.parValue) return false;
        if (!stockSymbol.equals(stock.stockSymbol)) return false;
        return type == stock.type;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = stockSymbol.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (int) (lastDividend ^ (lastDividend >>> 32));
        temp = Double.doubleToLongBits(fixedDividendPercentage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (parValue ^ (parValue >>> 32));
        return result;
    }
}
