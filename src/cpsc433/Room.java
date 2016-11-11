package cpsc433;

import java.util.ArrayList;

public class Room {
	
	public String number;
	private char size;
	private ArrayList<String> close_to;
	
	public Room(String num){
		number = num;
		close_to = new ArrayList<String>();
		//size is medium by default
		size = 'm';
	}
	
	public void addNeighbour(String r){
		if(!close_to.contains(r)){
			close_to.add(r);
		}
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