package cpsc433;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;


public class Node {
	
	public LinkedHashMap<String, String> StringAssignments;
	public LinkedHashMap<String, Assignment> Assignments;
	private ArrayList<String> roomKeys;
	public int score;
	
	
	public Node(LinkedHashMap<String, Assignment> assigns){
		this.Assignments = assigns;
		this.score = 1000;
		this.roomKeys = new ArrayList<String> (Assignments.keySet());
	}
	
	public void putStrings(LinkedHashMap<String, String> a){
		this.StringAssignments = a;
	}
	
	public void mutate(){
		Random rand = new Random();
		
		String room = roomKeys.get(rand.nextInt(roomKeys.size()));
		String room2 = roomKeys.get(rand.nextInt(roomKeys.size()));
		Person person = Assignments.get(room).randomPerson();
		
		Assignments.get(room2).addPerson(person);
		StringAssignments.put(person.name, room2);
			
	}
	
	@Override
	public String toString(){
		Iterator<Assignment> it = Assignments.values().iterator();
		String node = "";
		while(it.hasNext()){
			node += it.next().toString();
			if(it.hasNext()) node+=",";
		}
		return node;
	}
	
	public String AltToString(){
		Iterator<String> it = StringAssignments.keySet().iterator();
		String node = "";
		while(it.hasNext()){
			String person = it.next();
			node+= "(" + person +"," + StringAssignments.get(person) + ")";
			if(it.hasNext()) node+= ",";
		}
		
		return node;
	}

}

