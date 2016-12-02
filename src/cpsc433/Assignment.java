package cpsc433;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Assignment {
	
	private Room room;
	private HashSet<Person> people;
	private ArrayList<Person> peopleKeys;
	
	
	public Assignment(){
		this.room = null;
		this.people = null;
	}
	public Assignment(Room room, Person per){
		this.room = room;
		this.people = new HashSet<Person>();
		this.people.add(per);
		this.peopleKeys = new ArrayList<Person>(people);
	}
	
	public Assignment(Room room, HashSet<Person> people ){
		this.room = room;
		this.people = people;
		this.peopleKeys = new ArrayList<Person>(people);
	}
	
	public Assignment(Room room, ArrayList<Person> people){
		this.room = room;
		HashSet<Person> temp = new HashSet<Person>();
		for(Person per: people){
			temp.add(per);
		}
		this.people = temp;
		this.peopleKeys = new ArrayList<Person>(people);
	}
	
	public Room getRoom(){
		return room;
	}
	
	public Person randomPerson(){
		Random rand = new Random();
		Person p = peopleKeys.get(rand.nextInt(peopleKeys.size()));
		people.remove(p);
		return p;
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
