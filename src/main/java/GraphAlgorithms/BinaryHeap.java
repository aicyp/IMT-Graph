package GraphAlgorithms;

public class BinaryHeap {

	private int[] nodes;
	private int pos;

	public BinaryHeap() {
		this.nodes = new int[32];
		for (int i = 0; i < nodes.length; i++) {
			this.nodes[i] = Integer.MAX_VALUE;
		}
		this.pos = 0;
	}

	public void resize() {
		int[] tab = new int[this.nodes.length + 32];
		for (int i = 0; i < nodes.length; i++) {
			tab[i] = Integer.MAX_VALUE;
		}
		System.arraycopy(this.nodes, 0, tab, 0, this.nodes.length);
		this.nodes = tab;
	}

	public boolean isEmpty() {
		return pos == 0;
	}

	public void insert(int element) {
		if (pos >= this.nodes.length) {
			this.resize();
		}
		if (this.nodes[pos] == Integer.MAX_VALUE) {
			this.nodes[pos] = element;
			int child = pos;
			int father = (child - 1) / 2;
			while (child >= 0 && element < this.nodes[father]) {
				this.swap(father, child);
				child = father;
				father = (child - 1) / 2;
			}
			pos++;
		}
	}

	public int remove() {
		this.swap(0, pos - 1);
		pos--;
		int val = this.nodes[pos];
		this.nodes[pos] = Integer.MAX_VALUE;
		int temp = 0;
		int childPos = this.getBestChildPos(temp);
		while (childPos != Integer.MAX_VALUE && this.nodes[temp] > this.nodes[childPos]) {
			this.swap(temp, childPos);
			temp = childPos;
			childPos = this.getBestChildPos(temp);
		}
		return val;
	}

	private int getBestChildPos(int src) {
		if (isLeaf(src)) { // the leaf is a stopping case, then we return a default value
			return Integer.MAX_VALUE;
		} else {
			return this.nodes[2 * src + 1] <= this.nodes[2 * src + 2] ? 2 * src + 1 : 2 * src + 2;
		}
	}

	/**
	 * Test if the node is a leaf in the binary heap
	 * 
	 * @returns true if it's a leaf or false else
	 * 
	 */
	private boolean isLeaf(int src) {
		boolean ret = false;
		if ((2 * src + 1 >= this.nodes.length || this.nodes[2 * src + 1] == Integer.MAX_VALUE)
				&& (2 * src + 2 >= this.nodes.length || this.nodes[2 * src + 2] == Integer.MAX_VALUE)) {
			ret = true;
		}
		return ret;
	}

	private void swap(int father, int child) {
		int temp = nodes[father];
		nodes[father] = nodes[child];
		nodes[child] = temp;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < pos; i++) {
			s.append("Index : ").append(i).append(" Val : ").append(nodes[i]).append(" Childs : ").append(2 * i + 1)
					.append(" - ").append(2 * i + 2).append(", \n");
		}
		return s.toString();
	}

	/**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @returns a boolean equal to True if the binary tree is compact from left to
	 *          right
	 * 
	 */
	public boolean test() {
		return this.isEmpty() || testRec(0);
	}

	private boolean testRec(int root) {
		if (isLeaf(root)) {
			return true;
		} else {
			int left = 2 * root + 1;
			int right = 2 * root + 2;
			if (right >= pos) {
				return nodes[left] >= nodes[root] && testRec(left);
			} else {
				return nodes[left] >= nodes[root] && testRec(left) && nodes[right] >= nodes[root] && testRec(right);
			}
		}
	}

	public static void main(String[] args) {
		BinaryHeap jarjarBin = new BinaryHeap();
		System.out.println(jarjarBin.isEmpty() + "\n");
		int k = 20;
		int m = k;
		int min = 2;
		int max = 20;
		while (k > 0) {
			int rand = min + (int) (Math.random() * ((max - min) + 1));
			System.out.print("insert " + rand);
			jarjarBin.insert(rand);
			k--;
		}
		System.out.println("\n" + jarjarBin);
		System.out.println(jarjarBin.test());

		System.out.println("\nRemove val : " + jarjarBin.remove());
		System.out.println("\n" + jarjarBin);
		System.out.println(jarjarBin.test());

	}

}
