package com.simple.stock.repository;

import com.simple.stock.model.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface TradeRepository extends CrudRepository<Trade, Integer> {

    List<Trade> findTradeByStock(@Param("stockSymbol") String stockSymbol);

    @Query(value = "SELECT tt.* FROM T_TRADE tt, T_STOCK ts  WHERE ts.STOCK_SYMBOL = ? AND tt.EXECUTION_TIME > ?", nativeQuery = true)
    List<Trade> findStockByStockSymbolForLastFifteenMinute(@Param("stockSymbol") String stockSymbol, @Param("executionTime") Date executionTime);
}
