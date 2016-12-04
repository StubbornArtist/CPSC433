package cpsc433;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

public class Generation {
	
	public int genNumber;
	public HashSet<Node> facts;
	
	public Generation(int number){
		this.genNumber = number;
		this.facts = new HashSet<Node>();
	}
	
	public void addFact(LinkedHashMap<String, Assignment> a, LinkedHashMap<String, String> b){
		Node nodeN = new Node(a);
		nodeN.putStrings(b);
		this.facts.add(nodeN);
	}

	public void addFact(Node n){
		this.facts.add(n);
	}
	
	public Node factAt(int index){
		Iterator<Node> nodes = facts.iterator();
		int count = 0;
		while(nodes.hasNext()){
			if(count == index){
				return nodes.next();
			}
			nodes.next();
			count++;
		}
		return null;
	}
	public int size(){
		return facts.size();
	}
	
	public void mutate(int numGenSwap, int numFactSwap, int numChange, Environment e, Constraints c){
		Random rand = new Random();
		for(int i = 0; i < numGenSwap; i++){
			this.swap(e,c);
		}
		for(int i = 0; i < numFactSwap; i++){
			addFact(factAt(rand.nextInt(size())).changeRooms(e,c));
		}
		for(int i = 0; i < numChange; i++){
			addFact(factAt(rand.nextInt(size())).swapRooms(e));
		}
		genNumber++;
	}
	
	public void swap(Environment e, Constraints c){
		Random rand = new Random();
		int num1 = rand.nextInt(size());
		int num2;
		do{
			num2 = rand.nextInt(size());
		}while(num2 == num1);
		
		Node n1 = factAt(num2);
		Node n2 = factAt(num1);
		Person p;
		Assignment a1;
		Assignment a2;
		Room room;
		do{
			a1 = n1.peopleAt(rand.nextInt(n1.numRooms()));
			p = a1.randomPerson();
			room = n2.getRoom(p);	
			a2 = n2.getAssignment(room.getRoomNumber());
		}while(!c.lessThanTwoARoom(a1) || !c.lessThanTwoARoom(a2));
	
		n1.remove(p, a1.getRoom());
		n1.put(p, room);
		
		n2.remove(p, room);
		n2.put(p, a1.getRoom());		
	}

	public Node bestNode(){
		Iterator<Node> nodes = facts.iterator();
		Node maxNode = null;
		int maxScore = Integer.MIN_VALUE;
		
		while(nodes.hasNext()){
			Node n = nodes.next();
			if(n.score > maxScore && !(n.score == 0)){
				maxScore = n.score;
				maxNode = n;
			}	
		}
		return maxNode;
	}
	
	public void evaluate(Environment e, Constraints c){
		Iterator<Node> nodes = facts.iterator();
		while(nodes.hasNext()){
			Node n = nodes.next();
			n.setScore(c.eval(n, e));	
		}
	}
		
	/**
	 * A function that returns an Array filled with Nodes that are the top
	 * percentile of the generation
	 *
	 * @param	percentage	The portion of the best nodes in a generation
	 *						you want returned in the array
	 *
	 * @return	best		The array containing 
	 */
	public Node[] getBestOfGen(float percentage){
		Node[] best = new Node[Math.round(facts.size() * percentage)];
		
		Node[] sorted = sortGen();
		
		int j = facts.size() - 1;
		for (int i =0; i < best.length; i++){
			best[i] = sorted[j];
			j--;
		}
		
		return best;
	}
		
	public Node[] getWorstOfGen(float percentage){
		Node[] worst = new Node[Math.round(facts.size() * percentage)];
		
		Node[] sorted = sortGen();
		
		for (int i =0; i < worst.length; i++){
			worst[i] = sorted[i];
		}
		
		return worst;
	}
	
	private Node[] sortGen(){
		Node[] nArray = new Node[facts.size()];
		Object[] oArray = facts.toArray();
		
		for (int i = 0; i < oArray.length; i++){
			nArray[i] = (Node) oArray[i];
		}
		
		NodeQuickSort.sort(nArray, 0, nArray.length - 1);
				
		return nArray;
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
