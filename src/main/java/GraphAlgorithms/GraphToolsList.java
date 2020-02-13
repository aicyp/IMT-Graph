package GraphAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.TreeMap;

import Abstraction.AbstractListGraph;
import AdjacencyList.DirectedGraph;
import AdjacencyList.DirectedValuedGraph;
import Collection.Pair;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class GraphToolsList extends GraphTools {

	private static int _DEBBUG = 0;

	private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt = 0;

	// --------------------------------------------------
	// Constructors
	// --------------------------------------------------

	public GraphToolsList() {
		super();
	}

	// ------------------------------------------
	// Accessors
	// ------------------------------------------

	// ------------------------------------------
	// Methods
	// ------------------------------------------

	// Calcule les sommets accessibles depuis s par une chaîne
	private static void explorerSommet(AbstractNode s, List<AbstractNode> a) {
		debut[s.getLabel()] = cpt;
		visite[s.getLabel()] = 1;
		cpt++;

		a.add(s);
		if (s instanceof DirectedNode) {
			for (DirectedNode t : ((DirectedNode) s).getSuccs().keySet()) {
				if (!a.contains(t)) {
					explorerSommet(t, a);
				}
			}
		} else {
			for (UndirectedNode t : ((UndirectedNode) s).getNeighbours().keySet()) {
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
		for (int s : nodes) {
			if (visite[s] < 1) {
				explorerSommet(graph.getNodes().get(s), atteint);
			}
		}

		return atteint;
	}

	// Calcule les composantes connexes du graphe
	public static List<AbstractNode> explorerGrapheLargeur(AbstractListGraph<AbstractNode> graph) {
		List<AbstractNode> nodes = new ArrayList<AbstractNode>();
		boolean mark[] = new boolean[graph.getNbNodes()];
		Queue<AbstractNode> toVisit = new LinkedList<AbstractNode>();

		for (AbstractNode v : graph.getNodes()) {
			mark[v.getLabel()] = false;
		}

		for (int i = 0; i < mark.length; i++) {
			if (!mark[i]) {
				AbstractNode s = graph.getNodes().get(i);
				nodes.add(graph.getNodes().get(i));
				mark[i] = true;
				toVisit.add(s);

				while (!toVisit.isEmpty()) {
					AbstractNode v = toVisit.poll();

					if (s instanceof DirectedNode) {
						for (DirectedNode w : ((DirectedNode) v).getSuccs().keySet()) {
							if (!mark[w.getLabel()]) {
								nodes.add(w);
								mark[w.getLabel()] = true;
								toVisit.add((AbstractNode) w);
							}
						}
					} else {
						for (UndirectedNode w : ((UndirectedNode) v).getNeighbours().keySet()) {
							if (!mark[w.getLabel()]) {
								nodes.add(w);
								mark[w.getLabel()] = true;
								toVisit.add((AbstractNode) w);
							}
						}
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
		int[] f = fin.clone();

		DirectedGraph<DirectedNode> graphInverse = (DirectedGraph<DirectedNode>) ((DirectedGraph) graph)
				.computeInverse();

		Arrays.sort(f);
		for (int i = 0; i < f.length; i++) {
			for (int j = 0; j < fin.length; j++) {
				if (f[i] == fin[j]) {
					f[i] = j;
					fin[j] = -1;
				}
			}
		}

		List<Integer> fSorted = new ArrayList<>();

		for (int i = 0; i < f.length; i++) {
			fSorted.add(f[i]);
		}

		Collections.reverse(fSorted);

		for (int i = 0; i < fSorted.size(); i++) {
			f[i] = fSorted.get(i);
		}

		return explorerGrapheProfondeur((AbstractListGraph) graphInverse, f);
	}
	
	// Calcule le chemin le plus court à partir de l'algorithme de Bellman
	public static Pair<int[], List<DirectedNode>> getCheminPlusCourtBellman(DirectedValuedGraph graph, DirectedNode s) {
		
		// init
		int n = graph.getNbNodes();
		int[] values = new int[n];
		List<DirectedNode> precedent = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			values[i] = Integer.MAX_VALUE;
			precedent.add(null);
		}
		values[s.getLabel()] = 0;
		precedent.set(s.getLabel(), s);
		
		List<DirectedNode> d = explorerGrapheLargeur((AbstractListGraph) graph);
		System.out.println(d.toString());
		Map<Integer, List<DirectedNode>> dMap = new HashMap<Integer, List<DirectedNode>>();
		
		for (int i = 0; i < d.size(); i++) {
			if (dMap.containsKey(d.get(i).getLabel())) {
				dMap.get(d.get(i).getLabel()).add(graph.getNodes().get(i));
			} else {
				List<DirectedNode> tmp = new ArrayList<DirectedNode>();
				tmp.add(graph.getNodes().get(i));
				dMap.put(d.get(i).getLabel(), tmp);
			}
		}
		for (int k = 0; k < n; k++) {
			if (dMap.containsKey(k)) {
				for (DirectedNode node : dMap.get(k)) {
					for (Entry<DirectedNode, Integer> entry : node.getSuccs().entrySet()) {
						if (entry.getValue() + values[node.getLabel()] < values[entry.getKey().getLabel()]) {
							values[entry.getKey().getLabel()] = entry.getValue() + values[node.getLabel()];
							precedent.set(entry.getKey().getLabel(), node);
						}
					}
				}
			}
		}
		
		DirectedNode node = graph.getNodes().get((n + s.getLabel()) % n);
		for (Entry<DirectedNode, Integer> entry : node.getSuccs().entrySet()) {
			if (entry.getValue() + values[node.getLabel()] < values[entry.getKey().getLabel()]) {
				System.out.println("/!\\ There is a negative cycle.");
			}
		}
		
		
		return new Pair<int[], List<DirectedNode>>(values, precedent);
	}
	
	/*public static Pair<int[], List<DirectedNode>> bellman(DirectedValuedGraph g, DirectedNode s) {

		int n = g.getNbNodes();
		int[] values = new int[n];
		List<DirectedNode> precedent = new ArrayList<>(n);

		// Initialisation
		for (int i = 0; i < n; i++) {
			values[i] = 999999999;
			precedent.add(null);
		}
		values[s.getLabel()] = 0;
		precedent.set(s.getLabel(), s);
		int[] distancesFromS = new GraphToolsList().explorerGrapheLargeur((AbstractListGraph) g, (AbstractNode) s);
		System.out.println(Arrays.toString(distancesFromS));
		Map<Integer, List<DirectedNode>> distMap = new HashMap<Integer, List<DirectedNode>>();

		for (int i = 0; i < distancesFromS.length; i++) {
			if (distMap.containsKey(distancesFromS[i])) {
				distMap.get(distancesFromS[i]).add(g.getNodes().get(i));
			} else {
				List<DirectedNode> tmp = new ArrayList<DirectedNode>();
				tmp.add(g.getNodes().get(i));
				distMap.put(distancesFromS[i], tmp);
			}
		}
		for (int k = 0; k < n; k++) {
			if (distMap.containsKey(k))
				for (DirectedNode node : distMap.get(k)) {
					for (Entry<DirectedNode, Integer> entry : node.getSuccs().entrySet()) {
						if (entry.getValue() + values[node.getLabel()] < values[entry.getKey().getLabel()]) {
							values[entry.getKey().getLabel()] = entry.getValue() + values[node.getLabel()];
							precedent.set(entry.getKey().getLabel(), node);
						}
					}
				}
		}

		DirectedNode node = g.getNodes().get((n + s.getLabel()) % n);
		for (Entry<DirectedNode, Integer> entry : node.getSuccs().entrySet()) {
			if (entry.getValue() + values[node.getLabel()] < values[entry.getKey().getLabel()]) {
				System.out.println("/!\\ There is a negative cycle.");
			}
		}

		return new Pair<int[], List<DirectedNode>>(values, precedent);
	}*/

	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		DirectedGraph<DirectedNode> al = new DirectedGraph<>(Matrix);
		System.out.println(al);
		
		int[][] matrixDeux = GraphTools.generateGraphData(10, 20, false, false, false, 100001);
        int[][] matrixDeuxValued = GraphTools.generateValuedGraphData(10, false, false, true, false, 100001);
        GraphTools.afficherMatrix(matrixDeux);
        GraphTools.afficherMatrix(matrixDeuxValued);
        DirectedValuedGraph avl = new DirectedValuedGraph(matrixDeuxValued);


		int[] nodes = new int[al.getNbNodes()];

		for (AbstractNode s : al.getNodes()) {
			nodes[s.getLabel()] = s.getLabel();
		}

		long startTimeProfondeur = System.nanoTime();
		System.out.println("Exploration du graphe en profondeur :");
		System.out.println(explorerGrapheProfondeur((AbstractListGraph) al, nodes));
		long durationProfondeur = System.nanoTime() - startTimeProfondeur;
		System.out.println("Durée d'exécution du parcours en profondeur : " + durationProfondeur + " ns\n");

		long startTimeLargeur = System.nanoTime();
		System.out.println("Exploration du graphe en largeur :");
		System.out.println(explorerGrapheLargeur((AbstractListGraph) al));
		long durationLargeur = System.nanoTime() - startTimeLargeur;
		System.out.println("Durée d'exécution du parcours en largeur : " + durationLargeur + " ns\n");

		System.out.println("Calcul des composantes fortement connexes :");
		System.out.println(getCompFortementConnexe((AbstractListGraph) al));
		
		System.out.println("Calcul chemin le plus court - bellman :");
		DirectedValuedGraph dg = new DirectedValuedGraph(
				GraphTools.generateValuedGraphData(15, false, false, true, false, 100001));

		System.out.println(dg.toString());
		Pair<int[], List<DirectedNode>> ret = getCheminPlusCourtBellman(dg, dg.getNodes().get(0));
		System.out.println(Arrays.toString(ret.getLeft()));
		System.out.println(ret.getRight());
	}
}
