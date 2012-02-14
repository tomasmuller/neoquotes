package com.herokuapp.neoquotes.models;

import java.util.Collection;

import org.neo4j.helpers.collection.IteratorUtil;

/**
 * The main application graph.
 */
public class Graph {

	public static final String STOCK_NODES_CONFIG = ":{\"color\":\"#61AE24\", \"shape\":\"rectangle\", \"alpha\":1}";

	public static final String COMPANY_NODES_CONFIG = ":{\"color\":\"#00A1CB\", \"shape\":\"rectangle\", \"alpha\":1}";
	
	public static final String SYMBOL_NODES_CONFIG = ":{\"color\":\"#F18D05\", \"shape\":\"rectangle\", \"alpha\":1, \"price\":\"" + Wildcard.PRICE + "\"}";

	public static final String STOCKS_EDGES_CONFIG = ":{\"length\":2.5,\"weight\":2}";
	
	public static final String COMPANY_EDGES_CONFIG = ":{\"length\":2.5,\"weight\":2}";
	
	/**
	 * All NeoQuotes data, in Arbor.js format.
	 * @return String
	 */
	public String toJson() {
		Collection<Stock> stocks = IteratorUtil.asCollection(new Stock().findAll());
		Collection<Company> companies = IteratorUtil.asCollection(new Company().findAll());
		Collection<Symbol> symbols = IteratorUtil.asCollection(new Symbol().findAll());

		// build nodes
		StringBuilder nodes = new StringBuilder();
		nodes.append(getNodes(stocks)).append(",");
		nodes.append(getNodes(companies)).append(",");
		nodes.append(getNodes(symbols));
		
		// build edges
		StringBuilder edges = new StringBuilder();
		edges.append(getEdges(stocks)).append(",");
		edges.append(getEdges(companies));
		
		// return in arbor.js format
		return "{\"nodes\":{" + nodes.toString() + "},\"edges\":{" + edges.toString() + "}}";
	}

	/**
	 * All nodes.
	 * @return String
	 */
	private String getNodes(Collection<? extends Arborable> nodes) {
    	StringBuilder data = new StringBuilder();   	
    	if (nodes != null && nodes.iterator().hasNext()) {
    		data = new StringBuilder();
	    	for (Arborable a : nodes) {
	    		data.append(a.nodeToArborJsJson());
	    		data.append(",");
	    	}
	    	// remove last ','
	    	data = data.deleteCharAt(data.length() - 1);
    	}
    	return data.toString();
	}

	/**
	 * All edges.
	 * @return
	 */
	private String getEdges(Collection<? extends Arborable> nodes) {
    	StringBuilder data = new StringBuilder();
    	if (nodes != null && nodes.iterator().hasNext()) {
    		data = new StringBuilder();
	    	for (Arborable a : nodes) {
	    		data.append(a.edgesToArborJsJson());
	    		data.append(",");
	    	}
	    	// remove last ','
	    	data = data.deleteCharAt(data.length() - 1);
    	}
    	return data.toString();
	}

}
