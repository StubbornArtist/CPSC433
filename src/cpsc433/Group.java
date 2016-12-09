package cpsc433;

import java.util.HashSet;
import java.util.Iterator;

public class Group{
	private String name;	// Name of the group
	private HashSet<String> heads;	// Name of the person who heads the group
	private HashSet<String> members;	// A HashSet of all the members of the group
	
	public Group(String name){	// Constructor
		this.name = name;
		heads = new HashSet<String>();
		members = new HashSet<String>();
	}
	/**
	 * Gets the name of the group
	 * @return
	 */
	public String getName(){
		return name;
	}
	/**
	 * Add a head to this group
	 * @param person
	 * 				name of the new group head
	 */
	public void addHead(String person){	
		heads.add(person);
	}
	/**
	 * Get the iterator for the group heads
	 * @return
	 */
	public java.util.Iterator<String> getHeadIterator(){
		return heads.iterator();
	}
	/**
	 * Check if a given person heads this group
	 * @param person
	 * 			The person that may be a group head
	 * @return
	 */
	public boolean isHead(String person){
		return heads.contains(person);
	}
	/**
	 * Add a member to this group
	 * @param person
	 * 		The new member of the group		
	 */
	public void addMember(String person){	
		members.add(person);
	}
	/**
	 * Check if a person is a member of this group
	 * @param person
	 * 			The person
	 * @return
	 */
	public boolean hasMember(String person){
		return members.contains(person);
	}
	/**
	 * Get the iterator for the members of this group
	 * @return
	 */
	public Iterator<String> membersIterator(){
		java.util.Iterator<String> it = members.iterator();
		return it;
	}
	/**
	 * Get the hashset with all group members
	 * @return
	 */
	public HashSet<String> getMembers(){
		return members;
	}
	/**
	 * Get the hashset with all group heads
	 * @return
	 */
	public HashSet<String> getHeads(){
		return heads;
	}
}

