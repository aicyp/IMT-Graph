package GraphAlgorithms;


import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Abstraction.AbstractListGraph;
import Abstraction.IDirectedGraph;
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
	private static void explorerSommet(AbstractNode s, List<AbstractNode> a) {
		debut[s.getLabel()] = cpt;
		visite[s.getLabel()] = 1;
		cpt++;
		
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
		fin[s.getLabel()] = cpt;
		visite[s.getLabel()] = 2;
	}
	
	// Calcule les composantes connexes du graphe
	public static List<AbstractNode> explorerGrapheProfondeur(AbstractListGraph<AbstractNode> graph, int[] nodes) {
		debut = new int[graph.getNbNodes()];
		visite = new int[graph.getNbNodes()];
		fin = new int[graph.getNbNodes()];
		cpt = 0;
		
		List<AbstractNode> atteint = new ArrayList<AbstractNode>();
		
		if (graph.getNodes().get(0) instanceof DirectedNode) {
			for (int s: nodes) {
				if (visite[s] < 1) {
					explorerSommet(graph.getNodes().get(s), atteint);
				}
			}
		} else {
			for (int s: nodes) {
				if (visite[s] < 1) {
					explorerSommet(graph.getNodes().get(s), atteint);
				}
			}
		}
		
		
		return atteint;
	}
	
	// Calcule les composantes connexes du graphe
	public static List<AbstractNode> explorerGrapheLargeur(AbstractListGraph<AbstractNode> graph, AbstractNode s) {
		List<AbstractNode> nodes = new ArrayList<AbstractNode>();
		boolean mark[] = new boolean[graph.getNbNodes()];
		Queue<AbstractNode> toVisit = new PriorityQueue<AbstractNode>();
		
		for (AbstractNode v: graph.getNodes()) {
			mark[v.getLabel()] = false;
		}
		
		nodes.add(s);
		mark[s.getLabel()] = true;
		toVisit.add(s);
		
		while (!toVisit.isEmpty()) {
			AbstractNode v = toVisit.poll();
			
			if (s instanceof DirectedNode) {
				for (DirectedNode w: ((DirectedNode) v).getSuccs().keySet()) {
					if (!mark[w.getLabel()]) {
						nodes.add(w);
						mark[w.getLabel()] = true;
						toVisit.add(w);
					}
				}
			} else {
				for (UndirectedNode w: ((UndirectedNode) v).getNeighbours().keySet()) {
					if (!mark[w.getLabel()]) {
						nodes.add(w);
						mark[w.getLabel()] = true;
						toVisit.add(w);
					}
				}				
			}
		}
		
		return nodes;
	}
	
	// Calcule les composantes fortement connexes d'un graphe
	public static List<AbstractNode> getCompFortementConnexe(AbstractListGraph<AbstractNode> graph) {
		assert graph instanceof DirectedGraph;
		int[] nodes = new int[graph.getNbNodes()];
	
		for (AbstractNode s : graph.getNodes()) {
			nodes[s.getLabel()] = s.getLabel();
		}
		explorerGrapheProfondeur(graph, nodes);
		int[] f = fin;
		
		DirectedGraph<DirectedNode> graphInverse = (DirectedGraph<DirectedNode>) ((DirectedGraph) graph).computeInverse();
		
		SortedMap<Integer, Integer> fList = new TreeMap<>();
		for (int i = 0; i < f.length; i++) {
			fList.put(f[i], i);
		}
		List<Integer> fSorted = (List<Integer>) fList.values();
		Collections.reverse(fSorted);

		int[] nodesToVisit = new int[fSorted.size()];
		for (int i = 0 ; i < fSorted.size() ; i++) {
			nodesToVisit[i] = fSorted.get(i);
		}		
		
		return explorerGrapheProfondeur((AbstractListGraph) graphInverse, nodesToVisit);
	}

	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		DirectedGraph<DirectedNode> al = new DirectedGraph<>(Matrix);
		System.out.println(al);

		long startTime;
		
		System.out.println(getCompFortementConnexe((AbstractListGraph) al));
	}
}
