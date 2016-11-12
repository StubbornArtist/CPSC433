package cpsc433;

import java.util.ArrayList;
import java.util.HashSet;

public class Person {
	private String name;
	public boolean smokes;
	public boolean hacks;
	private ArrayList<String> roles;
	private HashSet<String> coWorkers;
	
	public Person(String name){
		this.name = name;
		roles = new ArrayList<String>();
		coWorkers = new HashSet<String>();
	}
	
	public String getName(){
		return name;
	}

	public void addRole(String role){
		if(!roles.contains(role)){
			roles.add(role);
		}
	}
	
	public boolean hasRole(String role){	// This method returns true if this person has the role specified by the argument
		if(roles.contains(role)){	// given. 'role' should be one of "secretary", "manager", or "researcher".
			return true;
		}
		else{
			return false;
		}
	}
	
	// This method accepts a person 'p' and adds them to the 'coWorker' Set via the person's name. Since this is a HashSet, it
	// will only add the person's name if it is not already in the set.
	public void addCoWorker(String p){
		coWorkers.add(p);
	}
	
	public boolean hasCoWorker(String p){
		return coWorkers.contains(p);
	}
}
