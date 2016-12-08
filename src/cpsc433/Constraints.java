package cpsc433;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
/**
 * A singleton class that holds methods to evaluate a fact with every hard and soft constraint
 * @author Ashley Currie, Cooper Davies, Edraelan Ayuban, Erica Aguete
 */
public class Constraints {
	private static Constraints instance = null;

	private Constraints() {
	}

	public static Constraints getInstance() {
		if (instance == null) {
			instance = new Constraints();
		}
		return instance;
	}
	
	/**
	 * Hard Constraint: All people from the environment have been assigned a room
	 * 
	 * @param a	: A hash map representing room assignments with person, room pairs
	 * @param e : Environment of the search process
	 * @return Whether the constraint was broken (false) or not (true)
	 */
	public boolean hardConstraint1(LinkedHashMap<String, String> a, Environment e) {
		Iterator<String> people = e.getPeople().keySet().iterator();
		
		// iterate through list of people in environment
		while (people.hasNext()) {
			// if assignment does not contain a person in the environment
			// return false
			if (!a.containsKey(people.next())) {
				return false;
			}
		} return true;
	}

	/**
	 * Hard Constraint: No person is assigned more than one room.
	 * 
	 * @param a : A hash map containing keys of room names and an assignment
	 *            containing: the room; people assigned to the room.
	 * @param e : Environment of the search process
	 * @return Whether the constraint was broken (false) or not (true)
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
						if (roomS.contains(p))
							return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Hard Constraint: No room contains more than two people.
	 * 
	 * @param a : A hash map of room assignments in room, list of people pairs
	 * @param e : Environment of the search process
	 * @return Whether the constraint was broken (false) or not (true)
	 */
	public boolean hardConstraint3(LinkedHashMap<String, Assignment> a, Environment e) {
		//loop through each room that is assigned
		for (Assignment assn : a.values()) {
			//check whether the room contains more then two people
			if (assn.getPeople().size() > 2) {
				return false;
			}
		} return true;
	}

	/**
	 * Hard Constraint: Every manager, group head, and project head has their own room.
	 * 
	 * @param a : A hash map of room assignments in room, list of person pairs
	 * @param e : Environment of the search process
	 * @return Whether the constraint was broken (false) or not (true)
	 */
	public boolean hardConstraint4(LinkedHashMap<String, Assignment> a, Environment e) {

		Assignment assign;
		HashSet<Person> people = null;
		Iterator<Person> it;
		Person person;
		Group g;
		Project p;

		// Iterate through the Linked Hash Map to examine each assignment
		for (Map.Entry<String, Assignment> assignment : a.entrySet()) {
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();

			if (people.size() > 1) {
				it = people.iterator();

				while (it.hasNext()) {

					person = (Person) it.next();

					// Check if the current person is a manager
					if (e.e_manager(person.name)) {
						return false;
					}

					// Check if this person is a group head
					for (Map.Entry<String, Group> group : e.getGroups().entrySet()) {
						g = group.getValue();
						if (e.e_heads_group(person.name, g.getName())) {
							return false;
						}
					}

					// Check if this person is a project head
					for (Map.Entry<String, Project> project : e.projects.entrySet()) {
						p = project.getValue();
						if (e.e_heads_project(person.name, p.getName())) {
							return false;
						}
					}
				}
			}
		} return true;
	}
	
	/**
	 * Hard Constraint: All people assigned initially remain assigned.
	 * Added as an ADDITIONAL constraint to maintain hard-coded assignments.
	 *  
	 * @param a : A hash map with person room pairs representing room assignments
	 * @param e : Environment of the search process.
	 * @return Whether the constraint was broken (false) or not (true)
	 */
	public boolean hardConstraint5(LinkedHashMap<String, String> a, Environment e){
		// iterate through assignments in environment
		for(String p: e.getAssignments().keySet()){
			// if a person is assigned a different room than in the environment
			// return false
			if(!a.get(p).equals(e.getAssignments().get(p))){
				return false;
			}
		}
		return true;
	}

