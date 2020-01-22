package GraphAlgorithms;


import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Abstraction.AbstractListGraph;
import AdjacencyList.DirectedGraph;
import AdjacencyList.DirectedValuedGraph;
import AdjacencyList.UndirectedValuedGraph;
import Collection.Triple;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class GraphToolsList  extends GraphTools {

	private static int _DEBBUG =0;

	private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt=0;

	//--------------------------------------------------
	// 				Constructors
	//--------------------------------------------------

	public GraphToolsList(){
		super();
	}

	// ------------------------------------------
	// 				Accessors
	// ------------------------------------------



	// ------------------------------------------
	// 				Methods
	// ------------------------------------------

	// Calcule les sommets accessibles depuis s par une chaîne
	private void explorerSommet(AbstractNode s, Set<AbstractNode> a) {
		a.add(s);
		if (s instanceof DirectedNode) {
			for (DirectedNode t: ((DirectedNode) s).getSuccs().keySet()) {
				if (!a.contains(t)) {
					explorerSommet(t, a);
				}
			}
		} else {
			for (UndirectedNode t: ((UndirectedNode) s).getNeighbours().keySet()) {
				if (!a.contains(t)) {
					explorerSommet(t, a);
				}
			}
		}
	}
	
	// Calcule les composantes connexes du graphe
	public void explorerGrapheProfondeur(AbstractListGraph<AbstractNode> graph) {
		Set<AbstractNode> atteint = new HashSet<AbstractNode>();
		for (AbstractNode s: graph.getNodes()) {
			if (!atteint.contains(s)) {
				this.explorerSommet(s, atteint);
			}
		}
	}
	
	public void explorerGrapheLaregur(AbstractListGraph<AbstractNode> graph, AbstractNode s) {
		boolean mark[] = new boolean[graph.getNbNodes()];
		
		for (AbstractNode v: graph.getNodes()) {
			mark[v.getLabel()] = false;
		}
		mark[s.getLabel()] = true;
		
		Queue<AbstractNode> toVisit = new PriorityQueue<AbstractNode>();
		toVisit.add(s);
		
		while (!toVisit.isEmpty()) {
			AbstractNode v = toVisit.poll();
			
			if (s instanceof DirectedNode) {
				for (DirectedNode w: ((DirectedNode) v).getSuccs().keySet()) {
					if (!mark[w.getLabel()]) {
						mark[w.getLabel()] = true;
						toVisit.add(w);
					}
				}
			} else {
				for (UndirectedNode w: ((UndirectedNode) v).getNeighbours().keySet()) {
					if (!mark[w.getLabel()]) {
						mark[w.getLabel()] = true;
						toVisit.add(w);
					}
				}				
			}
		}
	}


	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		DirectedGraph<DirectedNode> al = new DirectedGraph<>(Matrix);
		System.out.println(al);

		// A completer
	}
}
