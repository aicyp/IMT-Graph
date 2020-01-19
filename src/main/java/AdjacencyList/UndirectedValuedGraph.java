package AdjacencyList;

import java.util.ArrayList;

import GraphAlgorithms.GraphTools;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class UndirectedValuedGraph extends UndirectedGraph<UndirectedNode> {

	//--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------

    public UndirectedValuedGraph(int[][] matrixVal) {
    	super();
    	this.order = matrixVal.length;
        this.nodes = new ArrayList<>();
        for (int i = 0; i < this.order; i++) {
            this.nodes.add(i, this.makeNode(i));
        }
        for (UndirectedNode n : this.getNodes()) {
            for (int j = n.getLabel(); j < matrixVal[n.getLabel()].length; j++) {
            	UndirectedNode nn = this.getNodes().get(j);
                if (matrixVal[n.getLabel()][j] != 0) {
                    n.getNeighbours().put(nn,matrixVal[n.getLabel()][j]);
                    nn.getNeighbours().put(n,matrixVal[n.getLabel()][j]);
                    this.m++;
                }
            }
        }
    }

    //--------------------------------------------------
    // 				Methods
    //--------------------------------------------------
    

    /**
     * Adds the edge (from,to) with cost if it is not already present in the graph
     */
    public void addEdge(UndirectedNode x, UndirectedNode y, int cost) {
    	if (!this.isEdge(x, y)) {
    		this.getNodeOfList(x).addNeigh(this.getNodeOfList(y), cost);
    		this.getNodeOfList(y).addNeigh(this.getNodeOfList(x), cost);
    	}
    	
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (UndirectedNode n : nodes) {
            s.append("neighbours of ").append(n).append(" : ");
            for (UndirectedNode sn : n.getNeighbours().keySet()) {
                s.append("(").append(sn).append(",").append(n.getNeighbours().get(sn)).append(")  ");
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
    
    public static void main(String[] args) {
        int[][] matrix = GraphTools.generateGraphData(10, 15, false, true, false, 100001);
        int[][] matrixValued = GraphTools.generateValuedGraphData(10, false, true, true, false, 100001);
        GraphTools.afficherMatrix(matrix);
        GraphTools.afficherMatrix(matrixValued);
        UndirectedValuedGraph al = new UndirectedValuedGraph(matrixValued);
        System.out.println(al);
        
        System.out.println("\n************\nTests\n");
		System.out.println("isEdge() [0][3] (expected : false) - result : " + al.isEdge(new UndirectedNode(0), new UndirectedNode(3)));
		System.out.println("isEdge() [0][2] (expected : true) - result : " + al.isEdge(new UndirectedNode(0), new UndirectedNode(2)));
		
		al.addEdge(new UndirectedNode(0), new UndirectedNode(3), 36);
		System.out.println("isEdge() after added [0][3] (expected : true) - result : "
				+ al.isEdge(new UndirectedNode(0), new UndirectedNode(3)));
    }
}