	/**
	 * Soft Constraint: Group heads should have a large office.
	 * A penalty of -40 is given every time the constraint is violated.
	 * 
	 * @param a : A list of room assignments in person, room pairs
	 * @param e : Environment of the search process
	 * @return The score that the particular fact received for this constraint
	 */
	public int softConstraint1(LinkedHashMap<String, String> a, Environment e) {
		int score = 0;
		Iterator<String> heads;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();
		while (groupsIt.hasNext()) {
			heads = groups.get(groupsIt.next()).getHeadIterator();
			while (heads.hasNext()) {
				if (!e.e_large_room(a.get(heads.next()))) {
					score += -40;
				}
			}
		} return score;
	}

	/**
	 * Soft Constraint: Group heads should be close to all members of their group.
	 * A penalty of -2 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint2(LinkedHashMap<String, String> a, Environment e) {
		int score = 0;
		boolean isClose = false;
		Iterator<String> heads;
		Iterator<String> members;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();

		//go through each group
		while (groupsIt.hasNext()) {
			//current group
			Group g = groups.get(groupsIt.next());
			//list of heads for this group
			heads = g.getHeadIterator();
			//go through each head in this group
			while (heads.hasNext()) {
				//current head
				String head = heads.next();
				//each member of the group
				members = g.membersIterator();
				while (members.hasNext()) {
					//current member of the group
					String mem = members.next();
					//check that this member is close to the current head
					if (e.e_close(a.get(head), a.get(mem))) {
						isClose = true;
					}
					//if the current member is not the head them self and they are not close
					//to the head then penalize 
					//otherwise do not penalize
					if (!isClose && !mem.equals(head))
						score -= 2;
					else
						isClose = false;
				}
			}
		}
		return score;
	}
	
	/**
	 * Soft Constraint: Group heads are close to at least one secretary in their group.
	 * A penalty of -30 is given each time the constraint is violated. 
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint3(LinkedHashMap<String, String> a, Environment e) {
		Iterator<String> heads;
		Iterator<String> members;
		boolean closeToOne = false;
		int numSec = 0;
		int score = 0;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();
		//go through each group
		while (groupsIt.hasNext()) {
			//current group
			Group g = groups.get(groupsIt.next());
			//go through each head
			heads = g.getHeadIterator();
			while (heads.hasNext()) {
				//current head
				String head = heads.next();
				//go through each member of the current group
				members = g.membersIterator();
				while (members.hasNext()) {
					//current member
					String mem = members.next();
					//check if the current member is a secretary
					if (e.e_secretary(mem)) {
						//indicate that we found another secretary
						numSec++;
						//if this secretary is close to the head
						//then the head is close to at least one secretary in the group
						if (e.e_close(a.get(head), a.get(mem)))
							closeToOne = true;
					}
				}
				//if the current head is not close to any secretaries 
				//and the group actually contains secretaries then penalize
				if (!closeToOne && numSec > 0)
					score += -30;
				closeToOne = false;
				numSec = 0;
			}
		}
		return score;
	}
	
	/**
	 * Soft Constraint: Secretaries should share offices with other secretaries.
	 * A penalty of -5 is given each time the constraint is violated.
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint4(LinkedHashMap<String, String> a, Environment e) {
		Iterator<String> people = a.keySet().iterator();
		Iterator<String> coworkers;
		int score = 0;
		//go through each person 
		while (people.hasNext()) {
			//current person
			String person = people.next();
			//if the person is a secretary then check that 
			//anyone in the same room is also a secretary
			if (e.e_secretary(person)) {
				coworkers = a.keySet().iterator();
				while (coworkers.hasNext()) {
					String coworker = coworkers.next();
					if (a.get(person).equals(a.get(coworker)) && !e.e_secretary(coworker)) {
						score += -5;
					}
				}
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: Managers should be close to at least one secretary in their group.
	 * A penalty of -20 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint5(LinkedHashMap<String, String> a, Environment e) {
		boolean isClose = false;
		int score = 0;

		for (String p : a.keySet()) {
			if (e.e_manager(p)) {
				for (String gs : e.getGroups().keySet()) {
					if (e.e_group(p, gs)) {
						// get group p is in
						Group g = e.getGroups().get(gs);
						Iterator<String> m = g.membersIterator();

						// iterate through group members
						while (m.hasNext()) {
							String m2 = m.next();
							// if we encounter a secretary
							if (e.e_secretary(m2)) {
								if (e.e_close(a.get(p), a.get(m2))) {
									isClose = true;
								}
								// if no secretaries are close, penalize
								if (!isClose)
									score -= 20;
							} // no need to iterate through group members to
								// search for more secretaries
							if (isClose)
								break;
						}
					}
				}
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: Managers should be close to their group's head.
	 * A penalty of -20 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint6(LinkedHashMap<String, String> a, Environment e) {
		boolean isClose = false;
		int score = 0;

		for (String p : a.keySet()) {
			if (e.e_manager(p)) {
				for (String gs : e.getGroups().keySet()) {
					if (e.e_group(p, gs)) {
						// get group p is in
						Group g = e.getGroups().get(gs);
						Iterator<String> m = g.membersIterator();

						// iterate through group gs members
						while (m.hasNext()) {
							String m2 = m.next();
							// if we encounter a group head in group gs
							if (e.e_heads_group(m2, gs)) {
								// iterate through rooms ss close to room r
								if (e.e_close(a.get(p), a.get(m2))) {
									isClose = true;
								}
								// if no group heads are close, penalize
								if (!isClose)
									score -= 20;
							} // no need to iterate through group members to
								// search for more heads
							if (isClose)
								break;
						}
					}
				}
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: Managers should be close to all members of their group.
	 * A penalty of -2 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint7(LinkedHashMap<String, String> a, Environment e) {
		boolean isClose = false;
		int score = 0;

		for (String p : a.keySet()) {

			if (e.e_manager(p)) {
				for (String gs : e.getGroups().keySet()) {
					if (e.e_group(p, gs)) {
						// get group p is in
						Group g = e.getGroups().get(gs);
						Iterator<String> m = g.membersIterator();

						// iterate through group members
						while (m.hasNext()) {
							String m2 = m.next();

							// iterate through rooms ss close to room r
							if (e.e_close(a.get(p), a.get(m2))) {
								isClose = true;
							}
							// if m not in any of the rooms close to r, penalize
							if (!isClose && !m2.equals(p))
								score -= 2;
							// reset boolean
							isClose = false;

						}
					}
				}
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: Project heads should be close to all members of their projects.
	 * A penalty of -5 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint8(LinkedHashMap<String, String> a, Environment e) {
		boolean isClose = false;
		int score = 0;

		for (String p : a.keySet()) {

			for (String js : e.getProjects().keySet()) {
				if (e.e_heads_project(p, js)) {
					// get project p is in
					Project j = e.getProjects().get(js);
					Iterator<String> m = j.membersIterator();

					// iterate through group members
					while (m.hasNext()) {
						String m2 = m.next();

						// iterate through rooms ss close to room r
						if (e.e_close(a.get(p), a.get(m2))) {
							isClose = true;
						}
						// if m not in any of the rooms close to r, penalize
						if (!isClose && !m2.equals(p))
							score -= 5;
						// reset boolean
						isClose = false;
					}
				}
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: Large project heads should be close
	 * at least one secretary in their group.
	 * A penalty of -10 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint9(LinkedHashMap<String, String> a, Environment e) {
		int score = 0;
		// grabbing the projects
		LinkedHashMap<String, Project> projects = e.projects;

		// grab the project Heads
		for (Project proj : projects.values()) {
			if (!proj.isLarge())
				continue;
			Room headRoom = null;
			String secretaryRoom = null;
			boolean oneSecr = false;
			for (String head : proj.getHeads()) {
				headRoom = e.getRooms().get(a.get(head));
				for (String member : proj.getMembers()) {
					if (e.getPeople().get(member).hasRole("secretary")) {
						secretaryRoom = a.get(member);
						if (headRoom.close_to.contains(secretaryRoom)) {
							oneSecr = true;
						}
					}
				}
				if (oneSecr == false) {
					score -= 10;
				}
				// resetting for next head
				oneSecr = false;
			}
		} return score;
	}

	/**
	 * Soft Constraint: large project heads should be close to the head of their group.
	 * A penalty of -10 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room assignments in person, room pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint10(LinkedHashMap<String, String> a, Environment e) {
		int score = 0;
		// grabbing the projects
		LinkedHashMap<String, Project> projects = e.projects;
		LinkedHashMap<String, Group> groups = e.getGroups();

		HashSet<String> groupHeads = new HashSet<String>();
		for (Group gr : groups.values()) {
			for (String head : gr.getHeads()) {
				groupHeads.add(head);
			}
		}

		// grab the project Heads
		for (Project proj : projects.values()) {
			if (!proj.isLarge())
				continue;
			Room headRoom = null;
			for (String head : proj.getHeads()) {
				headRoom = e.getRooms().get(a.get(head));
				for (String member : proj.getMembers()) {
					if (groupHeads.contains(member)) {
						String groupHeadRoom = a.get(member);
						if (!headRoom.close_to.contains(groupHeadRoom)) {
							score -= 10;
						}
					}
				}
			}

		}
		return score;
	}

	/**
	 * Soft Constraint: A smoker shouldn't share an office with a non-smoker.
	 * A penalty of -50 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room, assignment pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint11(LinkedHashMap<String, Assignment> a, Environment e) {
		// The people we have already looked at, so we dont count them twice.
		HashSet<Person> checked = new HashSet<Person>();
		int score = 0;

		// Iterate through each assignment
		for (Assignment assn : a.values()) {
			// now we need to iterate through the people
			for (Person per : assn.getPeople()) {
				// we only really care if there is a smoker
				if (per.smokes) {
					// we need to check against other people in this assignment
					// (notice we can check against ourselves because this will
					// never cause a conflict
					for (Person per2 : assn.getPeople()) {
						// but make sure we havent checked this person already
						if (checked.contains(per2)) {
							continue;
						}
						// There is a non-smoker + a smoker so we decrement
						// score
						if (!per2.smokes) {
							score -= 50;
						}
					}
					checked.add(per);
				}
			}
		}
		return score;

	}

	/**
	 * Soft Constraint: Members of the same project should not
	 * share and office (to encourage synergy).
	 * A penalty of -7 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room, assignment pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint12(LinkedHashMap<String, Assignment> a, Environment e) {
		// The people we have already looked at, so we dont count them twice.
		HashSet<Person> checked = new HashSet<Person>();
		int score = 0;
		Project per1 = null;
		Project per2 = null;

		// Iterate through each assignment
		for (Assignment assn : a.values()) {
			// now we need to iterate through the people
			for (Person per : assn.getPeople()) {
				// we need to grab this person project.
				for (Project proj : e.projects.values()) {
					if (proj.hasMember(per.name)) {
						per1 = proj;
						break;
					}
				}
				// now we look at the other people
				for (Person pers2 : assn.getPeople()) {
					// are we looking at ourself or someone already checked?
					if (pers2 == per || checked.contains(pers2)) {
						continue;
					}
					// here we grab the second persons project
					for (Project proj : e.projects.values()) {
						if (proj.hasMember(pers2.name)) {
							per2 = proj;
							break;
						}
					}
					// if they share the same project, decrement the score
					if (per1 == per2) {
						score -= 7;
					}

				}
				// add this person so we know that we checked them
				checked.add(per);
			}
		}
		return score;

	}

	/**
	 * Soft Constraint: If a non-secretary hacker/non-hacker shares an office, then
	 * they should share with another hacker/non-hacker
	 * A penalty of -2 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room, assignment pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint13(LinkedHashMap<String, Assignment> a, Environment e) {

		int score = 0;
		Assignment assign;
		HashSet<Person> people = null;
		Iterator<Person> it;
		Person person1, person2;

		// Iterate through the Linked Hash Map to examine each assignment
		for (Map.Entry<String, Assignment> assignment : a.entrySet()) {
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();

			if (people.size() > 1) {
				it = people.iterator();

				person1 = (Person) it.next();
				person2 = (Person) it.next();

				// Check if both are non-secretaries
				if (!e.e_secretary(person1.name) && !e.e_secretary(person2.name)) {

					// Check if 'person1' is a hacker and if 'person2' is a
					// non-hacker.
					// If so, apply penalty.
					if (!e.e_hacker(person1.name) && e.e_hacker(person2.name)) {
						score += -2;
					}

					// Check if 'person1' is a non-hacker and if 'person2' is a
					// hacker.
					// If so, apply penalty
					else if (e.e_hacker(person1.name) && !e.e_hacker(person2.name)) {
						score += -2;
					}
				}
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: People prefer to have their own offices.
	 * A penalty of -4 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room, assignment pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint14(LinkedHashMap<String, Assignment> a, Environment e) {
		int score = 0;
		Assignment assign;
		HashSet<Person> people = null;

		// Iterate through the Linked Hash Map to examine each assignment
		for (Map.Entry<String, Assignment> assignment : a.entrySet()) {
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();

			if (people.size() > 1) {
				score += people.size() * -4;
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: If two people share an office, they should work together.
	 * A penalty of -3 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room, assignment pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint15(LinkedHashMap<String, Assignment> a, Environment e) {
		int score = 0;
		Assignment assign;
		HashSet<Person> people = null;
		Iterator<Person> it;
		Person person1, person2;

		// Iterate through the Linked Hash Map to examine each assignment
		for (Map.Entry<String, Assignment> assignment : a.entrySet()) {
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();

			if (people.size() > 1) {
				it = people.iterator();

				person1 = (Person) it.next();
				person2 = (Person) it.next();

				// Check if both are non-secretaries
				if (!e.e_works_with(person1.name, person2.name)) {
					score += -3;
				}
			}
		}
		return score;
	}

	/**
	 * Soft Constraint: Two people shouldn't share a small room.
	 * A penalty of -25 is given every time the constraint is violated.
	 * 
	 * @param a : A hash map of room, assignment pairs
	 * @param e : An instance of the environment
	 * @return Total accumulated penalty points.
	 */
	public int softConstraint16(LinkedHashMap<String, Assignment> a, Environment e) {

		int score = 0;
		Assignment assign;
		HashSet<Person> people = null;
		Room room;

		// Iterate through the Linked Hash Map to examine each assignment
		for (Map.Entry<String, Assignment> assignment : a.entrySet()) {
			// 'Assignment' Object
			assign = assignment.getValue();
			// Set of people assigned to this room
			people = assign.getPeople();
			// 'Room' object
			room = assign.getRoom();

			if (people.size() > 1) {
				if (room.getRoomSize() == 's') {
					score += -25;
				}
			}
		}
		return score;
	}

