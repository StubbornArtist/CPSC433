package cpsc433;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
/**
 * A class the represents a single fact in a generation.
 * There are two representations of room assignments. The first is person, room pairs.
 * The second is room, list of people pairs. Having both representations uses more memory,
 * but saves more time when evaluating constraints.
 * 
 * @author Ashley Currie, Cooper Davies, Edraelan Ayuban, Erica Aguete
 *
 */

public class Node {
	
	public LinkedHashMap<String, String> StringAssignments;
	public LinkedHashMap<String, Assignment> Assignments;
	public int score;
	
	/**
	 * Default constructor
	 */
	public Node(){
		this.Assignments = new LinkedHashMap<String, Assignment>();
		this.StringAssignments = new LinkedHashMap<String, String>();
	}
	/**
	 * Construct a fact with the second representation we are using
	 * @param assigns
	 * 				hash map of room assignments
	 */
	public Node(LinkedHashMap<String, Assignment> assigns){
		this.Assignments = assigns;
		this.score = 0;
	}
	/**
	 * Construct a fact from a pre-existing fact (deep copy)
	 * @param n
	 * 			the pre-existing fact
	 */
	public Node (Node n){
		this.Assignments = new LinkedHashMap<String, Assignment>();
		this.StringAssignments = new LinkedHashMap<String,String>();
		this.score = n.score;
		Iterator<String> assigns = n.Assignments.keySet().iterator();
		while(assigns.hasNext()){
			String room = assigns.next();
			this.Assignments.put(room,new Assignment(n.getAssignment(room)));
		}
		assigns = n.StringAssignments.keySet().iterator();
		while(assigns.hasNext()){
			String person = assigns.next();
			this.StringAssignments.put(person, n.getRoom(person));
		}
	}
	/**
	 * Set this fact's score
	 * @param score
	 * 			score to set
	 */
	public void setScore(int score){
		this.score = score;
	}
	/**
	 * Get the number of rooms in this fact
	 * @return
	 * 		number of rooms
	 */
	public int numRooms(){
		return Assignments.size();
	}
	/**
	 * Get the number of people in this fact
	 * @return
	 * 		The number of people
	 */
	public int numPeople(){
		return StringAssignments.size();
	}
	/**
	 * Add the string person, room pair representation to the fact
	 * @param a
	 * 			room assignments
	 */
	public void putStrings(LinkedHashMap<String, String> a){
		this.StringAssignments = a;
	}
	/**
	 * Get the person at a specified index
	 * @param index
	 * 				The index wanted
	 * @return
	 * 			The person at the given index. Returns null if the index is out of bounds.
	 */
	public Assignment peopleAt(int index){
		Iterator<String> assigns = Assignments.keySet().iterator();
		int count = 0;
		//loop through people keeping track of the person number
		while(assigns.hasNext()){
			//return the person once you've reached the specified index
			if(count == index){
				return Assignments.get(assigns.next());
			}
			assigns.next();
			count++;
		}
		return null;
	}
	/**
	 * Get a room at a specified index
	 * @param index
	 * 				The index wanted
	 * @return
	 * 			The room at the specified index. Returns null if the index is out of bounds.
	 */
	public Room roomAt(int index){
		Iterator<Assignment> rooms = Assignments.values().iterator();
		int count = 0;
		while(rooms.hasNext()){
			if(count == index){
				return rooms.next().getRoom();
			}
			rooms.next();
			count++;
		}
		return null;
	}
	/**
	 * Get the room that a specified person is in
	 * @param person
	 * 				The person
	 * @return
	 * 			The room that this person is in
	 */
	public Room getRoom(Person person){
		return Assignments.get(getRoom(person.name)).getRoom();
	}
	/**
	 * Get the room that a person with the specified name is in
	 * @param person
	 * 				Name of a person
	 * @return
	 * 			The room that the person is in
	 */
	
	public String getRoom(String person){
		return StringAssignments.get(person);
	}
	/**
	 * Get the list of people in a specified room (by room number)
	 * @param r
	 * 			The room number
	 * @return
	 * 			The list of people in the room with the given room number
	 */
	public Assignment getAssignment(String r){
		return Assignments.get(r);
	}
	
	/**
	 * Assign person p to room r
	 * @param p
	 * 			The person to assign
	 * @param r
	 * 			The room to assign the person to
	 */
	public void put(Person p, Room r){
		StringAssignments.put(p.name, r.getRoomNumber());
		if(!Assignments.containsKey(r.getRoomNumber())){
			Assignments.put(r.getRoomNumber(), new Assignment(r,p));
		}else{
			Assignments.get(r.getRoomNumber()).addPerson(p);
		}
	}
	/**
	 * Remove person p from room r
	 * @param p
	 * 			The person to remove from the room
	 * @param r
	 * 			The room to remove the person from
	 */
	public void remove(Person p, Room r){
		StringAssignments.remove(p.name);
		Assignments.get(r.getRoomNumber()).removePerson(p);
		if(Assignments.get(r.getRoomNumber()).isEmpty()){
			Assignments.remove(r.getRoomNumber());
		}
	}
	/**
	 * A mutation that will pick a person in this fact 
	 * and create a new fact where they are in a different room
	 * @param e
	 * 			An instance of the environment
	 * @return
	 * 			The new fact after the mutation
	 */
	public Node changeRooms(){
		//new node that is currently identical to this node
		Node newNode = new Node(this);
		
		Random rand = new Random();
		Room room;
		Room room2;
		Person person;
		
		//get a random room
		room = roomAt(rand.nextInt(numRooms()));
		//get a second random room
		room2 = roomAt(rand.nextInt(numRooms()));
		
		//grab a random person from the first room
		person = Assignments.get(room.getRoomNumber()).randomPerson();
		
		//move the person from their original room to their new room
		newNode.remove(person, room);
		newNode.put(person, room2);	
		return newNode;
	}
	/**
	 * A mutation that will pick two people from this fact and create a new fact
	 * where these two people have swapped rooms.
	 * @param e
	 * 			An instance of the environment
	 * @return
	 */
	public Node swapRooms(){
		//new fact that is currently identical to this fact
		Node newNode = new Node(this);
		
		Random rand = new Random();
		Room room1;
		Room room2;
		Person person1;
		Person person2;
		
		//grab two random rooms
		room1 = roomAt(rand.nextInt(numRooms()));
		room2 = roomAt(rand.nextInt(numRooms()));
		//grab a random person from each rooms
		person1 = Assignments.get(room1.getRoomNumber()).randomPerson();
		person2 = Assignments.get(room2.getRoomNumber()).randomPerson();
		
		//put the two people into opposite rooms
		newNode.remove(person1,room1);
		newNode.put(person1,room2);
		newNode.remove(person2, room2);
		newNode.put(person2, room1);		
		return newNode;
	}
	/**
	 * String representation of the room, list of people assignment form
	 */
	@Override
	public String toString(){
		Iterator<Assignment> it = Assignments.values().iterator();
		String node = this.score + "\n";
		while(it.hasNext()){
			node += it.next().toString();
			if(it.hasNext()) node+=",";
		}
		return node;
	}
	/**
	 * String representation of the person, room assignment form
	 * @return
	 */
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
}

