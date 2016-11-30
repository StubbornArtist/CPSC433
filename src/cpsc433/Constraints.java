package cpsc433;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Constraints {

	public Node currentNode;
	public Constraints(Node n, Environment e){
		currentNode = n;
		if (!hardConstraint1(currentNode.StringAssignments, e)){
			currentNode.score = 0;
			return;
		}
		if (!hardConstraint2(currentNode.Assignments)){
			currentNode.score = 0;
			return;
		}
		if (!hardConstraint3(currentNode.Assignments)){
			currentNode.score = 0;
			return;
		}
		if (!hardConstraint4(currentNode.Assignments)){
			currentNode.score = 0;
			return;
		}
		currentNode.score += softConstraint1(currentNode.StringAssignments,e);
		currentNode.score += softConstraint2(currentNode.StringAssignments,e);
		currentNode.score += softConstraint3(currentNode.StringAssignments,e);
		currentNode.score += softConstraint4(currentNode.StringAssignments,e);
		currentNode.score += softConstraint5(currentNode.Assignments);
		currentNode.score += softConstraint6(currentNode.Assignments);
		currentNode.score += softConstraint7(currentNode.Assignments);
		currentNode.score += softConstraint8(currentNode.Assignments);
		currentNode.score += softConstraint9(currentNode.Assignments);
		currentNode.score += softConstraint10(currentNode.Assignments);
		currentNode.score += softConstraint11(currentNode.Assignments);
		currentNode.score += softConstraint12(currentNode.Assignments);
		currentNode.score += softConstraint13(currentNode.Assignments);
		currentNode.score += softConstraint14(currentNode.Assignments);
		currentNode.score += softConstraint15(currentNode.Assignments);
		currentNode.score += softConstraint16(currentNode.Assignments);
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

	public boolean hardConstraint2(LinkedHashMap<String, Assignment> a) {
		return false;

	}

	public boolean hardConstraint3(LinkedHashMap<String, Assignment> a) {
		return false;

	}

	public boolean hardConstraint4(LinkedHashMap<String, Assignment> a) {
		return false;

	}

	public int softConstraint1(LinkedHashMap<String, String> a, Environment e) {
		boolean allLarge = true;
		Iterator<String> heads;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();
			while(groupsIt.hasNext()){
				heads = groups.get(groupsIt.next()).getHeadIterator();
				while(heads.hasNext()){
					if(!e.e_large_room(a.get(heads.next()))){
						allLarge = false;
						continue;
					}	
				}
			}	
			if(allLarge){
				return 0;
			}
		return -40;
	}

	public int softConstraint2(LinkedHashMap<String, String> a, Environment e) {
		boolean allClose = true;
		Iterator<String> heads;
		Iterator<String> members;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();
		
		while(groupsIt.hasNext()){
			Group g = groups.get(groupsIt.next());
			heads = g.getHeadIterator();
			members = g.membersIterator();
			while(heads.hasNext()){
				while(members.hasNext()){
					if(!e.e_close(a.get(heads.next()), a.get(members.next()))){
						allClose = false;
						continue;
					}
					
				}
			}
		}
		if(allClose){
			return 0;
		}
		return -2;

	}

	public int softConstraint3(LinkedHashMap<String, String> a, Environment e) {
		Iterator<String> heads;
		Iterator<String> members;
		LinkedHashMap<String, Group> groups = e.getGroups();
		Iterator<String> groupsIt = groups.keySet().iterator();
		
		while(groupsIt.hasNext()){
			Group g = groups.get(groupsIt.next());
			heads = g.getHeadIterator();
			members = g.membersIterator();
			while(heads.hasNext()){
				while(members.hasNext()){
					String mem = members.next();
					if(e.e_close(a.get(heads.next()), a.get(mem)) && e.e_secretary(mem)){
						return 0;
					}
					
				}
			}
		}
		return -30;
	}

	public int softConstraint4(LinkedHashMap<String, String> a, Environment e) {
		Iterator<String> people = a.keySet().iterator();
		Iterator<String> coworkers;
		while(people.hasNext()){
			String person = people.next();
			if(e.e_secretary(person)){
				coworkers = a.keySet().iterator();
				while(coworkers.hasNext()){
					String coworker = coworkers.next();
					if(a.get(person).equals(a.get(coworker)) && !e.e_secretary(coworker)){
						
						return -5;
					}
				}
			}
		}
		return 0;
	}

	public int softConstraint5(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint6(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint7(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint8(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint9(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint10(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint11(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint12(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint13(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint14(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint15(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint16(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

}
