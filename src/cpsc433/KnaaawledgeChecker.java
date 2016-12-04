package cpsc433;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class KnaaawledgeChecker {

	public KnaaawledgeChecker() {

	}

	public boolean checkTwoToARoom(Assignment a) {
		if (a.getPeople().size() >= 3) {
			return false;
		}
		return true;
	}

	public boolean checkManager(Assignment a, Environment env) {
		Iterator<Person> it = null;
		Person person = null;
		HashSet<Person> people = a.getPeople();

		// because we check this, by default if one of them is a manager, we
		// KNOW it's going to fail
		if (people.size() > 1) {
			it = people.iterator();
			while (it.hasNext()) {
				person = (Person) it.next();
				// Check if the current person is a manager
				if (env.e_manager(person.name)) {
					return false;
				}

				// Check if this person is a group head
				for (Map.Entry<String, Group> group : env.getGroups().entrySet()) {
					Group g = group.getValue();
					if (env.e_heads_group(person.name, g.getName())) {
						return false;
					}
				}

				// Check if this person is a project head
				for (Map.Entry<String, Project> project : env.projects.entrySet()) {
					Project p = project.getValue();
					if (env.e_heads_project(person.name, p.getName())) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
