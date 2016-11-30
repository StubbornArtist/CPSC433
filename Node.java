package cpsc433;

import java.util.LinkedHashMap;

public class Node {
	
	public LinkedHashMap<String, Assignment> Assignments;
	public int score;
	
	
	public Node(LinkedHashMap<String, Assignment> assigns){
		this.Assignments = assigns;
		this.score = 1000;
	}

}
