package cpsc433;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * A data structure to hold information about room assignments of form <room,{listofPeople}>
 * @author Ashley Currie, Cooper Davies, Edraelan Ayuban, Erica Aguete
 */

public class Assignment {
	
	private Room room;
	private HashSet<Person> people;
	
	/**
	 * Default constructor
	 */
	public Assignment(){
		this.room = null;
		this.people = null;
	}
	
	/**
	 * Create an assignment from a previously defined assignment
	 * @param a
	 * 			The assignment you want a deep copy of
	 */
	public Assignment(Assignment a){
		this.room = a.getRoom();
		this.people = new HashSet<Person>(a.getPeople());
	}
	/**
	 * Create an assignment from a room and a hashset of people
	 * @param room
	 * 			The room the people are assigned to
	 * @param people
	 * 			The people in the room
	 */
	
	public Assignment(Room room, HashSet<Person> people ){
		this.room = room;
		this.people = people;
	}
	/**
	 * Create an assignment from a room and a list of people
	 * 
	 * @param room
	 * 			The room the people are assigned to
	 * @param people
	 * 			The people in the room
	 */
	public Assignment(Room room, ArrayList<Person> people){
		this.room = room;
		HashSet<Person> temp = new HashSet<Person>();
		for(Person per: people){
			temp.add(per);
		}
		this.people = temp;
	}
	
	/**
	 * Create an assignment with a room and a single person
	 * @param room
	 * 			The room the person is in
	 * @param per
	 * 			The person in the room
	 */
	public Assignment(Room room, Person per){
		this.room = room;
		this.people = new HashSet<Person>();
		this.people.add(per);
	}
	
	/**
	 * Grabs a random person in the assignment.
	 * @return a random person
	 */
	public Person randomPerson(){
		Random rand = new Random();
		Integer i = rand.nextInt(people.size());
		return personAt(i);
	}
	
	/**
	 * Removes a specific person from the assignment
	 * @param p : person to remove
	 */
	public void removePerson(Person p){
		people.remove(p);
	}
	
	/**
	 * Adds a specific person to the assignment
	 * @param p : person to add
	 */
	public void addPerson(Person p){
		this.people.add(p);
	}
	/**
	 * Indicates whether this assignment has any people in it
	 * @return
	 * 		Whether the assignment has people (false) or not (true)
	 */
	public boolean isEmpty(){
		return people.isEmpty();
	}
	
	/**
	 * The number of people in an assignment
	 * @return
	 * 		The number of people
	 */
	public int size(){
		return people.size();
	}
	
	/**
	 * Returns the person at index i in the people data structure
	 * @param index
	 * @return person i
	 */
	public Person personAt(int index){
		Iterator<Person> people = this.people.iterator();
		int count = 0;
		while(people.hasNext()){
			if(count == index){
				return people.next();
			}
			people.next();
			count++;
		}
		return null;
	}
	
	/** 
	 * Returns true if p is assigned to the room, else false
	 * @param p : person in question
	 * @return true/false
	 */
	public boolean contains(Person p){
		return people.contains(p);
	}
	
	/**
	 * Get the room that the people in this assignment are assigned to
	 * @return
	 * 		The room the assignment refers to
	 */
	public Room getRoom(){
		return room;
	}
	/**
	 * Get a hashset of the people in the assignment
	 * @return
	 * 		The people
	 */
	public HashSet<Person> getPeople(){
		return people;
	}
	
	
	/**
	 * Returns a string representing the room name, followed by a list of people assigned to the room.
	 * 
	 * @return string of form <room_name, [name1,name2,...,nameN]>
	 */
	@Override 
	public String toString(){
		String a = "[";
		Iterator<Person> it = people.iterator();
		while(it.hasNext()){
			a+= it.next();
			if(it.hasNext()) a+=",";
		}
		return "<" + this.room + "," + a + "] >";  
	}
	
}
