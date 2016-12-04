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
	private LinkedHashMap<Person, Integer> peopleIndices;
	
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
		Integer i = rand.nextInt(people.size());
		return personAt(i);
	}
	
	public void removePerson(Person p){
		people.remove(p);
		Integer index = peopleIndices.get(p);
		peopleIndexed.remove(index);
		peopleIndices.remove(p);
	}
	
	public boolean isEmpty(){
		return people.isEmpty();
	}
	
	public void addPerson(Person p){
		this.people.add(p);
		this.peopleIndexed.put(peopleIndexed.size(), p);
		this.peopleIndices.put(p, peopleIndices.size());
	}
	
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
		this.peopleIndices = new LinkedHashMap<Person, Integer>();
		Integer count = 0;
		while(peopleIt.hasNext()){
			Person p = peopleIt.next();
			this.peopleIndexed.put(count, p);
			this.peopleIndices.put(p, count);
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
