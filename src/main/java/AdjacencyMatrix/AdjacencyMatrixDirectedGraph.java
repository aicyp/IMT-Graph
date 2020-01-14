package AdjacencyMatrix;

import Abstraction.AbstractMatrixGraph;
import GraphAlgorithms.GraphTools;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;
import Abstraction.IDirectedGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the directed graphs structured by an adjacency matrix.
 * It is possible to have simple and multiple graph
 */
public class AdjacencyMatrixDirectedGraph extends AbstractMatrixGraph<DirectedNode> implements IDirectedGraph {

	//--------------------------------------------------
	// 				Constructors
	//-------------------------------------------------- 

	public AdjacencyMatrixDirectedGraph() {
		super();
	}

	public AdjacencyMatrixDirectedGraph(int[][] M) {
		this.order = M.length;
		this.matrix = new int[this.order][this.order];
		for(int i = 0; i<this.order; i++){
			for(int j = 0; j<this.order; j++){
				this.matrix[i][j] = M[i][j];
				this.m += M[i][j];
			}
		}
	}

	public AdjacencyMatrixDirectedGraph(IDirectedGraph<DirectedNode> g) {
		this.order = g.getNbNodes();
		this.m = g.getNbArcs();
		this.matrix = g.toAdjacencyMatrix();
	}

	//--------------------------------------------------
	// 					Accessors
	//--------------------------------------------------

	@Override
	public int getNbArcs() {
		return this.m;
	}

	public List<Integer> getSuccessors(AbstractNode x) {
		List<Integer> v = new ArrayList<Integer>();
		for(int i =0;i<this.matrix[x.getLabel()].length;i++){
			if(this.matrix[x.getLabel()][i]>0){
				v.add(i);
			}
		}
		return v;
	}

	public List<Integer> getPredecessors(AbstractNode x) {
		List<Integer> v = new ArrayList<Integer>();
		for(int i =0;i<this.matrix.length;i++){
			if(this.matrix[i][x.getLabel()]>0){
				v.add(i);
			}
		}
		return v;
	}
	
	
	// ------------------------------------------------
	// 					Methods 
	// ------------------------------------------------		
	
	@Override
	public boolean isArc(AbstractNode from, AbstractNode to) {
		return this.matrix[from.getLabel()][to.getLabel()] > 0;
	}
	
	public int getNumberOfArcs(AbstractNode from, AbstractNode to) {
		return this.matrix[from.getLabel()][to.getLabel()];
	}

	/**
	 * removes the arc (from,to) if there exists at least one between these nodes in the graph.
	 */
	@Override
	public void removeArc(AbstractNode from, AbstractNode to) {
		if (isArc(from, to)) {
			this.matrix[from.getLabel()][to.getLabel()]--;
		}
	}

	/**
	 * Adds the arc (from,to). we allow multiple graph.
	 */
	@Override
	public void addArc(AbstractNode from, AbstractNode to) {
		this.matrix[from.getLabel()][to.getLabel()]++;
	}


	/**
	 * @return the adjacency matrix representation int[][] of the graph
	 */
	public int[][] toAdjacencyMatrix() {
		return this.matrix;
	}

	@Override
	public IDirectedGraph<DirectedNode> computeInverse() {
		AdjacencyMatrixDirectedGraph ma = new AdjacencyMatrixDirectedGraph(this.matrix);
		for(int i = 1; i<this.order; i++){
			for(int j = i+1; j<this.order; j++){
				int tmp = ma.matrix[i][j];
				ma.matrix[i][j] = ma.matrix[j][i];
				ma.matrix[j][i] = tmp;
			}
		}
		return ma;
	}

	@Override
	public String toString(){
		StringBuilder s = new StringBuilder("Adjacency Matrix: \n");
		for (int[] ints : matrix) {
			for (int anInt : ints) {
				s.append(anInt).append(" ");
			}
			s.append("\n");
		}
		s.append("\n");
		return s.toString();
	}

	public static void main(String[] args) {
		int[][] matrix2 = GraphTools.generateGraphData(10, 20, false, false, false, 100001);
		AdjacencyMatrixDirectedGraph am = new AdjacencyMatrixDirectedGraph(matrix2);
		System.out.println(am);
		List<Integer> t = am.getSuccessors(new DirectedNode(1));
		for (Integer integer : t) {
			System.out.print(integer + ", ");
		}
		System.out.println();
		List<Integer> t2 = am.getPredecessors(new DirectedNode(2));
		for (Integer integer : t2) {
			System.out.print(integer + ", ");
		}
		System.out.println("\n************\nTests\n");
		System.out.println("isArc() (expected : false) - result : " + am.isArc(new UndirectedNode(0), new UndirectedNode(1)));
		System.out.println("isArc() (expected : true) - result : " + am.isArc(new UndirectedNode(0), new UndirectedNode(3)));
		
		System.out.println("Number of edges on [3][2] : " + am.getNumberOfArcs(new UndirectedNode(3), new UndirectedNode(2)));
		am.addArc(new UndirectedNode(3), new UndirectedNode(2));
		am.addArc(new UndirectedNode(3), new UndirectedNode(2));
		System.out.println("Number of edges on [3][2] (expected : 3) - result : " + am.getNumberOfArcs(new UndirectedNode(3), new UndirectedNode(2)));
		am.removeArc(new UndirectedNode(3), new UndirectedNode(2));
		System.out.println("Number of edges on [3][2] (expected : 2) - result : " + am.getNumberOfArcs(new UndirectedNode(3), new UndirectedNode(2)));
		
		System.out.println(am.toString());
		System.out.println("After inversion :" + am.computeInverse());
	}
}
