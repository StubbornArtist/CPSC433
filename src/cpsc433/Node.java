package cpsc433;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;


public class Node {
	
	public LinkedHashMap<String, String> StringAssignments;
	public LinkedHashMap<String, Assignment> Assignments;
	private LinkedHashMap<Integer, String> roomKeys;
	public int score;
	
	
	public Node(LinkedHashMap<String, Assignment> assigns){
		this.Assignments = assigns;
		this.score = 1000;
		generateRoomKeys();
	}
	public Node (Node n){
		this.Assignments = new LinkedHashMap<String, Assignment>();
		this.StringAssignments = new LinkedHashMap<String,String>();
		
		Iterator<String> assigns = n.Assignments.keySet().iterator();
		while(assigns.hasNext()){
			String room = assigns.next();
			this.Assignments.put(room,new Assignment(n.getAssignment(room)));
		}
		assigns = n.StringAssignments.keySet().iterator();
		while(assigns.hasNext()){
			String person = assigns.next();
			this.StringAssignments.put(person, getRoom(person));
		}
	}
	
	public void putStrings(LinkedHashMap<String, String> a){
		this.StringAssignments = a;
	}
	public Assignment randomAssignment(){
		Random rand = new Random();
		return Assignments.get(roomKeys.get(rand.nextInt(roomKeys.size())));
	}
	public Room getRoom(Person person){
		return Assignments.get(StringAssignments.get(person.name)).getRoom();
	}
	public String getRoom(String person){
		return StringAssignments.get(person);
	}
	public Assignment getAssignment(String r){
		return Assignments.get(r);
	}
	
	public void put(Person p, Room r){
		StringAssignments.put(p.name, r.getRoomNumber());
		if(!Assignments.containsKey(r.getRoomNumber())){
			Assignments.put(r.getRoomNumber(), new Assignment(r,p));
		}
		Assignments.get(r.getRoomNumber()).addPerson(p);
	}
	public void remove(Person p, Room r){
		StringAssignments.remove(p.name);
		Assignments.get(r.getRoomNumber()).removePerson(p);
		if(Assignments.get(r.getRoomNumber()).isEmpty()){
			Assignments.remove(r.getRoomNumber());
			generateRoomKeys();
		}
	}
	public Node changeRooms(Environment e){
		Node newNode = new Node(this);
		
		Random rand = new Random();
		Room room;
		Room room2;
		Person person;
		//do{
			room = Assignments.get(roomKeys.get(rand.nextInt(roomKeys.size()))).getRoom();
			room2 = Assignments.get(roomKeys.get(rand.nextInt(roomKeys.size()))).getRoom();
			person = Assignments.get(room).randomPerson();
		//}while(e.assignments.containsKey(person.name));
		newNode.remove(person, room);
		newNode.put(person, room2);	
		return newNode;
	}
	
	public Node swapRooms(Environment e){
		Node newNode = new Node(this);
		
		Random rand = new Random();
		Room room1;
		Room room2;
		Person person1;
		Person person2;
		//do{
			room1 = Assignments.get(roomKeys.get(rand.nextInt(roomKeys.size()))).getRoom();
			room2 = Assignments.get(roomKeys.get(rand.nextInt(roomKeys.size()))).getRoom();
			person1 = Assignments.get(room1).randomPerson();
			person2 = Assignments.get(room2).randomPerson();
		//}while(e.getAssignments().containsKey(person1.name) 
				//|| e.getAssignments().containsKey(person2.name));
			newNode.remove(person1,room1);
			newNode.put(person1,room2);
			newNode.remove(person2, room2);
			newNode.put(person2, room1);		
		return newNode;
		
	}
	
	@Override
	public String toString(){
		Iterator<Assignment> it = Assignments.values().iterator();
		String node = "";
		while(it.hasNext()){
			node += it.next().toString();
			if(it.hasNext()) node+=",";
		}
		return node;
	}
	
	public String altToString(){
		Iterator<String> it = StringAssignments.keySet().iterator();
		String node = "";
		while(it.hasNext()){
			String person = it.next();
			node+= "(" + person +"," + StringAssignments.get(person) + ")";
			if(it.hasNext()) node+= ",";
		}
		
		return node;
	}
	
	private void generateRoomKeys(){
		Iterator<String> rooms = Assignments.keySet().iterator();
		this.roomKeys = new LinkedHashMap<Integer, String>();
		Integer count = 0;
		while(rooms.hasNext()){
			roomKeys.put(count, rooms.next());
			count++;
		}
		
	}

}

