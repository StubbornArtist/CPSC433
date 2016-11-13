package cpsc433;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.text.html.HTMLDocument.Iterator;

import cpsc433.Predicate.ParamType;

/**
 * This is class extends {@link cpsc433.PredicateReader} just as required to 
 * in the assignment. You can extend this class to include your predicate definitions
 * or you can create another class that extends {@link cpsc433.PredicateReader} and
 * use that one.
 * <p>
 * I have defined this class as a singleton.
 * 
 * <p>Copyright: Copyright (c) 2003-16, Department of Computer Science, University 
 * of Calgary.  Permission to use, copy, modify, distribute and sell this 
 * software and its documentation for any purpose is hereby granted without 
 * fee, provided that the above copyright notice appear in all copies and that
 * both that copyright notice and this permission notice appear in supporting 
 * documentation.  The Department of Computer Science makes no representations
 * about the suitability of this software for any purpose.  It is provided
 * "as is" without express or implied warranty.</p>
 *
 * @author <a href="http://www.cpsc.ucalgary.ca/~kremer/">Rob Kremer</a>
 *
 */
public class Environment extends PredicateReader implements SisyphusPredicates {

	private static Environment instance=null;
	protected boolean fixedAssignments=false;
	private LinkedHashMap<String,Person> people;
	private LinkedHashMap<String,Room> rooms;
	private LinkedHashMap<String, String> assignments;
	private LinkedHashMap<String, Group> groups;
	private LinkedHashMap<String, Project> projects;
	
	protected Environment(String name) {
		super(name==null?"theEnvironment":name);
		people = new LinkedHashMap<String, Person>();
		rooms = new LinkedHashMap<String, Room>();
		assignments = new LinkedHashMap<String, String>();
		groups = new LinkedHashMap<String, Group>();
		projects = new LinkedHashMap<String, Project>();
	}
	
	/**
	 * A getter for the global instance of this class.  If an instance of this class
	 * does not already exist, it will be created.
	 * @return The singleton (global) instance.
	 */
	public static Environment get() {
		if (instance==null) instance = new Environment(null);
		return instance;
	}

	// UTILITY PREDICATES
	
	/**
	 * The help text for the exit() predicate.
	 */
	public static String h_exit = "quit the program";
	/**
	 * The definition of the exit() assertion predicate.  It will exit the program abruptly.
	 */
	public void a_exit() {
		System.exit(0);
	}

	@Override
	public void a_person(String p) {
		// TODO Auto-generated method stub
		if(!e_person(p)){
			people.put(p, new Person(p));
		}
	}

	@Override
	public boolean e_person(String p) {
		// TODO Auto-generated method stub
		return people.containsKey(p);
	}

