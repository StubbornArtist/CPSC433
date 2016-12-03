package cpsc433;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

public class Assignment {
	
	private Room room;
	private HashSet<Person> people;
	private LinkedHashMap<Integer,Person> peopleIndexed;
	
	public Assignment(){
		this.room = null;
		this.people = null;
	}
	public Assignment(Assignment a){
		this.room = a.getRoom();
		this.people = new HashSet<Person>(a.getPeople());
		generateIndexed();
	}
	public Assignment(Room room, Person per){
		this.room = room;
		this.people = new HashSet<Person>();
		this.people.add(per);
		generateIndexed();
	}
	public Assignment(Room room, HashSet<Person> people ){
		this.room = room;
		this.people = people;
		generateIndexed();
	}
	public Assignment(Room room, ArrayList<Person> people){
		this.room = room;
		HashSet<Person> temp = new HashSet<Person>();
		for(Person per: people){
			temp.add(per);
		}
		this.people = temp;
		generateIndexed();
	}
		
	public Person randomPerson(){
		Random rand = new Random();
		Integer i = rand.nextInt(peopleIndexed.size());
		return peopleIndexed.get(i);
	}
	
	public void removePerson(Person p){
		people.remove(p);
		generateIndexed();
	}
	
	public boolean isEmpty(){
		return people.isEmpty();
	}
	
	public void addPerson(Person p){
		this.people.add(p);
		this.peopleIndexed.put(peopleIndexed.size(), p);
	}
			
	public boolean contains(Person p){
		return people.contains(p);
	}
	
	public Room getRoom(){
		return room;
	}
	
	public HashSet<Person> getPeople(){
		return people;
	}
	
	private void generateIndexed(){
		Iterator<Person> peopleIt = this.people.iterator();
		this.peopleIndexed = new LinkedHashMap<Integer,Person>();
		Integer count = 0;
		while(peopleIt.hasNext()){
			this.peopleIndexed.put(count, peopleIt.next());
			count++;
		}
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
