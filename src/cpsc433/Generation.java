package cpsc433;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
/**
 * This is a class that represents a generation. Each generation has a set 
 * of nodes/facts and a number to identify it.
 * 
 * @author Ashley Currie, Cooper Davies, Edraelan Ayuban, Erica Aguete
 *
 */

public class Generation {
	
	public int genNumber;
	public HashSet<Node> facts;
	
	public Generation(int number){
		this.genNumber = number;
		this.facts = new HashSet<Node>();
	}
	/**
	 * Adds a fact to the generation using the internals of a node
	 * @param a 
	 * 			room to list of people representation
	 * @param b
	 * 			person to room representation
	 */
	public void addFact(LinkedHashMap<String, Assignment> a, LinkedHashMap<String, String> b){
		Node nodeN = new Node(a);
		nodeN.putStrings(b);
		this.facts.add(nodeN);
	}

	/**
	 * Adds a fact to the generation
	 * @param n
	 * 			the fact to add
	 */
	public void addFact(Node n){
		this.facts.add(n);
	}
	/**
	 * Remove a fact from the generation
	 * @param n
	 * 			the fact to be removed
	 */
	public void removeFact(Node n){
		this.facts.remove(n);
	}
	
	/**
	 * Change the facts in a generation
	 * @param set
	 * 			the list of new facts
	 */
	public void addFact(HashSet<Node> set){
		this.facts = set;
	}
	
	/**
	 * Get the fact at some index
	 * @param index
	 * 				the number of the fact you want to retrieve
	 * @return
	 * 			returns the fact at the specified index 
	 * or null if the index is out of bounds
	 */
	public Node factAt(int index){
		Iterator<Node> nodes = facts.iterator();
		int count = 0;
		//iterate through all nodes until you reach the 
		//number you are looking for
		while(nodes.hasNext()){
			if(count == index){
				return nodes.next();
			}
			nodes.next();
			count++;
		}
		return null;
	}
	/**
	 * Get the size of the generation (number of facts in the generation)
	 * @return
	 * 			number of facts
	 */
	public int size(){
		return facts.size();
	}
	/**
	 * Mutate the current generation
	 * @param numGenSwap
	 * 					the number of swaps to do between facts in the generation
	 * @param numFactSwap
	 * 					the number of room swaps to do within a fact
	 * @param numChange
	 * 					the number of room changes to do within a fact
	 * @param e
	 * 			an instance of the environment 
	 */
	
	public void mutate(int numGenSwap, int numFactSwap, int numChange, Environment e){
		Random rand = new Random();
		//do specified number of swaps between facts
		for(int i = 0; i < numGenSwap; i++){
			this.swap(e);
		}
		//do the specified number of room swaps in a single fact
		//these nodes are randomly chosen
		for(int i = 0; i < numFactSwap; i++){
			addFact(factAt(rand.nextInt(size())).changeRooms(e));
		}
		//do the specified number of room changes in a single fact
		//these nodes are also randomly chosen
		for(int i = 0; i < numChange; i++){
			addFact(factAt(rand.nextInt(size())).swapRooms(e));
		}
		//increase the generation number
		genNumber++;
	}
	/**
	 * Perform a swap within this generation between two randomly chosen facts.
	 * The swap is performed by picking a random person in one fact, finding where 
	 * that person is in the second fact, and then putting them back into both facts 
	 * in the room that they were located in the other fact.
	 * @param e
	 * 			an instance of the environment
	 */
	public void swap(Environment e){
		Random rand = new Random();
		//pick two random fact indices
		int num1 = rand.nextInt(size());
		int num2;
		//keep trying to find a second fact index
		//that is not the first fact's index
		do{
			num2 = rand.nextInt(size());
		}while(num2 == num1);
		
		//grab the nodes at the random indices
		Node n1 = new Node(factAt(num2));
		Node n2 = new Node(factAt(num1));
		Person p;
		Assignment a1;
		Room room;
		//pick a random room
		a1 = n1.peopleAt(rand.nextInt(n1.numRooms()));
		//pick a random person from that room
		p = a1.randomPerson();
		//find the location of that same person in the other fact
		room = n2.getRoom(p);
		
		//perform the swap
		n1.remove(p, a1.getRoom());
		n1.put(p, room);
		
		n2.remove(p, room);
		n2.put(p, a1.getRoom());
		
		//add these new facts to the generation
		addFact(n1);
		addFact(n2);
	}

	/**
	 * Gives the fact with the best score in the generation
	 * @return
	 * 			the best fact
	 */
	public Node bestNode(){
		Iterator<Node> nodes = facts.iterator();
		Node maxNode = nodes.next(); 
		while(nodes.hasNext()){
			Node n = nodes.next();
			if(n.score > maxNode.score){
				maxNode = n;
			}	
		}
		return maxNode;
	}
	/**
	 * Test all soft and hard constraints on each node of this generation.
	 * Save the scores for each node.
	 * @param e
	 * 			an instance of the environment for evaluation	
	 * @param c
	 * 			an instance of the constraints 
	 */
	public void evaluate(Environment e, Constraints c){
		Iterator<Node> nodes = facts.iterator();
		while(nodes.hasNext()){
			Node n = nodes.next();
			n.setScore(c.eval(n, e));
		}
	}
		
	/**
	 * A function that returns an Array filled with facts that are the top
	 * percentile of the generation
	 *
	 * @param	percentage	The portion of the best facts in a generation
	 *						you want returned in the array
	 *
	 * @return	best		The array containing the best fact
	 */
	public Node[] getBestOfGen(float percentage){
		Node[] best = new Node[Math.round(facts.size() * percentage)];
		
		Node[] sorted = sortGen();
		
		int j = facts.size() - 1;
		for (int i = 0; i < best.length; i++){
			best[i] = new Node(sorted[j]);
			j--;
		}
		
		return best;
	}
	/**
	 * A function that returns an Array filled with facts that are the bottom
	 * percentile of the generation
	 *
	 * @param	percentage	The portion of the worst facts in a generation
	 *						you want returned in the array
	 *
	 * @return	best		The array containing the worst facts
	 */
		
	public Node[] getWorstOfGen(float percentage){
		Node[] worst = new Node[Math.round(facts.size() * percentage)];
		
		Node[] sorted = sortGen();
		
		for (int i =0; i < worst.length; i++){
			worst[i] = new Node(sorted[i]);
		}
		
		return worst;
	}
	/**
	 * Sort the generation from worst to best
	 * @return
	 * 		the array of sorted facts
	 */
	private Node[] sortGen(){
		Node[] nArray = new Node[facts.size()];
		Object[] oArray = facts.toArray();
		
		for (int i = 0; i < oArray.length; i++){
			nArray[i] = (Node) oArray[i];
		}
		//perform a quick sort on the nodes
		NodeQuickSort.sort(nArray, 0, nArray.length - 1);
		return nArray;
	}
	
	/**
	 * Representation of the generation with room to list of people 
	 */
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
	/**
	 * Representation of the generation with person to room pairs
	 * @return
	 */
	public String altToString(){
		Iterator<Node> nodes = facts.iterator();
		String gen = "Generation " + genNumber + "\n{";
		while(nodes.hasNext()){
			gen += nodes.next().altToString();
			if(nodes.hasNext()) gen+=",\n";
		}
		gen+="}";
		return gen;
		
	}
			
}