	@Override
	public void a_secretary(String p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean e_secretary(String p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void a_researcher(String p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean e_researcher(String p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void a_manager(String p) {
		// TODO Auto-generated method stub
		a_person(p);
		if (!e_manager(p)){	// Check if the person 'p' has the role "manager". If not, give him the role "manager".
			people.get(p).addRole("manager");
		}
	}

	@Override
	public boolean e_manager(String p) {
		// TODO Auto-generated method stub
		return e_person(p) && people.get(p).hasRole("manager");
	}

	@Override
	public void a_smoker(String p) {
		// TODO Auto-generated method stub
		a_person(p);
		people.get(p).smokes = true;
	}

	@Override
	public boolean e_smoker(String p) {
		// TODO Auto-generated method stub
		return e_person(p) && people.get(p).smokes;
	}

	@Override
	public void a_hacker(String p) {
		// TODO Auto-generated method stub
		a_person(p);
		people.get(p).hacks = true;
	}

	@Override
	public boolean e_hacker(String p) {
		// TODO Auto-generated method stub
		return e_person(p) && people.get(p).hacks;
	}

	@Override
	public void a_group(String p, String grp) {
		// TODO Auto-generated method stub
		a_person(p);
		a_group(grp);

		groups.get(grp).addMember(p);
	}

	@Override
	public boolean e_group(String p, String grp) {
		// TODO Auto-generated method stub
		if ( (!people.containsKey(p)) || (!groups.containsKey(grp)) ){
			return false;
		}
		return groups.get(grp).hasMember(p);
	}

	@Override
	public void a_project(String p, String prj) {
		// TODO Auto-generated method stub
		a_person(p);
		a_project(prj);
		
		projects.get(prj).addMember(p);
	}

	@Override
	public boolean e_project(String p, String prj) {
		// TODO Auto-generated method stub
		return e_person(p) && e_project(prj) && projects.get(prj).hasMember(p);
	}

	@Override
	public void a_heads_group(String p, String grp) {
		// TODO Auto-generated method stub
		a_group(p, grp);
		groups.get(grp).setHead(p);
	}

	@Override
	public boolean e_heads_group(String p, String grp) {
		// TODO Auto-generated method stub
		return e_person(p) && e_group(grp) && groups.get(grp).isHead(p);
	}

	@Override
	public void a_heads_project(String p, String prj) {
		// TODO Auto-generated method stub
		a_project(p, prj);
		projects.get(prj).setHead(p);
	}

	@Override
	public boolean e_heads_project(String p, String prj) {
		// TODO Auto-generated method stub
		return e_person(p) && e_project(prj) && projects.get(prj).isHead(p);
	}

	@Override
	public void a_works_with(String p, TreeSet<Pair<ParamType, Object>> p2s) {
		// TODO Auto-generated method stub
		java.util.Iterator<Pair<ParamType, Object>> it = p2s.iterator();
		while(it.hasNext()){
			String p2 = (String)it.next().getValue();
			a_works_with(p, p2);
		}
	}

	@Override
	public boolean e_works_with(String p, TreeSet<Pair<ParamType, Object>> p2s) {
		// TODO Auto-generated method stub
		java.util.Iterator<Pair<ParamType, Object>> it = p2s.iterator();
		while(it.hasNext()){
			String p2 = (String)it.next().getValue();
			if(!e_works_with(p, p2)){
				return false;
			}
		}
		return true;
	}

	@Override
	public void a_works_with(String p, String p2) {
		// TODO Auto-generated method stub
		a_person(p);
		a_person(p2);
		
		people.get(p).addCoWorker(p2);
		people.get(p2).addCoWorker(p);
	}

	@Override
	public boolean e_works_with(String p, String p2) {
		// TODO Auto-generated method stub
		return e_person(p) && e_person(p2) && people.get(p).hasCoWorker(p2);
	}

	@Override
	public void a_assign_to(String p, String room) throws Exception {
		// TODO Auto-generated method stub
		a_person(p);
		a_room(room);
		assignments.put(p, room);
	}

	@Override
	public boolean e_assign_to(String p, String room) {
		// TODO Auto-generated method stub
		return assignments.containsKey(p)&& assignments.get(p).equals(room);
	}

	@Override
	public void a_room(String r) {
		// TODO Auto-generated method stub
		if(!e_room(r)){
			rooms.put(r, new Room(r));
		}
	}

	@Override
	public boolean e_room(String r) {
		// TODO Auto-generated method stub
		return rooms.containsKey(r);
	}

	@Override
	public void a_close(String room, String room2) {
		// TODO Auto-generated method stub
		a_room(room);
		a_room(room2);
		rooms.get(room).addNeighbour(room2);
		rooms.get(room2).addNeighbour(room);	
	}

	@Override
	public boolean e_close(String room, String room2) {
		// TODO Auto-generated method stub
		return e_room(room) && e_room(room2)
		&& (rooms.get(room).neighbour(room2) || rooms.get(room2).neighbour(room));
	}

	@Override
	public void a_close(String room, TreeSet<Pair<ParamType, Object>> set) {
		// TODO Auto-generated method stub
		java.util.Iterator<Pair<ParamType, Object>> it = set.iterator();
		while(it.hasNext()){
			String r= (String)it.next().getValue();
			a_close(room, r);
		}
	}

	@Override
	public boolean e_close(String room, TreeSet<Pair<ParamType, Object>> set) {
		// TODO Auto-generated method stub
		java.util.Iterator<Pair<ParamType, Object>> it = set.iterator();
		while(it.hasNext()){
			String r = (String)it.next().getValue();
			if(!e_close(room, r)){
				return false;
			}
		}
		return true;
	}

	@Override
	public void a_large_room(String r) {
		// TODO Auto-generated method stub
		a_room(r);
		rooms.get(r).setRoomSize('l');
	}

	@Override
	public boolean e_large_room(String r) {
		// TODO Auto-generated method stub
		return e_room(r)&& rooms.get(r).getRoomSize()=='l';
	}

	@Override
	public void a_medium_room(String r) {
		// TODO Auto-generated method stub
		a_room(r);
		rooms.get(r).setRoomSize('m');
	}

	@Override
	public boolean e_medium_room(String r) {
		// TODO Auto-generated method stub
		return e_room(r) && rooms.get(r).getRoomSize() == 'm';
	}

	@Override
	public void a_small_room(String r) {
		// TODO Auto-generated method stub
		a_room(r);
		rooms.get(r).setRoomSize('s');
	}

	@Override
	public boolean e_small_room(String r) {
		// TODO Auto-generated method stub
		return e_room(r) && rooms.get(r).getRoomSize()== 's'; 
	}

	@Override
	public void a_group(String g) {
		// TODO Auto-generated method stub
		if (!groups.containsKey(g)){
			groups.put(g, new Group(g));
		}
	}

	@Override
	public boolean e_group(String g) {
		// TODO Auto-generated method stub
		return groups.containsKey(g);
	}

	@Override
	public void a_project(String p) {
		// TODO Auto-generated method stub
		if(!e_project(p)){
			projects.put(p, new Project(p));
		}
	}

	@Override
	public boolean e_project(String p) {
		// TODO Auto-generated method stub
		return projects.containsKey(p);
	}

	@Override
	public void a_large_project(String prj) {
		// TODO Auto-generated method stub
		a_project(prj);
		projects.get(prj).setLargeProj(true);
	}

	@Override
	public boolean e_large_project(String prj) {
		// TODO Auto-generated method stub
		return e_project(prj) && projects.get(prj).isLarge();
	}
}

