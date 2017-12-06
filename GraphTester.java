


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;
import java.text.*;


//------------------------------------------------------
public class GraphTester
{
	public static Scanner userScanner = new Scanner(System.in);

	// -------  main --------------
	public static void main(String[] args)
	{
		runProgram();
	}
	/**	Initiates a while loop that keeps a main menu and sub menus open.
	 * User can respond through input numbers
	 *	@author	Jeremy Lee
	 */
	public static void runProgram(){
		boolean running = true;
		int res = -1;
		// Edge last = null; TODO: Removed by Ali because wasn't used and Edge class deleted
		Fleur<String> graph = new Fleur<>();
		StackInterface<String> removed = new LinkedStack<>();
		while(running){
			outputMainMenu();
			res = getInteger(userScanner.nextLine());
			if(res!=-1){
				switch(res){
					case 1:
						graph =fillFleur();
						break;
					case 2:
						displayGraph(graph);
						break;
					case 3:
						solveGraph(graph);
						break;
					case 4:
						addEdge(graph);
						break;
					case 5:
						removeEdge(graph, removed);
						break;
					case 6://undo
						undoEdgeRemoval(graph, removed);
						break;
					case 7:
						graph.writeToTextFile();
						break;
					case 8:
						running = false;
						break;
				}
			}
		}
	}

	/**	Prints Main Menu Screen
	 *	@author	Jeremy Lee
	 */
	public static void outputMainMenu(){
		System.out.println("Euler Circuit Vacation Main Menu: \n");

		System.out.println(
				"1. Read the graph from a text file\u2028\n" +
						"2. Display the graph\u2028\n" +
						"3. Solve the graph\u2028\n" +
						"4. Add an edge to the graph\u2028\n" +
						"5. Remove an edge from the graph\u2028\n" +
						"6. Undo the previous removal(s)\u2028\n" +
						"7. Write the graph to a text file\u2028\n" +
						"8. Quit" + "\nEnter a number:");
	}

	/**	Parses integer that is between 1-8.
	 * @param str string to be parsed to integer
	 * @return int between 1-8 if the string is those numbers, else returns -1
	 *	@author	Jeremy Lee
	 */
	public static int getInteger(String str){
		int res = -1;
		try
		{
			res = Integer.parseInt(str);
			if(!(res==1||res==2||res==3||res==4||res==5||res==6||res==7||res==8)){
				return -1;
			}
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Enter a valid number!");
		}
		return res;
	}

	/**	Offers options to traverse through Depth First, Breadth or Adjacency list.
	 * @param graph graph to be traversed
	 *	@author	Jeremy Lee
	 */
	public static void displayGraph(Fleur<String> graph){
		if(graph.isEulerCircuit()){
			System.out.println("1. Depth First\u2028\n" +
					"2. Breadth First\u2028\n" +
					"3. Adjacency List\u2028\n");
			int res = getInteger(userScanner.nextLine());
			switch(res){
				case 1:
					System.out.println("\nDepth First Traversal\n");
					graph.depthFirstTraversal(graph.getStart(), new PrintVisitor());System.out.println();
					break;
				case 2:
					System.out.println("\nBreadth First Traversal\n");
					graph.breadthFirstTraversal(graph.getStart(), new PrintVisitor()); System.out.println();
					break;
				case 3:
					System.out.println("\nAdjacency List");
					graph.showAdjTable();System.out.println();
					break;
			}		}else{
			System.out.println("Not an Euler Circuit! ");
		}
		System.out.println();
	}

	/**	Solves and outputs solved graph using Fleury's Algorithm
	 * @param graph graph to be solved
	 *	@author	Jeremy Lee
	 */
	public static void solveGraph(Fleur<String> graph){
		ArrayList<String> list = graph.applyFleur(graph.getStart());
		String next;
		for(int i = 0; i < list.size()-1; i++){
			String first = list.get(i).split("/")[0];
			String second = list.get(i).split("/")[1];

			if(list.get(i+1).contains(first)){
				if(first.compareTo(list.get(i+1).split("/")[0]) == 0){
					next = list.get(i+1).split("/")[1];
				}else{
					next = list.get(i+1).split("/")[0];
				}
				System.out.println("From " + first + " to " + second + "(" + next+")");
			}else if(list.get(i+1).contains(second)){
				if(second.compareTo(list.get(i+1).split("/")[0]) == 0){
					next = list.get(i+1).split("/")[1];
				}else{
					next = list.get(i+1).split("/")[0];
				}
				System.out.println("From " + second + " to " + first + "(" + next+")");

			}

		}
		System.out.println();
	}

	/**	Prompts user to add an edge.
	 * @param graph graph to add an edge
	 *	@author	Jeremy Lee
	 */
	public static void addEdge(Fleur<String> graph){
		System.out.println("First Intersection? ");
		String firstInter = userScanner.nextLine();
		System.out.println("Second Intersection? ");
		String secondInter = userScanner.nextLine();
		graph.addEdge(firstInter, secondInter, 0); //add priority?
		System.out.println();

	}

