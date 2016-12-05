package cpsc433;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the main class for the SysiphusI assignment. It's main function is to
 * interpret the command line.
 * 
 * <p>
 * Copyright: Copyright (c) 2003-16, Department of Computer Science, University
 * of Calgary. Permission to use, copy, modify, distribute and sell this
 * software and its documentation for any purpose is hereby granted without fee,
 * provided that the above copyright notice appear in all copies and that both
 * that copyright notice and this permission notice appear in supporting
 * documentation. The Department of Computer Science makes no representations
 * about the suitability of this software for any purpose. It is provided "as
 * is" without express or implied warranty.
 * </p>
 *
 * @author <a href="http://www.cpsc.ucalgary.ca/~kremer/">Rob Kremer</a>
 *
 */
public class SisyphusI {
	/**
	 * Merely create a new SisypyusI object, and let the constructor run the
	 * program.
	 * 
	 * @param args
	 *            The command line argument list
	 */
	public static void main(String[] args) {
		new SisyphusI(args);
	}

	protected final String[] args;
	protected Environment env;
	protected Constraints con;
	public boolean search = true;
	//private Node bestNode = null;

	public SisyphusI(String[] args) {
		this.args = args;
		run();
	}

	protected void run() {
		env = getEnvironment();
		con = getConstraints();

		String fromFile = null;

		if (args.length > 0) {
			fromFile = args[0];
			env.fromFile(fromFile);
		} else {
			printSynopsis();
		}
		createShutdownHook();

		if (args.length > 1) { // using command-line arguments
			runCommandLineMode();
			killShutdownHook();
		} else { // using interactive mode
			runInteractiveMode();
			killShutdownHook();
		}
	}

	/**
	 * Return the environment object. One should return an environment object
	 * that makes sense for YOUR solution to the problem: the environment could
	 * contain all the object instances required for the domain (like people,
	 * rooms, etc), as well as potential solutions and partial solutions.
	 * 
	 * @return The global environment object.
	 */
	protected Environment getEnvironment() {
		return Environment.get();
	}
	protected Constraints getConstraints(){
		return Constraints.getInstance();
	}

	protected void printSynopsis() {
		System.out.println("Synopsis: SisyphusI [<env-file> [<time-in-ms>]]");
	}

	/**
	 * If you want to install a shutdown hook, you can do that here. A shutdown
	 * hook is completely optional, but can be useful if you search doesn't exit
	 * in a timely manner.
	 */
	
	protected void createShutdownHook() {
	}

	protected void killShutdownHook() {
	}
	
