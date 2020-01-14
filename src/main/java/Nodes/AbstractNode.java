package Nodes;


public abstract class AbstractNode {

	//--------------------------------------------------
	// 				Class variables
	//--------------------------------------------------

	private int label;
	private int weight;

	//--------------------------------------------------
	// 				Constructors
	//--------------------------------------------------

	public AbstractNode(int i) {
		this.label = i;
		this.weight = 0;
	}

	public AbstractNode(int i, int p) {
		this(i);
		this.weight = p;
	}

	// ------------------------------------------
	// 				Accessors
	// ------------------------------------------

	/**
	 * @return an integer which is a unique identifier of a node
	 */
	public int getLabel(){
		return this.label;
	}

	/**
	 * @return an integer which is a weight associated with a node
	 */
	public int getWeight(){
		return this.weight;
	}

	/**
	 * setter for the weight variable
	 * @param w the weight to set
	 */
	public void setWeight(int w){
		this.weight = w;
	}

	/**
	 * check if two nodes are equals => the label is the key
	 * @param n an object which is an abstract node
	 * @return true iff this and n are equal
	 */
	public boolean equals(Object n) {
		return n instanceof AbstractNode && ((AbstractNode) n).getLabel() == this.getLabel();
	}

	public String toString() {
		String s = "node-"+label;
		if(weight>0) {
			s += "-"+weight;
		}
		return s;
	}
}
