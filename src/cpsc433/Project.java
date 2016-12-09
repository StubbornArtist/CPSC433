package cpsc433;

/**
 * A data structure to hold information about projects
 * @author Ashley Currie, Cooper Davies, Edraelan Ayuban, Erica Aguete
 */

public class Project extends Group{
	private boolean largeProject;
	/**
	 * Construct a project with a name
	 * @param name
	 * 			The project name
	 */
	public Project(String name){
		super(name);
		largeProject = false;
	}
	/**
	 * Indicate whether this project is a large project or not
	 * @param b
	 * 		true if this is a large project false otherwise
	 *
	 */
	public void setLargeProj(boolean b){
		largeProject = b;
	}
	/**
	 * Indicates if the project is large or not
	 * @return
	 * 		true if this is a large project false otherwise
	 */
	public boolean isLarge(){
		return largeProject;
	}

}

