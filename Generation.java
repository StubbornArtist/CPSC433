package cpsc433;

import java.util.HashSet;
import java.util.LinkedHashMap;

public class Generation {
	
	public int genNumber;
	public HashSet<Node> facts;

	
	public Generation(int number){
		this.genNumber = number;
		this.facts = new HashSet<Node>();
	}
	
	public void addFact(LinkedHashMap<String, Assignment> a){
		Node nodeN = new Node(a);
		this.facts.add(nodeN);
	}
}
