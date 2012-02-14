package com.herokuapp.neoquotes.repositories;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.herokuapp.neoquotes.AbstractUnitTest;

@Transactional
public class StockRepositoryTest extends AbstractUnitTest {

	@Autowired
	private StockRepository stockRepository;
	
	@Test
	public void testStockRepositoryNotNull() {
		assertNotNull(stockRepository);
	}
	
}
