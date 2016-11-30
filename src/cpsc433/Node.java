package cpsc433;

import java.util.LinkedHashMap;

public class Node {
	
	public LinkedHashMap<String, String> StringAssignments;
	public LinkedHashMap<String, Assignment> Assignments;
	public int score;
	
	
	public Node(LinkedHashMap<String, Assignment> assigns){
		this.Assignments = assigns;
		this.score = 1000;
	}
	
	public void putStrings(LinkedHashMap<String, String> a){
		this.StringAssignments = a;
	}

}

