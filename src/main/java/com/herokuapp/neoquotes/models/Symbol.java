package com.herokuapp.neoquotes.models;

import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.helpers.collection.ClosableIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.transaction.annotation.Transactional;

import com.herokuapp.neoquotes.repositories.SymbolRepository;

@NodeEntity
@Configurable
public class Symbol implements Arborable {
	
	private transient Logger log = Logger.getLogger(this.getClass());
	
    @Autowired
    private transient SymbolRepository symbolRepository;
    
    @GraphId
    private Long id;
    
    @Indexed(indexType = IndexType.FULLTEXT, indexName = "searchBySymbol")
    private String symbol;
    
    private String price;
    
    @RelatedTo(type = "LISTED_AS", direction = Direction.INCOMING)
    private Company company;

    @RelatedTo(type = "RELATED", direction = Direction.BOTH)
    private Set<Symbol> relatedSymbols;


    /**
     * Search for the requested symbol. If no symbol is found,
     * a new one is created with existing data in <code>YqlResult</code> param. 
     * @param yqlResult
     * @return String (json representation of created symbol)
     */
    public String search(YqlResult yqlResult) {
    	String message = StatusCode.SYMBOL_NOT_FOUND;
    	log.info(yqlResult);
    	log.info("symbolRepository = " + symbolRepository);

    	if (yqlResult.validate()) {
    		message = findOrCreate(yqlResult).toJson();
    	}

    	return message;
    }

    /**
     * Construct new symbol. 
     * @param yqlResult
     * @return Symbol
     */
    @Transactional
    public Symbol findOrCreate(YqlResult yqlResult) {
    	// check if symbol exists
    	Symbol searched = symbolRepository.findByPropertyValue("symbol", yqlResult.getSymbol());
    	log.info("Searching for: " + yqlResult.getSymbol() + ", found: " + searched);

    	if (searched == null) {
    		searched = new Symbol();
    		searched.setSymbol(yqlResult.getSymbol());
        	
        	Stock stock = new Stock().findOrCreate(yqlResult.getStockName());
        	Company company = new Company().findOrCreate(yqlResult.getCompanyName());

        	log.info("Stock: " + stock + ". StockCompanies: " + stock.getCompanies());
        	log.info("Company: " + company);
        	
        	stock.addCompany(company);
        	company.addListedIn(stock);
        	company.addSymbol(searched);
        	searched.setCompany(company);
    	}

    	// update the price
    	searched.setPrice(yqlResult.getPrice());
    	searched.persist();

    	return searched;
    }
    
    @Transactional
    public ClosableIterable<Symbol> findAll() {
    	return symbolRepository.findAll();
    }
    
    @Override
    public String nodeToArborJsJson() {
		return "\"" + getSymbol() + "\"" + Graph.SYMBOL_NODES_CONFIG.replace(Wildcard.PRICE, getPrice());
	}

    @Override
    public String edgesToArborJsJson() {
    	return "";
    }

    /**
     * Representation to Arbor.js, of this Symbol and related data.
     * @param symbol
     * @return String
     */
	public String toJson() {
		StringBuilder nodes = new StringBuilder();
		StringBuilder edges = new StringBuilder();

		// this symbol
		nodes.append(this.nodeToArborJsJson()).append(",");

		// company of this symbol
		nodes.append(getCompany().nodeToArborJsJson()).append(",");

		// edges of the company of this symbol
		edges.append(getCompany().edgesToArborJsJson()).append(",");

		// Stocks that this company is listed
		for (Stock stock : getCompany().getStocks()) {
			nodes.append(stock.nodeToArborJsJson()).append(",");

			// Other companies from this stock
			for (Company company : stock.getCompanies()) {
				nodes.append(company.nodeToArborJsJson()).append(",");
				
				// symbols of other companies
				nodes.append(company.allSymbolsAsNodesToJson()).append(",");
				edges.append(company.edgesToArborJsJson()).append(",");
			}

			edges.append(stock.edgesToArborJsJson()).append(",");
		}

		// remove last comma
		nodes = nodes.deleteCharAt(nodes.length() - 1);
		edges = edges.deleteCharAt(edges.length() - 1);

		// Symbol info to show to the user
		StringBuilder symbolInfo = new StringBuilder();
		symbolInfo.append("\"price\":\"" + getPrice() + "\",");
		symbolInfo.append("\"company\":\"" + getCompany().getName() + "\",");
		symbolInfo.append("\"symbol\":\"" + getSymbol() + "\",");

		return "{" + symbolInfo.toString() + "\"nodes\":{" + nodes.toString() + "},\"edges\":{" + edges.toString() + "}}";
	}
	
    /**
     * Add related symbols.
     * @param symbols
     */
    public void setRelatedTo(Symbol... symbols) {
    	for (Symbol s : symbols) {
    		getRelatedSymbols().add(s);
    	}
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getSymbol() {
		return symbol;
	}
    
    public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
    
    public String getPrice() {
		return price;
	}
    
    public void setPrice(String price) {
		this.price = price;
	}
    
    public Company getCompany() {
		return company;
	}
    
    public void setCompany(Company company) {
		this.company = company;
	}
    
    public Set<Symbol> getRelatedSymbols() {
		return relatedSymbols;
	}
    
    public void setRelatedSymbols(Set<Symbol> relatedSymbols) {
		this.relatedSymbols = relatedSymbols;
	}

	@Override
	public String toString() {
		return "Symbol [id=" + id + ", symbol=" + symbol + ", price=" + price
				+ ", company=" + company + ", relatedSymbols=" + relatedSymbols
				+ "]";
	}
    
}
