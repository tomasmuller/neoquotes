package com.herokuapp.neoquotes.repositories;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.herokuapp.neoquotes.AbstractUnitTest;
import com.herokuapp.neoquotes.models.Stock;

import static org.junit.Assert.assertEquals;

@Transactional
public class StockRepositoryTest extends AbstractUnitTest {

    @Autowired
    private Neo4jTemplate template;
      
    @Autowired
    private StockRepository stockRepository;

    @Test
    public void testStockRepositoryNotNull() {
        assertNotNull(stockRepository);
    }

    @Test
    public void createAndReadStock() {
        final Stock stock = new Stock("NASDAQ");
        final Stock saved = template.save(stock);
        assertEquals("must be same name after save", stock.getName(), saved.getName());
        final Stock loaded = template.findOne(saved.getId(), Stock.class);
        assertEquals("same id after load", saved.getId(), loaded.getId());
        assertEquals("same name after load", stock.getName(), saved.getName());
    }

    @Test
    public void createAndReadStockWithRepository() {
        final Stock stock = new Stock("NYSE");
        final Stock saved = stockRepository.save(stock);
        assertEquals("same name after save", stock.getName(), saved.getName());
        final Stock loaded = stockRepository.findOne(saved.getId());
        assertEquals("same id after load", saved.getId(), loaded.getId());
        assertEquals("same name after load", stock.getName(), saved.getName());
    }

}