	/**	Prompts user to remove an edge.
	 * @param graph graph to remove an edge
	 *	@author	Jeremy Lee
	 */
	public static void removeEdge(Fleur<String> graph, StackInterface<String> removed){
		System.out.println("First Intersection? ");
		String firstInter = userScanner.nextLine();
		System.out.println("Second Intersection? ");
		String secondInter = userScanner.nextLine();

		if(graph.remove(firstInter, secondInter)){
			System.out.println("Street between " + firstInter + " and " + secondInter + " was removed! ");
			removed.push(firstInter); removed.push(secondInter);
		}else {
			System.out.println("Remove unsuccessful! ");
		}

		System.out.println();
	}

	/**	undo the previous edge removal.
	 * @param graph graph to undo edge removal on
	 * @param removed Stack of edges that were removed that are linked to the graph
	 *	@author	Jeremy Lee
	 */
	public static void undoEdgeRemoval(Fleur<String> graph, StackInterface<String> removed){
		if(!removed.isEmpty()){
			String first = removed.pop(); String second = removed.pop();
			System.out.println("Undo of street between " + second + " and " + first + " successful!");
			graph.addEdge(first, second, 0);
			System.out.println();
		}
	}
	/**	fills a new Fleur object filled with the information from the input txt file. Each line is read in and split into the name of the
	* 	street itself and the two other streets it crosses. Vertices are named using the name of the street/the other street it crosses.
	* 	(Ex. street 1 crosses street 2 and street 3 the two vertices are street 1/street 2 and street 1/street 3)
	* 	These pieces are then checked to ensure that an intersection is not the same as another swapped(Ex. Street 1/street 2 == street 2/street 1)
	* 	and corrected if so. All relevant edges are then added to the Fleur object
	*	@return	Fleur<String> a new fleur graph that has been filled with the information from the txt file that was
	*	read in
	*	@author	Andrew Goodman
	*
	*/
	
	public static Fleur<String> fillFleur(){
		/* TO-DO
		*Should now be working 100% let me know if you find an issue
		*/
		Scanner fileScan = openInputFile();
		Fleur<String> newFleur = new Fleur<String>();
		ArrayList<String> previousStreets = new ArrayList<String>();
		while(fileScan != null && fileScan.hasNext()) {
			String line = fileScan.nextLine();
			String streetName = line.split("from")[0].trim();
			String firstInterSec = line.split("from")[1].split("to")[0].trim();
			String secondInterSec = line.split("from")[1].split("to")[1].trim();
			String fullInter1 = streetName+"/"+firstInterSec;
			String fullInter2 = streetName+"/"+secondInterSec;
			if(previousStreets.contains(firstInterSec+"/"+streetName)){
				fullInter1 = firstInterSec+"/"+streetName;
			}else {
				System.out.println("adding: " + streetName+"/"+firstInterSec);
				previousStreets.add(streetName+"/"+firstInterSec);
			}
			if(previousStreets.contains(secondInterSec+"/"+streetName)) {
				fullInter2 = secondInterSec+"/"+streetName;

			}else {
				System.out.println("adding: " + streetName+"/"+secondInterSec);
				previousStreets.add(streetName+"/"+secondInterSec);
			}
				newFleur.addEdge(fullInter1,fullInter2, 0);

			
		}
		return newFleur;
		
	}

	public static void output(Scanner scanner){
		Scanner input = scanner;

	}

	// opens a text file for input, returns a Scanner:
	public static Scanner openInputFile()
	{
		String filename;
		Scanner scanner=null;

		System.out.print("Enter the input filename: ");
		filename = userScanner.nextLine();
		File file= new File(filename);

		try{
			scanner = new Scanner(file);
		}// end try
		catch(FileNotFoundException fe){
			System.out.println("Can't open input file\n");
			return null; // array of 0 elements
		} // end catch
		return scanner;
	}

	public static void printPath(){

	}
	   // -------  main --------------
	  /* public static void main(String[] args)
	   {
	      // build graph
	      Graph<String> myGraph1 = new Graph<String>();
	      myGraph1.addEdge("A", "B", 0);   myGraph1.addEdge("A", "C", 0);  myGraph1.addEdge("A", "D", 0);
	      myGraph1.addEdge("B", "E", 0);   myGraph1.addEdge("B", "F", 0);
	      myGraph1.addEdge("C", "G", 0);
	      myGraph1.addEdge("D", "H", 0);   myGraph1.addEdge("D", "I", 0);
	      myGraph1.addEdge("F", "J", 0);
	      myGraph1.addEdge("G", "K", 0);   myGraph1.addEdge("G", "L", 0);
	      myGraph1.addEdge("H", "M", 0);   myGraph1.addEdge("H", "N", 0);
	      myGraph1.addEdge("I", "N", 0);
	      
	      System.out.print();
	      myGraph1.showAdjTable();



	   }*/

}
