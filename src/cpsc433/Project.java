package cpsc433;

/**
 * A data structure to hold information about projects
 * @author Ashley Currie, Cooper Davies, Edraelan Ayuban, Erica Aguete
 */

public class Project extends Group{
	private boolean largeProject;
	
	public Project(String name){
		super(name);
		largeProject = false;
	}
	
	public void setLargeProj(boolean b){
		largeProject = b;
	}
	
	public boolean isLarge(){
		return largeProject;
	}

}

