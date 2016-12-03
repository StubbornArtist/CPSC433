package cpsc433;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;

public class Constraints {
	private static Constraints instance = null;
	private Constraints(){}
	
	public static Constraints getInstance(){
		if(instance == null){
			instance = new Constraints();
		}
		return instance;
	}
	public boolean hardConstraint1(LinkedHashMap<String, String> a, Environment e) {
		Iterator<String> people = e.getPeople().keySet().iterator();
		
		while(people.hasNext()){
			if(!a.containsKey(people.next())){
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Hard Constraint: No person is assigned more than one room.
	 * 
	 * @param a : hashmap containing keys of room names and an assignment containing: the room; people assigned to the room.
	 * @param e : environment of the search process
	 * @return true if for all p:Person, there does not exist r,s:Room such that assigned-to(p,r) && assigned-to(p,s).
	 */
	public boolean hardConstraint2(LinkedHashMap<String, Assignment> a, Environment e) {
		for (String r : a.keySet()) {
			for (String s : a.keySet()) {
				// iterate through assignments to get 2 different rooms
				if (!r.equals(s)) {
					HashSet<Person> roomR = a.get(r).getPeople();
					HashSet<Person> roomS = a.get(s).getPeople();
					
					// for each person p in room R, if p is also in RoomS,
					// the hard constraint is broken
					for (Person p : roomR) {
						if (roomS.contains(p)) return false;
					}
				}
			}
		} return true;
	}

	public boolean hardConstraint3(LinkedHashMap<String, Assignment> a, Environment e) {
		return true;

	}

	// Project heads, Group Heads, and Managers can't share a room with anyone else
	public boolean hardConstraint4(LinkedHashMap<String, Assignment> a, Environment e) {
		
		Assignment assign;
		HashSet people;
		Iterator<Person> it;
		Person person;
		Group g;
		Project p;
		
		// Iterate through the Linked Hash Map to examine each assignment
		for(Map.Entry<String, Assignment> assignment : a.entrySet())
		{
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();
			
			if (people.size() > 1)
			{
				it = people.iterator();
				
				while(it.hasNext()){
					
					person = (Person) it.next();
					
					// Check if the current person is a manager 
					if(e.e_manager(person.name)){
						return false;
					}
					
					// Check if this person is a group head
					for(Map.Entry<String, Group> group : e.getGroups().entrySet()){
						g = group.getValue();
						if(e.e_heads_group(person.name, g.getName())){
							return false;
						}
					}
					
					// Check if this person is a project head
					for(Map.Entry<String, Project> project : e.projects.entrySet()){
						p = project.getValue();
						if(e.e_heads_project(person.name, p.getName())){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	//All heads of groups should have a large office
	//penalty of -40
	public int softConstraint1(LinkedHashMap<String, String> a, Environment e) {
		int score = 0;
		Iterator<String> heads;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();
			while(groupsIt.hasNext()){
				heads = groups.get(groupsIt.next()).getHeadIterator();
				while(heads.hasNext()){
					if(!e.e_large_room(a.get(heads.next()))){
						score +=-40;
					}	
				}
			}	
		return score;
	}

	//All heads of groups should be close to all members of their group
	//penalty of -2
	public int softConstraint2(LinkedHashMap<String, String> a, Environment e) {
		int score = 0;
		boolean isClose = false;
		Iterator<String> heads;
		Iterator<String> members;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();
		
		while(groupsIt.hasNext()){
			Group g = groups.get(groupsIt.next());
			heads = g.getHeadIterator();
			while(heads.hasNext()){
				String head = heads.next();
				members = g.membersIterator();
				while(members.hasNext()){
					String mem = members.next();
					if(e.e_close(a.get(head), a.get(mem))){
						isClose = true;
					}
					if (!isClose && !mem.equals(head)) score -= 2;
					else isClose = false;
				}
			}
		}
		return score;
	}


	public int softConstraint3(LinkedHashMap<String, String> a, Environment e) {
		Iterator<String> heads;
		Iterator<String> members;
		boolean closeToOne = false;
		int numSec = 0;
		int score = 0;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();
		
		while(groupsIt.hasNext()){
			Group g = groups.get(groupsIt.next());
			heads = g.getHeadIterator();
			while(heads.hasNext()){
				String head = heads.next();
				members = g.membersIterator();
				while(members.hasNext()){
					String mem = members.next();
					if(e.e_secretary(mem)){
						numSec++;
						if(e.e_close(a.get(head), a.get(mem))) closeToOne = true;
					}
				}
				if(!closeToOne && numSec > 0) score += -30;
				closeToOne = false;
				numSec = 0;
			}
		}
		return score;
	}

	public int softConstraint4(LinkedHashMap<String, String> a, Environment e) {
		Iterator<String> people = a.keySet().iterator();
		Iterator<String> coworkers;
		int score = 0;
		
		while(people.hasNext()){
			String person = people.next();
			if(e.e_secretary(person)){
				coworkers = a.keySet().iterator();
				while(coworkers.hasNext()){
					String coworker = coworkers.next();
					if(a.get(person).equals(a.get(coworker)) && !e.e_secretary(coworker)){
						score+=-5;
					}
				}
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: All managers should be close to at least one secretary in their group.
	 * Accumulate 20 penalty points for each group head that is not close to at least one secretary in their group.
	 * 
	 * @param a : hashmap containing: (key) the room name; (value) the name of the person assigned to the room.
	 * @param e: environment of the search process
	 * @return pen: total penalty points for violating this constraint
	 */
	public int softConstraint5(LinkedHashMap<String, String> a, Environment e) {
		boolean isClose = false;
		int score = 0;
		
		for (String p : a.keySet()) {
			// get room associated with person p
			String rName = a.get(p);
			Room r = e.getRooms().get(rName);
			
			if (e.e_manager(p)) {
				for (String gs : e.getGroups().keySet()) {
					if (e.e_group(p,gs)) {
						// get group p is in
						Group g = e.getGroups().get(gs);
						Iterator<String> m = g.membersIterator();
						
						// iterate through group members
						while (m.hasNext()) {
							String  m2 = m.next();
							// if we encounter a secretary
							if (e.e_secretary(m2)) {
								Iterator<String> s = r.closeToIterator();
								if (e.e_close(a.get(p),a.get(m2))) {
									isClose = true;
								}
								// if no secretaries are close, penalize
								if (!isClose) score -=20;
							} // no need to iterate through group members to search for more secretaries
							if (isClose) break;
						}
					}
				}
			}
		} return score;
	}

	/**
	 * Soft Constraint: All managers should be close to their group head.
	 * Accumulate 20 penalty points for each group head that is not close to at least one secretary in their group.
	 * 
	 * @param a : hashmap containing: (key) the room name; (value) the name of the person assigned to the room.
	 * @param e: environment of the search process
	 * @return total penalty points for violating this constraint
	 */
	public int softConstraint6(LinkedHashMap<String, String> a, Environment e) {
		boolean isClose = false;
		int pen = 0;
		
		for (String p : a.keySet()) {
			// get room associated with person p
			String rName = a.get(p);
			Room r = e.getRooms().get(rName);
			
			if (e.e_manager(p)) {
				for (String gs : e.getGroups().keySet()) {
					if (e.e_group(p,gs)) {
						// get group p is in
						Group g = e.getGroups().get(gs);
						Iterator<String> m = g.membersIterator();
						
						// iterate through group gs members
						while (m.hasNext()) {
							String m2 = m.next();
							//if we encounter a group head in group gs
							if (e.e_heads_group(m2,gs)) {
								Iterator<String> s = r.closeToIterator();
								
								// iterate through rooms ss close to room r
								if (e.e_close(a.get(p),a.get(m2))) {
									isClose = true;
								}
								// if no group heads are close, penalize
								if (!isClose) pen -= 20;
							}// no need to iterate through group members to search for more heads
							if (isClose) break;
						}	
					}
				}
			}
		} return pen;
	}

	/**
	 * Soft Constraint: All managers should be close to all members of their group.
	 * Accumulate 2 penalty points for each group head that is not close to at least one secretary in their group.
	 * 
	 * @param a : hashmap containing: (key) the room name; (value) the name of the person assigned to the room.
	 * @param e: environment of the search process
	 * @return total penalty points for violating this constraint
	 */
	public int softConstraint7(LinkedHashMap<String, String> a, Environment e) {
		boolean isClose = false;
		int pen = 0;
		
		for (String p : a.keySet()) {
			// get room associated with person p
			String rName = a.get(p);
			Room r = e.getRooms().get(rName);
			
			if (e.e_manager(p)) {
				for (String gs : e.getGroups().keySet()) {
					if (e.e_group(p,gs)) {
						// get group p is in
						Group g = e.getGroups().get(gs);
						Iterator<String> m = g.membersIterator();
						
						// iterate through group members
						while (m.hasNext()) {
							String m2 = m.next();	
							Iterator<String> s = r.closeToIterator();
							
							// iterate through rooms ss close to room r
							if (e.e_close(a.get(p),a.get(m2))) {
								isClose = true;
							}
							// if m not in any of the rooms close to r, penalize
							if (!isClose && !m2.equals(p)) pen -= 2;
							// reset boolean
							isClose = false;

						}
					}	
				}
			}
		} return pen;
	}

	/**
	 * Soft Constraint: All project heads should be close to all members of their project.
	 * Accumulate 5 penalty points for each group head that is not close to at least one secretary in their group.
	 * 
	 * @param a : hashmap containing: (key) the room name; (value) the name of the person assigned to the room.
	 * @param e: environment of the search process
	 * @return total penalty points for violating this constraint
	 */
	public int softConstraint8(LinkedHashMap<String, String> a, Environment e) {
		boolean isClose = false;
		int pen = 0;
		
		for (String p : a.keySet()) {
			// get room associated with person p
			String rName = a.get(p);
			Room r = e.getRooms().get(rName);

			for (String js : e.getProjects().keySet()) {
				if (e.e_heads_project(p,js)) {
					// get project p is in
					Project j = e.getProjects().get(js);
					Iterator<String> m = j.membersIterator();
					
					// iterate through group members
					while (m.hasNext()) {
						String m2 = m.next();	
						Iterator<String> s = r.closeToIterator();
						
						// iterate through rooms ss close to room r
						if (e.e_close(a.get(p),a.get(m2))) {
							isClose = true;
						}
						// if m not in any of the rooms close to r, penalize
						if (!isClose && !m2.equals(p)) pen -= 5;
						// reset boolean
						isClose = false;		
					}
				}	
			}
		} return pen;
	}

	public int softConstraint9(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint10(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint11(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint12(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	// If a non-secretary hacker/non-hacker shares an office, then he/she
	// should share with another hacker/non-hacker
	public int softConstraint13(LinkedHashMap<String, Assignment> a, Environment e) {

		int penalty = 0;
		Assignment assign;
		HashSet people;
		Iterator<Person> it;
		Person person1, person2;
		
		// Iterate through the Linked Hash Map to examine each assignment
		for(Map.Entry<String, Assignment> assignment : a.entrySet())
		{
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();
			
			if (people.size() > 1)
			{
				it = people.iterator();
					
				person1 = (Person) it.next();
				person2 = (Person) it.next();
				
				// Check if both are non-secretaries
				if(!e.e_secretary(person1.name) && !e.e_secretary(person2.name)){

					// Check if 'person1' is a hacker and if 'person2' is a non-hacker.
					// If so, apply penalty.
					if ( !e.e_hacker(person1.name) && e.e_hacker(person2.name) )
					{
						penalty += -2;
					}
					
					// Check if 'person1' is a non-hacker and if 'person2' is a hacker.
					// If so, apply penalty
					else if ( e.e_hacker(person1.name) && !e.e_hacker(person2.name) )
					{
						penalty += -2;
					}
				}
			}
		}
		return penalty;
	}

	// People prefer their own room
	public int softConstraint14(LinkedHashMap<String, Assignment> a, Environment e) {
		int penalty = 0;
		Assignment assign;
		HashSet people;
		
		// Iterate through the Linked Hash Map to examine each assignment
		for(Map.Entry<String, Assignment> assignment : a.entrySet())
		{
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();
			
			if (people.size() > 1)
			{
				penalty += people.size() * -4;
			}
		}
		return penalty;
	}

	// If two people share an office, they should work together
	public int softConstraint15(LinkedHashMap<String, Assignment> a, Environment e) {
		int penalty = 0;
		Assignment assign;
		HashSet people;
		Iterator<Person> it;
		Person person1, person2;
		
		// Iterate through the Linked Hash Map to examine each assignment
		for(Map.Entry<String, Assignment> assignment : a.entrySet())
		{
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();
			
			if (people.size() > 1)
			{
				it = people.iterator();
					
				person1 = (Person) it.next();
				person2 = (Person) it.next();
				
				// Check if both are non-secretaries
				if(!e.e_works_with(person1.name,person2.name)){
					penalty += -3;
				}
			}
		}
		return penalty;
	}

	// Two people shouldn't share a small room
	public int softConstraint16(LinkedHashMap<String, Assignment> a, Environment e) {

		int penalty = 0;
		Assignment assign;
		HashSet people;
		Room room;
		
		// Iterate through the Linked Hash Map to examine each assignment
		for(Map.Entry<String, Assignment> assignment : a.entrySet())
		{
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();
			// 'Room' object
			room = assign.getRoom();
			
			if (people.size() > 1)
			{
				if(room.getRoomSize() == 's'){
					penalty += -25;
				}
			}
		}
		return penalty;
	}
	
	public int eval(Node n, Environment e){
		int score = Integer.MAX_VALUE;
		if (!hardConstraint1(n.StringAssignments, e)){
			return -1;
		}
		if (!hardConstraint2(n.Assignments, e)){
			return -1;
		}
		if (!hardConstraint3(n.Assignments, e)){
			return -1;
		}
		if (!hardConstraint4(n.Assignments, e)){
			return -1;
		}
		score += softConstraint1(n.StringAssignments,e);
		score += softConstraint2(n.StringAssignments,e);
		score += softConstraint3(n.StringAssignments,e);
		score += softConstraint4(n.StringAssignments,e);
		score += softConstraint5(n.StringAssignments,e);
		score += softConstraint6(n.StringAssignments,e);
		score += softConstraint7(n.StringAssignments,e);
		score += softConstraint8(n.StringAssignments,e);
		score += softConstraint9(n.Assignments,e);
		score += softConstraint10(n.Assignments,e);
		score += softConstraint11(n.Assignments,e);
		score += softConstraint12(n.Assignments,e);
		score += softConstraint13(n.Assignments,e);
		score += softConstraint14(n.Assignments,e);
		score += softConstraint15(n.Assignments,e);
		score += softConstraint16(n.Assignments,e);
		
		return score;
	}

}
