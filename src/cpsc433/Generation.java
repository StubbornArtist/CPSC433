package cpsc433;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

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

	public Node bestNode(){
		Iterator<Node> nodes = facts.iterator();
		Node maxNode = null;
		int maxScore = 0;
		
		while(nodes.hasNext()){
			Node n = nodes.next();
			if(n.score > maxScore){
				maxScore = n.score;
				maxNode = n;
			}	
		}
		return maxNode;
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
		String gen = "Generation" + genNumber + "\n{";
		while(nodes.hasNext()){
			gen += nodes.next();
			if(nodes.hasNext()) gen+=",\n";
		}
		gen+="}";
		return gen;
	}
			
}
