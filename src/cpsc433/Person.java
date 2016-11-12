package cpsc433;

import java.util.ArrayList;

public class Person {
	private String name;
	public boolean smokes;
	public boolean hacks;
	private ArrayList<String> roles;
	
	public Person(String name){
		this.name = name;
		roles = new ArrayList<String>();
	}
	
	public String getName(){
		return name;
	}

	public void addRole(String role){
		if(!roles.contains(role)){
			roles.add(role);
		}
	}
	
	public boolean hasRole(String role){	// This function returns true if this person has the role specified by the argument
		if(roles.contains(role)){	// given. 'role' should be one of "secretary", "manager", or "researcher".
			return true;
		}
		else{
			return false;
		}
	}
}
