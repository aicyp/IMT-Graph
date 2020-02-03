package AdjacencyList;

import java.util.ArrayList;
import java.util.Map;

import Abstraction.AbstractListGraph;
import GraphAlgorithms.GraphTools;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;
import Abstraction.IDirectedGraph;

public class DirectedGraph<A extends DirectedNode> extends AbstractListGraph<A> implements IDirectedGraph<A> {

	private static int _DEBBUG =0;
		
    //--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------

	public DirectedGraph(){
		super();
		this.nodes = new ArrayList<A>();
	}

    public DirectedGraph(int[][] matrix) {
        this.order = matrix.length;
        this.nodes = new ArrayList<A>();
        for (int i = 0; i < this.order; i++) {
            this.nodes.add(i, this.makeNode(i));
        }
        for (A n : this.getNodes()) {
            for (int j = 0; j < matrix[n.getLabel()].length; j++) {
                A nn = this.getNodes().get(j);
                if (matrix[n.getLabel()][j] != 0) {
                    n.getSuccs().put(nn,0);
                    nn.getPreds().put(n,0);
                    this.m++;
                }
            }
        }
    }

    public DirectedGraph(DirectedGraph<A> g) {
        super();
        this.nodes = new ArrayList<>();
        this.order = g.getNbNodes();
        this.m = g.getNbArcs();
        for(A n : g.getNodes()) {
            this.nodes.add(makeNode(n.getLabel()));
        }
        for (A n : g.getNodes()) {
            A nn = this.getNodes().get(n.getLabel());
            for (DirectedNode sn : n.getSuccs().keySet()) {
                DirectedNode snn = this.getNodes().get(sn.getLabel());
                nn.getSuccs().put(snn,0);
                snn.getPreds().put(nn,0);
            }
        }

    }

    // ------------------------------------------
    // 				Accessors
    // ------------------------------------------

    @Override
    public int getNbArcs() {
        return this.m;
    }

    @Override
    public boolean isArc(A from, A to) {
    	return this.getNodeOfList(from).getSuccs().containsKey(this.getNodeOfList(to)) 
    			&& this.getNodeOfList(to).getPreds().containsKey(this.getNodeOfList(from));
    }

    @Override
    public void removeArc(A from, A to) {
    	if (isArc(from, to)) {
    		this.getNodeOfList(from).getSuccs().remove(this.getNodeOfList(to));
    		this.getNodeOfList(to).getPreds().remove(this.getNodeOfList(from));
    	}
    }

    @Override
    public void addArc(A from, A to) {
    	if (!isArc(from, to)) {
    		this.getNodeOfList(from).addSucc(this.getNodeOfList(to), this.getNodeOfList(to).getLabel());
        	this.getNodeOfList(to).addPred(this.getNodeOfList(from), this.getNodeOfList(from).getLabel());
    	}
    }

    //--------------------------------------------------
    // 				Methods
    //--------------------------------------------------

    /**
     * Method to generify node creation
     * @param label of a node
     * @return a node typed by A extends DirectedNode
     */
    @Override
    public A makeNode(int label) {
        return (A)new DirectedNode(label);
    }

    /**
     * @return the corresponding nodes in the list this.nodes
     */
    public A getNodeOfList(A src) {
        return this.getNodes().get(src.getLabel());
    }

    /**
     * @return the adjacency matrix representation int[][] of the graph
     */
    @Override
    public int[][] toAdjacencyMatrix() {
        int[][] matrix = new int[order][order];
        for (int i = 0; i < order; i++) {
            for (DirectedNode j : nodes.get(i).getSuccs().keySet()) {
                int IndSucc = j.getLabel();
                matrix[i][IndSucc] = 1;
            }
        }
        return matrix;
    }

    @Override
    public IDirectedGraph computeInverse() {
        DirectedGraph<A> g = new DirectedGraph<>(this);
        int[][] matrix = g.toAdjacencyMatrix();
        for(int i = 0; i<this.order; i++){
        	for (DirectedNode j : nodes.get(i).getSuccs().keySet()) {
            	if (isArc(getNodeOfList((A) new DirectedNode(i)), getNodeOfList((A) j))) {
            		g.addArc(g.getNodeOfList((A) j), g.getNodeOfList((A) new DirectedNode(i)));
            	}
            }
		}
        g = new DirectedGraph<>(matrix);
        return g;
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(DirectedNode n : nodes){
            s.append("successors of ").append(n).append(" : ");
            for(DirectedNode sn : n.getSuccs().keySet()){
                s.append(sn).append(" ");
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, false, 100001);
        GraphTools.afficherMatrix(Matrix);
        DirectedGraph al = new DirectedGraph(Matrix);
        System.out.println(al);

        System.out.println("\n************\nTests\n");
		System.out.println("isArc() (expected : false) - result : " + al.isArc(new DirectedNode(0), new DirectedNode(1)));
		System.out.println("isArc() [0][3] (expected : true) - result : " + al.isArc(new DirectedNode(0), new DirectedNode(3)));
		
		al.removeArc(new DirectedNode(0), new DirectedNode(3));
		System.out.println("isArc() after removed [0][3] (expected : false) - result : "
				+ al.isArc(new DirectedNode(0), new DirectedNode(3)));
		
		al.addArc(new DirectedNode(0), new DirectedNode(3));
		System.out.println("isArc() after added [0][3] (expected : true) - result : "
				+ al.isArc(new DirectedNode(0), new DirectedNode(3)));
		
		System.out.println(al.toString());
		System.out.println("After inversion :\n" + al.computeInverse());
    }
}
