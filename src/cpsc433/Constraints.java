package cpsc433;

import java.util.LinkedHashMap;

public class Constraints {

	public Node currentNode;
	public Constraints(Node n){
		currentNode = n;
		if (!hardConstraint1(currentNode.Assignments)){
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
		return false;

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
