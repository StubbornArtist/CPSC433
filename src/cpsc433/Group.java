package cpsc433;

import java.util.HashSet;

public class Group{
	private String name;	// Name of the group
	private String head;	// Name of the person who heads the group
	private HashSet<String> members;	// A HashSet of all the members of the group
	
	public Group(String name){	// Constructor
		this.name = name;
		head = null;
		members = new HashSet<String>();
	}
	
	public String getName(){	// Returns the name of the group
		return name;
	}
	
	public void setHead(String person){	// Sets the head of the group
		head = person;
	}
	
	public boolean isHead(String person){	// A method that checks if the given person heads the group
		return head.equals(person);
	}
	
	public void addMember(String person){	// Adds a person to the HashSet. If they already exist, they are not added to 
		members.add(person);		// the set
	}
	
	public boolean hasMember(String person){	// Evaluates if the given person is apart of the 'members' HashSet
		return members.contains(person);
	}
}
