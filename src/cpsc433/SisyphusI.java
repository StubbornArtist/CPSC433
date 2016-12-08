package cpsc433;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
	protected Node bestNode = null;

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

	protected Constraints getConstraints() {
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

	protected void createOutputFile(Node n, String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			if (n == null) {
				writer.write("//No solution");
			} else {
				Iterator<String> peopleIt = n.StringAssignments.keySet().iterator();
				while (peopleIt.hasNext()) {
					String person = peopleIt.next();
					String room = n.StringAssignments.get(person);
					writer.write("assign-to(" + person + ", " + room + ")\n");
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
		try {
			long timeLimit = new Long(args[1]).longValue();
			String fileName = args[0] + ".out";

			System.out.println("Performing search for " + timeLimit + "ms");
			try {
				doSearch(env, timeLimit);
				createOutputFile(bestNode, fileName);
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
	 * Perform the actual search. Runs for 90% of the allowed time.
	 * 
	 * @param env
	 *            An Environment object.
	 * @param timeLimit
	 *            A time limit in milliseconds.
	 */
	protected void doSearch(Environment env, long timeLimit) {
		// timer to stop the creation of new generations once time is up
		final Timer timeout = new Timer(false);
		TimerTask killSearch = new TimerTask() {
			@Override
			public void run() {
				search = false;
				this.cancel();
				timeout.cancel();
			}

		};
		timeout.schedule(killSearch, (long) (timeLimit * 0.9));
		if(env.getPeople().isEmpty()){
			return;
		}
		//the size of each generation 
		int GenSize = 200;
		// create the first generation
		Generation currentGen = createFirstGen(env, GenSize);
		//if the current generation is null then no solution was found 
		if(currentGen == null){
			return;
		}
		currentGen.evaluate(env, con);
		bestNode = currentGen.bestNode();
		while (search) {
			// mutate the current generation
			//3 swaps between nodes, 3 swaps between rooms in a single node, 3 room changes in a node
			currentGen.mutate(3, 3, 3, env);
			// evaluate each of the nodes in the generation
			currentGen.evaluate(env, con);
			// cull generations of mutated nodes
			currentGen = cullGeneration(currentGen, GenSize, (float) 0.8, (float) 0.2);

			//test if the current node is better than the best so far
			//if it is then make it the best node overall
			Node curBest = currentGen.bestNode();
				if(curBest.score > bestNode.score){
					bestNode = new Node(curBest);
				}
		}
	}

	private Generation createFirstGen(Environment env, int genSize) {
		Random rand = new Random();
		Generation genOne = new Generation(0);
		// A generation of genSize facts
		for (int i = 0; i < genSize; i++) {
			LinkedHashMap<String, Assignment> assignment = new LinkedHashMap<String, Assignment>();
			LinkedHashMap<String, String> StringAssigns = new LinkedHashMap<String, String>();
			LinkedHashMap<String, Room> rooms = env.getRooms();
			ArrayList<Room> roomList = new ArrayList<Room>();
			HashSet<String> heads = new HashSet<String>();
			HashSet<Person> pepes = new HashSet<Person>();
			
			for(Person p: env.getPeople().values()){
				pepes.add(p);
			}
			for(String head: env.getHeads()){
				heads.add(head);
			}
			for(Room r: rooms.values()){
				roomList.add(r);
			}
			// Assign our hard assignments
			for (String person : env.getAssignments().keySet()) {
				if(roomList.isEmpty()) return null;
				// grab the person
				Person p = env.getPeople().get(person);
				// grab the room
				Room r = env.getRooms().get(env.getAssignments().get(person));
				// create the assignments
				if(assignment.containsKey(r.getRoomNumber())){
					assignment.get(r.getRoomNumber()).addPerson(p);
				}
				else{
					Assignment a = new Assignment(r, p);
					assignment.put(r.getRoomNumber(), a);
				}
				StringAssigns.put(p.name,r.getRoomNumber());
				// if they are a head, remove them from the head list, as well
				// as remove the room from the room list
				if (heads.contains(p.name) || assignment.get(r.getRoomNumber()).size() > 1) {
					roomList.remove(r);
				}
				if (heads.contains(p.name)) {
					heads.remove(p.name);
				}
			}

			// Assign the heads
			for (String p : heads) {
				//if the person has been assigned skip
				if(StringAssigns.containsKey(p)) continue;
				
				//if there are no more rooms this is not a valid solution
				if(roomList.isEmpty()) return null;
				//grab the person that corresponds to the name
				Person headPersonToAdd = env.getPeople().get(p);
				
				// pick a random room
				int roomNumberToUse = rand.nextInt(roomList.size());
				Room roomToUse = roomList.get(roomNumberToUse);
				// if the room has an assignment we can't use it because heads
				// need their own room
				while (assignment.containsKey(roomToUse.getRoomNumber())) {
					roomNumberToUse = rand.nextInt(roomList.size());
					roomToUse = roomList.get(roomNumberToUse);
				}
				// create a new assignment with the head in it
				Assignment a = new Assignment(roomToUse, headPersonToAdd);
				assignment.put(roomToUse.getRoomNumber(), a);
				StringAssigns.put(p, roomToUse.getRoomNumber());
				// remove the room from the room list because we shouldn't put
				// anyone else here
				roomList.remove(roomNumberToUse);
			}

			// Assign everyone else
			for (Person p : pepes) {
				
				//if the person has been assigned skip 
				if(StringAssigns.containsKey(p.name)) continue;
				//if no rooms remain this is not a valid solution (null indicates no solution)
				if(roomList.isEmpty()) return null;
				//pick a random room
				int roomNumberToUse = rand.nextInt(roomList.size());
				Room roomToUse = roomList.get(roomNumberToUse);

				// Find a room that has less than two people in it
				while (assignment.containsKey(roomToUse.getRoomNumber()) && assignment.get(roomToUse.getRoomNumber()).size() > 1) {
					roomNumberToUse = rand.nextInt(roomList.size());
					roomToUse = roomList.get(roomNumberToUse);
				}
				//if the room has a previous assignment in it just ad the new person to it
				//otherwise create a new assignment with the person
				if(assignment.containsKey(roomToUse.getRoomNumber())){
					assignment.get(roomToUse.getRoomNumber()).addPerson(p);
				}
				else{
					assignment.put(roomToUse.getRoomNumber(), new Assignment(roomToUse, p));
				}
				//remove the room as an option once it reaches capacity
				if(assignment.get(roomToUse.getRoomNumber()).size() > 1) {
					roomList.remove(roomNumberToUse);
				}
				// set the assignments in the hashMap
				StringAssigns.put(p.name, roomToUse.getRoomNumber());
			}
			//add each fact to the initial generation
			genOne.addFact(assignment, StringAssigns);
		}
		return genOne;
	}
	/**
	 * A function that takes a generation of nodes with scores and takes returns a new generation with fewer 
	 * nodes. The quality of theses nodes can be chosen.
	 * 
	 * @param gen - the generation you want to decrease in size
	 * @param desiredSize - the size you wish the generation to be 
	 * 						(cannot exceed the original size of the generation)
	 * @param bestPercentage - the percent of nodes you would like to keep that are scored the best
	 * @param worstPercentage - the percent of nodes you would like to keep that have the worst scores
	 * @return	the original generation with the specified number of nodes 
	 */

	public Generation cullGeneration(Generation gen, int desiredSize, float bestPercentage, float worstPercentage) {
		Generation newGen = new Generation(gen.genNumber);

		// The proportion of nodes to grab from both the best nodes and
		// the worst nodes
		float desirePercentage = (float) desiredSize / gen.size();

		//retrieve the nodes best and worst nodes
		Node[] bestNodes = gen.getBestOfGen(bestPercentage);
		Node[] worstNodes = gen.getWorstOfGen(worstPercentage);
		//determine the number of the best nodes to keep
		int counter = Math.round(desirePercentage * bestNodes.length);
		//determine the number of the worst nodes to keep
		int counter2 = Math.round(desirePercentage * worstNodes.length);
		//cannot grab more nodes than what exists
		if (counter > bestNodes.length) {
			counter = bestNodes.length;
		}
		if (counter2 > worstNodes.length) {
			counter2 = worstNodes.length;
		}
		//grab specified percentage of best and worst
		for (int i = 0; i < counter; i++) {
			newGen.addFact(new Node(bestNodes[i]));
		}
		for (int i = 0; i < counter2; i++) {
			newGen.addFact(new Node(worstNodes[i]));
		}
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
