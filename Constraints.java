package cpsc433;

import java.util.HashSet;
import java.util.LinkedHashMap;

public class Constraints {

	private Environment environment;
	public Node currentNode;

	public Constraints(Node n, Environment env) {
		currentNode = n;
		this.environment = environment;
		if (!hardConstraint1(currentNode.Assignments)) {
			currentNode.score = 0;
			return;
		}
		if (!hardConstraint2(currentNode.Assignments)) {
			currentNode.score = 0;
			return;
		}
		if (!hardConstraint3(currentNode.Assignments)) {
			currentNode.score = 0;
			return;
		}
		if (!hardConstraint4(currentNode.Assignments)) {
			currentNode.score = 0;
			return;
		}
		currentNode.score += softConstraint1(currentNode.Assignments);
		currentNode.score += softConstraint2(currentNode.Assignments);
		currentNode.score += softConstraint3(currentNode.Assignments);
		currentNode.score += softConstraint4(currentNode.Assignments);
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

	public boolean hardConstraint1(LinkedHashMap<String, Assignment> a) {
		return false;

	}

	public boolean hardConstraint2(LinkedHashMap<String, Assignment> a) {
		return false;

	}

	public boolean hardConstraint3(LinkedHashMap<String, Assignment> a) {
		for (Assignment assn : a.values()) {
			if (assn.getPeople().size() > 2) {
				return false;
			}
		}
		return true;

	}

	public boolean hardConstraint4(LinkedHashMap<String, Assignment> a) {
		return false;

	}

	public int softConstraint1(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint2(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint3(LinkedHashMap<String, Assignment> a) {
		return 0;

	}

	public int softConstraint4(LinkedHashMap<String, Assignment> a) {
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
		int score = 0;
		HashSet<Person> checked = new HashSet<Person>();
		// For each assignment
		for (Assignment assn : a.values()) {
			HashSet<Person> People = assn.getPeople();
			// For each person on that assignment
			for (Person per : People) {
				// If a person is a smoker
				if (per.smokes) {
					// Check all others in that room
					for (Person per2 : People) {
						// Have we already accounted for this person?
						if (checked.contains(per2)) {
							continue;
						}
						// I don't check if we're comparing to ourself because
						// this will always fail if we are.
						if (!per2.smokes) {
							// decrement our score
							score -= 50;
						}
					}
				}
				// adding ourselves to checked so we don't double our score
				// unintentionally
				checked.add(per);
			}
		}

		return score;

	}

	public int softConstraint12(LinkedHashMap<String, Assignment> a) {
		int score = 0;
		LinkedHashMap<String, Project> projects = environment.projects;
		Project currProject = null;

		HashSet<Person> checked = new HashSet<Person>();
		// For each assignment
		for (Assignment assn : a.values()) {
			HashSet<Person> People = assn.getPeople();
			// For each person on that assignment
			for (Person per : People) {
				// grab their project and set currProject to it.
				for (Project proj : projects.values()) {
					if (proj.hasMember(per.toString())) {
						currProject = proj;
						break;
					}
				}
				// Check all others in that room
				for (Person per2 : People) {
					// Have we already accounted for this person?
					if (checked.contains(per2)) {
						continue;
					}
					//is this person us?
					if(per2 == per){
						continue;
					}
					Project tempProj = null;
					// grab this new persons project
					for (Project proj : projects.values()) {
						if (proj.hasMember(per2.toString())) {
							tempProj = proj;
							break;
						}
					}
					//and compare projects
					if (tempProj != null && currProject != null && tempProj == currProject) {
						// decrement our score
						score -= 7;
					}
				}
				// adding ourselves to checked so we don't double our score
				// unintentionally
				checked.add(per);
			}
		}

		return score;

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
