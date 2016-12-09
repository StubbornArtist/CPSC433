package cpsc433;

/**
 * A data structure to hold information about a person
 * @author Ashley Currie, Cooper Davies, Edraelan Ayuban, Erica Aguete
 */
import java.util.HashSet;

public class Person {
	public String name;
	public boolean smokes;
	public boolean hacks;
	private HashSet<String> roles;
	private HashSet<String> coWorkers;
	
	/**
	 * Construct a person with a given name
	 * @param name
	 * 			The person's name
	 */
	public Person(String name){
		this.name = name;
		//roles and coworkers are empty by default
		roles = new HashSet<String>();
		coWorkers = new HashSet<String>();
	}
	/**
	 * Give the person a new role
	 * @param role
	 * 			the name of the rol to assign
	 */
	public void addRole(String role){
		roles.add(role);
	}
	/**
	 * This method returns true if this person has the role specified 
	 * 
	 * @param role
	 * 			The role ("secretary", "manager",or "researcher")
	 * @return
	 */
	public boolean hasRole(String role){	
		if(roles.contains(role)){	
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Get the iterator for the roles the person has
	 * @return
	 */
	public java.util.Iterator<String> rolesIterator(){
		return roles.iterator();
	}
	/**
	 * Add a person that this person works with
	 * @param p
	 * 			The new coworker
	 */
	public void addCoWorker(String p){
		coWorkers.add(p);
	}
	/**
	 * Tells you whether a person with a given name is this person's coworker
	 * @param p
	 * 			The name of the possible coworker
	 * @return
	 */
	public boolean hasCoWorker(String p){
		return coWorkers.contains(p);
	}
	/**
	 * Get the iterator for this person's coworkers
	 * @return
	 */
	public java.util.Iterator<String> coWorkerIterator(){
		return coWorkers.iterator();
	}
	@Override
	public String toString(){
		return name;
	}
}
