package com.simple.stock.repository;


import com.simple.stock.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface StockRepository extends CrudRepository<Stock, Integer> {

    Stock findByStockSymbol(@Param("stockSymbol") String stockSymbol);
}
