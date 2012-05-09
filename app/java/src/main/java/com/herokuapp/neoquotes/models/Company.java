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

import com.herokuapp.neoquotes.repositories.CompanyRepository;

@NodeEntity
@Configurable
public class Company implements Arborable {

    @Autowired
    private transient CompanyRepository companyRepository;
    
    @GraphId
    private Long id;
    
    @Indexed(indexType = IndexType.FULLTEXT, indexName = "searchByCompanyName")
    private String name;
    
    @RelatedTo(type = "LISTED_COMPANIES", direction = Direction.INCOMING)
    @Fetch private Set<Stock> stocks;
       
    @RelatedTo(type = "LISTED_AS", direction = Direction.OUTGOING)
    @Fetch private Set<Symbol> symbols;


    /**
     * Empty constructor.
     */
    public Company() {
    }

    /**
     * Construct company with name.
     * @param name
     */
    public Company(String name) {
        this.name = name;
    }

    /**
     * Find or create a new company, with the given name.
     * @param companyName
     * @return Company
     */
    @Transactional
    public Company findOrCreate(String companyName) {
        Company company = companyRepository.findByPropertyValue("name", companyName);
        if (company == null) {
            company = new Company(companyName);
            company.persist();
        }
        return company;
    }
    
    @Transactional
    public ClosableIterable<Company> findAll() {
        return companyRepository.findAll();
    }
    
    @Override
    public String nodeToArborJsJson() {
        return "\"" + getName() + "\"" + Arborable.COMPANY_NODES_CONFIG;
    }

    @Override
    public String edgesToArborJsJson() {
        StringBuilder edges = new StringBuilder();
        edges.append("\"" + getName() + "\":{");

        if (getSymbols() != null && !getSymbols().isEmpty()) {
            for (Symbol s : getSymbols()) {
                edges.append("\"" + s.getSymbol() + "\"" + Arborable.COMPANY_EDGES_CONFIG).append(",");
            }
            edges = edges.deleteCharAt(edges.length() - 1);
        }

        edges.append("}");
        return edges.toString();
    }

    /**
     * All symbols in Arbor.js json format.
     * @return String
     */
    public String allSymbolsAsNodesToJson() {
        StringBuilder nodes = new StringBuilder();
        for (Symbol symbol : getSymbols()) {
            nodes.append(symbol.nodeToArborJsJson());
        }
        return nodes.toString();
    }

    /** 
     * @param stock
     */
    public void addListedIn(Stock stock) {
        getStocks().add(stock);
    }
    
    /** 
     * @param symbols
     */
    public void addSymbol(Symbol... symbols) {
        for (Symbol s : symbols) {
            getSymbols().add(s);
        }
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
    
    public Set<Stock> getStocks() {
        return stocks;
    }
    
    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }
    
    public Set<Symbol> getSymbols() {
        return symbols;
    }
    
    public void setSymbols(Set<Symbol> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String toString() {
        return "Company [id=" + id + ", name=" + name + "]";
    }

}
