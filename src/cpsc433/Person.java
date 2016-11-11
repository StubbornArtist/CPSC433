package cpsc433;

import java.util.ArrayList;

public class Person {
	private String name;
	public boolean smokes;
	public boolean hacks;
	private ArrayList<String> roles;
	private ArrayList<Person> coworkers;
	private ArrayList<String> groups;
	
	public Person(String name){
		this.name = name;
		roles = new ArrayList<String>();
		coworkers= new ArrayList<Person>();
		groups = new ArrayList<String>();
	}
	
	public String getName(){
		return name;
	}

	public void addRole(String role){
		if(!roles.contains(role)){
			roles.add(role);
		}
	}
	public void addCoWorker(Person p){
		if(!coworkers.contains(p)){
			coworkers.add(p);
		}
	}
	public void addGroup(String g){
		if(!groups.contains(g)){
			groups.add(g);
		}
	}
	
	public boolean inGroup(String g){
		return groups.contains(g);
	}
}
