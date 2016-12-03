package cpsc433;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

public class Generation {
	
	private int genNumber;
	public HashSet<Node> facts;
	private LinkedHashMap<Integer, Node> factsIndexed;

	
	public Generation(int number){
		this.genNumber = number;
		this.facts = new HashSet<Node>();
		this.factsIndexed = new LinkedHashMap<Integer, Node>();
	}
	
	public void addFact(LinkedHashMap<String, Assignment> a, LinkedHashMap<String, String> b){
		Node nodeN = new Node(a);
		nodeN.putStrings(b);
		this.facts.add(nodeN);
		this.factsIndexed.put(factsIndexed.size() , nodeN);
	}
	
	public void addFact(Node n){
		this.facts.add(n);
		this.factsIndexed.put(factsIndexed.size() , n);
	}
	
	public Node factAt(int index){
		return factsIndexed.get(index);
	}
		
	public void mutate(int numGenSwap, int numFactSwap, int numChange, Environment e){
		Random rand = new Random();
		for(int i = 0; i < numGenSwap; i++){
			this.swap(e);
			System.out.println(this);
		}
		for(int i = 0; i < numFactSwap; i++){
			addFact(factAt(rand.nextInt(factsIndexed.size())).changeRooms(e));
			System.out.println(this);
		}
		for(int i = 0; i < numChange; i++){
			addFact(factAt(rand.nextInt(factsIndexed.size())).swapRooms(e));
			System.out.println(this);
		}
		genNumber++;
	}
	
	public void swap(Environment e){
		Random rand = new Random();
		int num1 = rand.nextInt(factsIndexed.size());
		int num2;
		do{
			num2 = rand.nextInt(factsIndexed.size());
		}while(num2 == num1);
		
		Node n1 = factAt(num2);
		Node n2 = factAt(num1);
		Person p;
		Assignment a1;
		Room room;
		
		//do{
			a1 = n1.randomAssignment();
			p = a1.randomPerson();
			room = n2.getRoom(p);	
			
		//}while(e.getAssignments().containsKey(p1.name) || e.getAssignments().containsKey(p2.name));
		
		n1.remove(p, a1.getRoom());
		n1.put(p, room);
		
		n2.remove(p, room);
		n2.put(p, a1.getRoom());		
	}
	
	public Node bestFact(){
		Iterator<Node> nodes = facts.iterator();
		Node maxNode = null;
		int maxScore = Integer.MIN_VALUE;
		
		while(nodes.hasNext()){
			Node n = nodes.next();
			if(n.score > maxScore){
				maxScore = n.score;
				maxNode = n;
			}	
		}
		return maxNode;
	}
			
	@Override
	public String toString(){
		Iterator<Node> nodes = facts.iterator();
		String gen = "Generation " + genNumber + "\n{";
		while(nodes.hasNext()){
			gen += nodes.next();
			if(nodes.hasNext()) gen+=",\n";
		}
		gen+="}";
		return gen;
	}
	
			
}
