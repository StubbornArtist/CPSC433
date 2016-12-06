package cpsc433;

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

