package Abstraction;

import Nodes.AbstractNode;

public interface IUndirectedGraph<A extends AbstractNode> extends IGraph {

	/**
	 * @return the number of edges in the graph
 	 */
	int getNbEdges();

	/**
	 * @return true if there is an edge between x and y
	 */
	boolean isEdge(A x, A y);

	/**
	 * Removes edge (x,y) if there exists one
     */
	void removeEdge(A x, A y);

	/**
	 * Adds edge (x,y), requires that nodes x and y already exist
     */
	void addEdge(A x, A y);

}
