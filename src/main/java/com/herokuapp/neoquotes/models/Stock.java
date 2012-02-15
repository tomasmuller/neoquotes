package com.herokuapp.neoquotes.models;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.helpers.collection.ClosableIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.transaction.annotation.Transactional;

import com.herokuapp.neoquotes.repositories.StockRepository;

@NodeEntity
@Configurable
public class Stock implements Arborable {

	@Autowired
	private transient StockRepository stockRepository;

    @GraphId
    private Long id;
    
    @Indexed(indexType = IndexType.FULLTEXT, indexName = "searchByStockName")
    private String name;

    @RelatedTo(type = "LISTED_COMPANIES", direction = Direction.OUTGOING)
    @Fetch private Set<Company> companies;


    /**
     * Empty constructor.
     */
    public Stock() { }

    /**
     * Constructor with stock name.
     * @param name
     */
    public Stock(String name) {
		this.name = name;
	}
    
    /**
     * Find or create Stock with the given name.
     * @param stockName
     * @return Stock
     */
    @Transactional
	public Stock findOrCreate(String stockName) {
		Stock stock = stockRepository.findByPropertyValue("name", stockName);
		if (stock == null) {
			stock = new Stock(stockName);
			stock.persist();
		}
		return stock;
	}
    
    @Transactional
    public ClosableIterable<Stock> findAll() {
    	return stockRepository.findAll();
    }
	
    @Override
    public String nodeToArborJsJson() {
		return "\"" + getName() + "\"" + Arborable.STOCK_NODES_CONFIG;
	}

    @Override
    public String edgesToArborJsJson() {
    	StringBuilder edges = new StringBuilder();
    	edges.append("\"" + getName() + "\":{");
    	
    	if (getCompanies() != null && !getCompanies().isEmpty()) {
        	for (Company c : getCompanies()) {
        		edges.append("\"" + c.getName() + "\"" + Arborable.STOCKS_EDGES_CONFIG).append(",");
        	}
        	edges = edges.deleteCharAt(edges.length() - 1);
    	}
    	
    	edges.append("}");
    	return edges.toString();
    }
    
	/** 
     * @param company
     */
    public void addCompany(Company company) {
    	getCompanies().add(company);
    }

	public Long getId() {
		return id;
	}
    
    public String getName() {
		return name;
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
    public Set<Company> getCompanies() {
		return companies;
	}
    
    public void setCompanies(Set<Company> companies) {
		this.companies = companies;
	}

	@Override
	public String toString() {
		return "Stock [id=" + id + ", name=" + name + "]";
	}
    
}
