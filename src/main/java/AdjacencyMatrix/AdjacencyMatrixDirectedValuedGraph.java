package AdjacencyMatrix;

import GraphAlgorithms.GraphTools;
import Nodes.AbstractNode;
import Nodes.DirectedNode;

public class AdjacencyMatrixDirectedValuedGraph extends AdjacencyMatrixDirectedGraph {

	// --------------------------------------------------
	// Class variables
	// --------------------------------------------------

	private int[][] matrixCosts; // The graph with Costs

	// --------------------------------------------------
	// Constructors
	// --------------------------------------------------

	public AdjacencyMatrixDirectedValuedGraph(int[][] mat, int[][] matrixVal) {
		super();
		this.order = mat.length;
		this.matrix = new int[this.order][this.order];
		this.matrixCosts = new int[this.order][this.order];
		for (int i = 0; i < this.order; i++) {
			for (int j = 0; j < this.order; j++) {
				int val = mat[i][j];
				int cost = matrixVal[i][j];
				this.matrix[i][j] = val;
				this.matrixCosts[i][j] = cost;
				this.m += val;
			}
		}
	}

	// --------------------------------------------------
	// Accessors
	// --------------------------------------------------

	/**
	 * @return the matrix with costs of the graph
	 */
	public int[][] getMatrixCosts() {
		return matrixCosts;
	}

	// ------------------------------------------------
	// Methods
	// ------------------------------------------------

	/**
	 * removes the arc (from,to) if there exists at least one between these nodes in
	 * the graph. And if there remains no arc, removes the cost.
	 */
	@Override
	public void removeArc(AbstractNode from, AbstractNode to) {
		super.removeArc(from, to);
		if (!super.isArc(from, to)) {
			this.matrixCosts[from.getLabel()][to.getLabel()] = 0;
		}
	}

	/**
	 * adds the arc (from,to,cost), we allow the multi-graph. If there is already
	 * one initial cost, we keep it.
	 */
	public void addArc(AbstractNode from, AbstractNode to, int cost) {
		super.addArc(from, to);
		if (this.matrixCosts[from.getLabel()][to.getLabel()] == 0) {
			this.matrixCosts[from.getLabel()][to.getLabel()] = cost;
		}
	}

	public String toString() {
		StringBuilder s = new StringBuilder(super.toString() + "\n Matrix of Costs: \n");
		for (int[] matrixCost : this.matrixCosts) {
			for (int i : matrixCost) {
				s.append(i).append(" ");
			}
			s.append("\n");
		}
		s.append("\n");
		return s.toString();
	}

	public static void main(String[] args) {
		int[][] matrix = GraphTools.generateGraphData(10, 30, false, false, false, 100001);
		int[][] matrixValued = GraphTools.generateValuedGraphData(10, false, false, true, false, 100001);
		AdjacencyMatrixDirectedValuedGraph am = new AdjacencyMatrixDirectedValuedGraph(matrix, matrixValued);
		System.out.println(am);

		System.out.println("\n************\nTests\n");
		System.out
				.println("isArc() (expected : false) - result : " + am.isArc(new DirectedNode(0), new DirectedNode(1)));
		System.out.println(
				"isArc() [0][3] (expected : true) - result : " + am.isArc(new DirectedNode(0), new DirectedNode(3)));

		am.removeArc(new DirectedNode(0), new DirectedNode(3));
		System.out.println("isArc() after removed [0][3] (expected : false) - result : "
				+ am.isArc(new DirectedNode(0), new DirectedNode(3)));
		System.out.println("Cost (expected : 0) - result : " + am.getMatrixCosts()[0][3]);

		am.addArc(new DirectedNode(0), new DirectedNode(3), 26);
		System.out.println("isArc() after added [0][3] (expected : true) - result : "
				+ am.isArc(new DirectedNode(0), new DirectedNode(3)));
		System.out.println("Cost (expected : 26) - result : " + am.getMatrixCosts()[0][3]);

	}

	@Override
	public int[][] toAdjacencyMatrixCosts() {
		return this.matrixCosts;
	}
}
