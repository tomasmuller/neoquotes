package com.herokuapp.neoquotes.models;


/**
 * Nice interface name huh? : )
 * 
 * Define two methods for Node Entities that can
 * be exposed to Arbor.js.
 */
public interface Arborable {

	public static final String STOCK_NODES_CONFIG = ":{\"color\":\"#61AE24\", \"shape\":\"rectangle\", \"alpha\":1}";

	public static final String COMPANY_NODES_CONFIG = ":{\"color\":\"#00A1CB\", \"shape\":\"rectangle\", \"alpha\":1}";
	
	public static final String SYMBOL_NODES_CONFIG = ":{\"color\":\"#F18D05\", \"shape\":\"rectangle\", \"alpha\":1}";

	public static final String STOCKS_EDGES_CONFIG = ":{\"length\":2.5,\"weight\":2}";
	
	public static final String COMPANY_EDGES_CONFIG = ":{\"length\":2.5,\"weight\":2}";
	
    /**
     * Representation of nodes for Arbor.js library.
     * @return String
     */
	String nodeToArborJsJson();
	
	/**
	 * Representation of edges for Arbor.js library.
	 * @return String
	 */
	String edgesToArborJsJson();

}