	protected void createOutputFile(Node n, String fileName){
		try {
			FileWriter writer = new FileWriter(fileName);
			if(n.score == -1){
				writer.write("No solution");
			}
			else{
				Iterator<String> peopleIt = n.StringAssignments.keySet().iterator();
				while(peopleIt.hasNext()){
					String person = peopleIt.next();
					String room = n.StringAssignments.get(person);
					writer.write("assign-to(" + person + ", " + room + ")\n" );
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("IO WRITER ERROR OCCURED" + e.getMessage());
		}
		
	}

	/**
	 * Run in "Command line mode", that is, batch mode.
	 */
	protected void runCommandLineMode() {
		Node bestNode;
		try {
			long timeLimit = new Long(args[1]).longValue();
			String fileName = args[0] + ".out";
			
			System.out.println("Performing search for " + timeLimit + "ms");
			try {
				bestNode = doSearch(env, timeLimit);
				createOutputFile(bestNode, fileName);
				System.out.println(bestNode);
				System.out.println(bestNode.score);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException ex) {
			System.out.println("Error: The 2nd argument must be a long integer.");
			printSynopsis();
			System.exit(-1);
		}
	}

	/**
	 * Perform the actual search
	 * 
	 * @param env
	 *            An Environment object.
	 * @param timeLimit
	 *            A time limit in milliseconds.
	 */
	protected Node doSearch(Environment env, long timeLimit) {
		Node bestNode = null;
		//timer to stop the creation of new generations once time is up
		final Timer timeout = new Timer(false);
		TimerTask killSearch = new TimerTask(){
			@Override
			public void run() {
				search = false;
				this.cancel();
				timeout.cancel();
			}
		};
		timeout.schedule(killSearch, (long) (timeLimit * 0.9));
		int GenSize = 500;
		//create the first generation
		Generation currentGen = createFirstGen(env, GenSize);
		while(search){
			//mutate generations with currentGen as input
			currentGen.mutate( 1,1,1, env);
			//evaluate each of the nodes in the generation
			currentGen.evaluate(env, con);
			//cull generations of mutated nodes
			currentGen = cullGeneration(currentGen, GenSize, (float) 0.8, (float) 0.2);
			bestNode = currentGen.bestNode();
			//System.out.println(currentGen);
		}	
		
		//retrieve the best of the nodes in the current generation
		return bestNode;
	}
	
	private Generation createFirstGen(Environment env, int genSize) {
		Random rand = new Random();
		Generation genOne = new Generation(0);
		// A generation of genSize facts
		for (int i = 0; i < genSize; i++) {
			LinkedHashMap<String, Assignment> assignment = new LinkedHashMap<String, Assignment>();
			LinkedHashMap<String, String> StringAssigns = new LinkedHashMap<String, String>();
			Iterator<Person> people = env.getPeople().values().iterator();
			Iterator<String> assigned = env.getAssignments().keySet().iterator();
			LinkedHashMap<String, Room> rooms = env.getRooms();
			ArrayList<Room> roomList = new ArrayList<Room>(rooms.values());
			
			while(assigned.hasNext()){
				Person person = env.getPeople().get(assigned.next());
				Room room = env.getRooms().get(env.getAssignments().get(person.name));
				if(assignment.containsKey(room.getRoomNumber())){
					assignment.get(room.getRoomNumber()).addPerson(person);
				}
				else{
					assignment.put(room.getRoomNumber(), new Assignment(room, person));
				}
				StringAssigns.put(person.name, room.getRoomNumber());
			}
			for(String h : env.getHeads()){
				Person p = env.getPeople().get(h);
				Room roomVal;
				if(StringAssigns.containsKey(h)) continue;
				do{
					roomVal = roomList.get(rand.nextInt(roomList.size()));
				}while(assignment.containsKey(roomVal.getRoomNumber()));

				assignment.put(roomVal.getRoomNumber(), new Assignment(roomVal, p));
				StringAssigns.put(p.name, roomVal.getRoomNumber());
			}
			// Assign each person a random room
			while(people.hasNext()){
				Person personVal = people.next();
				//Is this person hard-assigned a room?
				//If they are, the roomVal becomes the room that they are assigned to
				//no means we assign them the random room
				if(StringAssigns.containsKey(personVal.name)) continue;
				Room roomVal;
				Assignment a;
				do{
					roomVal = roomList.get(rand.nextInt(roomList.size()));
				
				}while(assignment.containsKey(roomVal.getRoomNumber()) && 
						assignment.get(roomVal.getRoomNumber()).size() > 1);
				// The keys are the room numbers
				if (assignment.containsKey(roomVal.getRoomNumber())) {
					assignment.get(roomVal.getRoomNumber()).addPerson(personVal);
					StringAssigns.put(personVal.name, roomVal.getRoomNumber());
				} else {
					assignment.put(roomVal.getRoomNumber(), new Assignment(roomVal, personVal));
					StringAssigns.put(personVal.name, roomVal.getRoomNumber());	
				}
			}
			genOne.addFact(assignment,StringAssigns);
		}
		return genOne;
	}

	public Generation cullGeneration(Generation gen, int desiredSize, float bestPercentage, float worstPercentage){
		Random rand = new Random();
		Generation newGen = new Generation(gen.genNumber);
		
		// The proportion of nodes to grab from both the best nodes and
		// the worst nodes
		float desirePercentage = (float) desiredSize / gen.size();
		
		Node[] bestNodes = gen.getBestOfGen(bestPercentage);
		Node[] worstNodes = gen.getWorstOfGen(worstPercentage);
		int counter = Math.round(desirePercentage * (bestNodes.length -1));
		int counter2 = Math.round(desirePercentage * (worstNodes.length -1));
		if(counter > bestNodes.length){
			counter = bestNodes.length -1;
		}
		if(counter2 > worstNodes.length){
			counter2 = worstNodes.length -1;
		}
		for(int i = 0; i < counter ; i++){
			newGen.addFact(bestNodes[i]);
		}
		for(int i = 0; i < counter2; i++){
			newGen.addFact(worstNodes[i]);
		}
		/*
		// Remove all nodes from the new generation that have a score of 0
		for(Node n : newGen.facts){
			if (n.score < 0){
				newGen.removeFact(n);
			}
		}
		
		// Pad the new generation with new nodes if we have yet to hit the desired size
		for (int i = newGen.size(); i < desiredSize; i++) {
			LinkedHashMap<String, Assignment> assignment = new LinkedHashMap<String, Assignment>();
			LinkedHashMap<String, String> StringAssigns = new LinkedHashMap<String, String>();
			Iterator<Person> people = env.getPeople().values().iterator();
			LinkedHashMap<String, Room> rooms = env.getRooms();
			ArrayList<Room> roomList = new ArrayList<Room>(rooms.values());

			// Assign each person a random room
			while(people.hasNext()){
				Person personVal = people.next();
				Room roomVal;
				//Is this person hard-assigned a room?
				//If they are, the roomVal becomes the room that they are assigned to
				//no means we assign them the random room	
				if (env.assignments.containsKey(personVal)){
					roomVal = rooms.get(env.assignments.get(personVal));
				}else{
					do{
						roomVal = roomList.get(rand.nextInt(roomList.size()));
					}while(assignment.containsKey(roomVal.getRoomNumber()) 
							&& assignment.get(roomVal.getRoomNumber()).size()== 2);
				}	
				// The keys are the room numbers
				if (assignment.containsKey(roomVal.getRoomNumber())) {
					assignment.get(roomVal.getRoomNumber()).addPerson(personVal);
					StringAssigns.put(personVal.name, roomVal.getRoomNumber());
				} else {
					assignment.put(roomVal.getRoomNumber(), new Assignment(roomVal, personVal));
					StringAssigns.put(personVal.name, roomVal.getRoomNumber());	
				}
			}
			newGen.addFact(assignment,StringAssigns);
		}
		*/
		return newGen;
	}
		
	protected void runInteractiveMode() {
		final int maxBuf = 200;
		byte[] buf = new byte[maxBuf];
		int length;
		try {
			System.out.print(
					"\nSisyphus I: query using predicates, assert using \"!\" prefixing predicates;\n !exit() to quit; !help() for help.\n\n> ");
			while ((length = System.in.read(buf)) != -1) {
				String s = new String(buf, 0, length);
				s = s.trim();
				if (s.equals("exit"))
					break;
				if (s.equals("?") || s.equals("help")) {
					s = "!help()";
					System.out.println("> !help()");
				}
				if (s.length() > 0) {
					if (s.charAt(0) == '!')
						env.assert_(s.substring(1));
					else
						System.out.print(" --> " + env.eval(s));
				}
				System.out.print("\n> ");
			}
		} catch (Exception e) {
			System.err.println("exiting: " + e.toString());
		}
	}
}
