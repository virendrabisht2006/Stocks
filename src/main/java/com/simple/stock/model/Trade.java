package com.simple.stock.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "T_TRADE")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trade_id")
    private int tradeId;

    @ManyToOne
    @JoinColumn(name = "stock", referencedColumnName = "stock_id")
    private Stock stock;

    private double tradePrice;

    private long quantity;

    private Indicator indicator;

    private Date executionTime;

    public Trade(Stock stock, double tradePrice, long quantity, Indicator indicator) {
        this.stock = stock;
        this.tradePrice = tradePrice;
        this.quantity = quantity;
        this.indicator = indicator;
        this.executionTime = new Date();
    }

    public Trade() {
    }


    public Stock getStock() {
        return stock;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public long getQuantity() {
        return quantity;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public int getTradeId() {
        return tradeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        if (Double.compare(trade.tradePrice, tradePrice) != 0) return false;
        if (quantity != trade.quantity) return false;
        if (!stock.equals(trade.stock)) return false;
        return indicator == trade.indicator;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = stock.hashCode();
        temp = Double.doubleToLongBits(tradePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (quantity ^ (quantity >>> 32));
        result = 31 * result + indicator.hashCode();
        return result;
    }
}
