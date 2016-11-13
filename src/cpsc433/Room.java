package cpsc433;

import java.util.HashSet;

public class Room {
	
	public String number;
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

}