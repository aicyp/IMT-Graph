package Abstraction;

import Nodes.AbstractNode;

public interface IDirectedGraph<A extends AbstractNode> extends IGraph {

	/**
	 * @return the number of arcs in the graph
 	 */
	int getNbArcs();

	/**
	 * @return true if arc (from,to) exists in the graph
 	 */
	boolean isArc(A from, A to);

	/**
	 * Removes the arc (from,to), if it exists
 	 */
	void removeArc(A from, A to);

	/**
	 * Adds the arc (from,to) if it is not already present in the graph, requires the existing of nodes from and to 
 	 */
	void addArc(A from, A to);

	/**
	 * @return a new graph implementing IDirectedGraph interface which is the inverse graph of this
 	 */
	IDirectedGraph computeInverse();
}
