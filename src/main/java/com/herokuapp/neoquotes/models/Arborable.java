package com.herokuapp.neoquotes.models;


/**
 * Nice interface name huh? : )
 * 
 * Define two methods for Node Entities that can
 * be exposed to Arbor.js.
 */
public interface Arborable {

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
