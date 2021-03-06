package com.herokuapp.neoquotes.repositories;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.herokuapp.neoquotes.AbstractUnitTest;

@Transactional
public class SymbolRepositoryTest extends AbstractUnitTest {

    @Autowired
    private SymbolRepository symbolRepository;

    /**
     * We can use <code>template.fetch(node.getProperty());</code>
     * to fetch data when needed.
     */
    @Autowired 
    private Neo4jTemplate template;
    
    @Test
    public void testSymbolRepositoryNotNull() {
        assertNotNull(symbolRepository);
    }

}
