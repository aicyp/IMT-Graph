package Abstraction;

public interface IGraph {
	/**
	 * @return the number of nodes in the graph (referred to as the order of the graph)
 	 */
	int getNbNodes();

	/**
	 * @return the adjacency matrix representation int[][] of the graph
 	 */
	int[][] toAdjacencyMatrix();

}