	/**
	 * Evaluates a node according to the constraints.
	 * 
	 * @param n : the node to evaluate
	 * @param e : Environment of the search process
	 * @return The score that the node receives
	 */
	public int eval(Node n, Environment e) {
		//if the node breaks a hard constraint it is immediately set to the worst possible score
		int score = 0;
		if (!hardConstraint1(n.StringAssignments, e)) {
			return Integer.MIN_VALUE;
		}
		if (!hardConstraint2(n.Assignments, e)) {
			return Integer.MIN_VALUE;
		}
		if (!hardConstraint3(n.Assignments, e)) {
			return Integer.MIN_VALUE;
		}
		if (!hardConstraint4(n.Assignments, e)) {
			return Integer.MIN_VALUE;
		}
		if(!hardConstraint5(n.StringAssignments,e)){
			return Integer.MIN_VALUE;
		}
		//if no hard constraints are broken then accumulate 
		//the penalties for breaking soft constraints
		score += softConstraint1(n.StringAssignments, e);
		score += softConstraint2(n.StringAssignments, e);
		score += softConstraint3(n.StringAssignments, e);
		score += softConstraint4(n.StringAssignments, e);
		score += softConstraint5(n.StringAssignments, e);
		score += softConstraint6(n.StringAssignments, e);
		score += softConstraint7(n.StringAssignments, e);
		score += softConstraint8(n.StringAssignments, e);
		score += softConstraint9(n.StringAssignments, e);
		score += softConstraint10(n.StringAssignments, e);
		score += softConstraint11(n.Assignments, e);
		score += softConstraint12(n.Assignments, e);
		score += softConstraint13(n.Assignments, e);
		score += softConstraint14(n.Assignments, e);
		score += softConstraint15(n.Assignments, e);
		score += softConstraint16(n.Assignments, e);

		return score;
	}

}
