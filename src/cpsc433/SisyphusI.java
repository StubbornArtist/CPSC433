package cpsc433;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

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
			if(args[1].equals("-1")){
				System.out.println("Creating output file");
				env.createOutputFile(fromFile + ".out");
			}
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
	
	protected void createOutputFile(Generation g, String fileName){
		try {
			FileWriter writer = new FileWriter(fileName);
			Node solution = g.bestNode();
			Iterator<String> peopleIt = solution.StringAssignments.keySet().iterator();
			while(peopleIt.hasNext()){
				String person = peopleIt.next();
				String room = solution.StringAssignments.get(person);
				writer.write("assign-to(" + person + ", " + room + ")\n" );
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("IO WRITER ERROR OCCURED");
		}
		
	}

	/**
	 * Run in "Command line mode", that is, batch mode.
	 */
	protected void runCommandLineMode() {
		try {
			long timeLimit = new Long(args[1]).longValue();
			System.out.println("Performing search for " + timeLimit + "ms");
			try {
				doSearch(env, timeLimit);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException ex) {
			System.out.println("Error: The 2nd argument must be a long integer.");
			printSynopsis();
			System.exit(-1);
		}
		printResults();
	}

	/**
	 * Perform the actual search
	 * 
	 * @param env
	 *            An Environment object.
	 * @param timeLimit
	 *            A time limit in milliseconds.
	 */
	protected void doSearch(Environment env, long timeLimit) {
		Generation one = createFirstGen(env, 2);
		Iterator<Node> nodes = one.facts.iterator();
		while(nodes.hasNext()){
			Node n = nodes.next();
			n.score = con.eval(n, env);
			n.mutate();
		}
		createOutputFile(one, "solution.out");
	}

	private Generation createFirstGen(Environment env, int genSize) {
		Random rand = new Random();
		Generation genOne = new Generation(0);
		// A generation of genSize facts
		for (int i = 0; i < genSize; i++) {
			LinkedHashMap<String, Assignment> assignment = new LinkedHashMap<String, Assignment>();
			LinkedHashMap<String, String> StringAssigns = new LinkedHashMap<String, String>();
			Iterator<Person> people = env.getPeople().values().iterator();
			LinkedHashMap<String, Room> rooms = env.getRooms();
			ArrayList<Room> roomList = new ArrayList<Room>(rooms.values());

			// Assign each person a random room
			while(people.hasNext()){
				Person personVal = people.next();
				// the room
				Room roomVal;
				//Is this person hard-assigned a room?
				//If they are, the roomVal becomes the room that they are assigned to
				//no means we assign them the random room	
				if (env.assignments.containsKey(personVal)){
					roomVal = rooms.get(env.assignments.get(personVal));
				}else{
					 roomVal = roomList.get(rand.nextInt(roomList.size()));
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
			genOne.addFact(assignment,StringAssigns);
		}
		return genOne;
	}

	protected void printResults() {
		System.out.println("Would print results here, but the search isn't implemented yet.");
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
