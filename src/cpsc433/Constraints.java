package cpsc433;
import java.util.LinkedHashMap;
import java.util.Iterator;

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

	public boolean hardConstraint2(LinkedHashMap<String, Assignment> a, Environment e) {
		return true;

	}

	public boolean hardConstraint3(LinkedHashMap<String, Assignment> a, Environment e) {
		return true;

	}

	public boolean hardConstraint4(LinkedHashMap<String, Assignment> a, Environment e) {
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
					if(!e.e_close(a.get(head), a.get(members.next()))){
						score+= -2;
					}	
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

	public int softConstraint5(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint6(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint7(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint8(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

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

	public int softConstraint13(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint14(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint15(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}

	public int softConstraint16(LinkedHashMap<String, Assignment> a, Environment e) {
		return 0;

	}
	
	public int eval(Node n, Environment e){
		int score = 1000;
		if (!hardConstraint1(n.StringAssignments, e)){
			return 0;
		}
		if (!hardConstraint2(n.Assignments, e)){
			return 0;
		}
		if (!hardConstraint3(n.Assignments, e)){
			return 0;
		}
		if (!hardConstraint4(n.Assignments, e)){
			return 0;
		}
		score += softConstraint1(n.StringAssignments,e);
		score += softConstraint2(n.StringAssignments,e);
		score += softConstraint3(n.StringAssignments,e);
		score += softConstraint4(n.StringAssignments,e);
		score += softConstraint5(n.Assignments,e);
		score += softConstraint6(n.Assignments,e);
		score += softConstraint7(n.Assignments,e);
		score += softConstraint8(n.Assignments,e);
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
