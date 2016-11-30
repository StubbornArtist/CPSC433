package cpsc433;

import java.util.ArrayList;
import java.util.HashSet;

public class Assignment {
	
	private Room room;
	private HashSet<Person> people;
	
	
	public Assignment(){
		this.room = null;
		this.people = null;
	}
	
	public Assignment(Room room, Person per){
		this.room = room;
		this.people = new HashSet<Person>();
		this.people.add(per);
	}
	
	public Assignment(Room room, HashSet<Person> people ){
		this.room = room;
		this.people = people;
	}
	
	public Assignment(Room room, ArrayList<Person> people){
		this.room = room;
		HashSet<Person> temp = new HashSet<Person>();
		for(Person per: people){
			temp.add(per);
		}
		this.people = temp;
	}
	
	public Room getRoom(){
		return room;
	}
	
	public HashSet<Person> getPeople(){
		return people;
	}
	
	public void addPerson(Person p){
		this.people.add(p);
		
	}
	
	public boolean contains(Person p){
		return people.contains(p);
	}
	
}
