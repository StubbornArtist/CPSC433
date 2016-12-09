package cpsc433;

import java.util.HashSet;
import java.util.Iterator;

/**
 * A data structure to hold information about rooms
 * @author Ashley Currie, Cooper Davies, Edraelan Ayuban, Erica Aguete
 */
public class Room {
	
	private String number;
	private char size;
	public HashSet<String> close_to;
	
	/**
	 * Construct a room with a number
	 * @param num
	 * 			The room number
	 */
	public Room(String num){
		number = num;
		close_to = new HashSet<String>();
		//size is medium by default
		size = 'm';
	}
	/**
	 * Add a room that is close to this room
	 * @param r
	 * 			The room close to this room
	 */
	public void addNeighbour(String r){
		close_to.add(r);
	}
	/**
	 * Get this rooms number
	 * @return
	 * 		The room number
	 */
	public String getRoomNumber(){
		return this.number;
	}
	/**
	 * Set the size of this room
	 * @param size
	 * 			The size you want the room to be
	 */
	public void setRoomSize(char size){
		this.size = size;
	}
	/**
	 * Get the size of the room
	 * @return
	 */
	public char getRoomSize(){
		return size;
	}
	/**
	 * Check if a room with a given number is close to this room
	 * @param room
	 * 			the other room's number
	 * @return
	 * 			true if the room is close, false otherwise
	 */
	public boolean neighbour(String room){
		return close_to.contains(room);
	}
	
	/**
	 * Get the iterator for the neighbours of this room
	 * @return
	 */
	public Iterator<String> closeToIterator(){
		java.util.Iterator<String> it = close_to.iterator();
		return it;
	}
	@Override
	public String toString(){
		
		return number;
	}
}
