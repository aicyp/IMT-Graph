package Abstraction;

import Nodes.AbstractNode;

public abstract class AbstractMatrixGraph<A extends AbstractNode> implements IGraph {

    //--------------------------------------------------
    // 				Class variables
    //--------------------------------------------------

    protected int order;		// Number of vertices
    protected int m = 0;		// Number of edges/arcs
    protected int[][] matrix;	// The adjacency matrix

    //--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------

    public AbstractMatrixGraph() {
        this.matrix = new int[0][0];
        this.order = 0;
        this.m = 0;
    }

    // ------------------------------------------
    // 		Accessors
    // ------------------------------------------

    /**
     * Returns the list of nodes in the graph
     */
    public int[][] getMatrix() {
        return this.matrix;
    }

    /**
     * Returns the number of nodes in the graph (referred to as the order of the graph)
     */
    public int getNbNodes() {
        return this.order;
    }

}
