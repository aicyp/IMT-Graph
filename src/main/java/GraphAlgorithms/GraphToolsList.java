package GraphAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Abstraction.AbstractListGraph;
import AdjacencyList.DirectedGraph;
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

	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		DirectedGraph<DirectedNode> al = new DirectedGraph<>(Matrix);
		System.out.println(al);

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
	}
}
