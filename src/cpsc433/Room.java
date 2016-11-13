package cpsc433;

import java.util.HashSet;
import java.util.Iterator;

public class Room {
	
	private String number;
	private char size;
	private HashSet<String> close_to;
	
	public Room(String num){
		number = num;
		close_to = new HashSet<String>();
		//size is medium by default
		size = 'm';
	}
	
	public void addNeighbour(String r){
		close_to.add(r);
	}
	
	public void setRoomSize(char size){
		this.size = size;
	}
	
	public char getRoomSize(){
		return size;
	}
	
	public boolean neighbour(String room){
		return close_to.contains(room);
	}

	public Iterator<String> closeToIterator(){
		java.util.Iterator<String> it = close_to.iterator();
		return it;
	}
}
