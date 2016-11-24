package cpsc433;

import java.util.HashSet;
import java.util.LinkedHashMap;

public class Generation {
	
	public int genNumber;
	public HashSet<LinkedHashMap<String, Assignment>> facts;

	
	public Generation(int number){
		this.genNumber = number;
		this.facts = new HashSet<LinkedHashMap<String, Assignment>>();
	}
	
	public void addFact(LinkedHashMap<String, Assignment> a){
		this.facts.add(a);
	}
}
