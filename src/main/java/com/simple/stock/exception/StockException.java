package com.simple.stock.exception;


public class StockException extends RuntimeException {

    public StockException(String errorMessage) {
        super(errorMessage);
    }

    public StockException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}

