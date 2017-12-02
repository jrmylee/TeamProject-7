


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


		Fleur<String> fleurGraph = fillFleur();
//		fleurGraph.showAdjTable();
//		// build graph
//		Graph<String> myGraph1 = new Graph<String>();
//		myGraph1.addEdge("A", "B", 0);   myGraph1.addEdge("A", "C", 0);  myGraph1.addEdge("A", "D", 0);
//		myGraph1.addEdge("B", "E", 0);   myGraph1.addEdge("B", "F", 0);
//		myGraph1.addEdge("C", "G", 0);
//		myGraph1.addEdge("D", "H", 0);   myGraph1.addEdge("D", "I", 0);
//		myGraph1.addEdge("F", "J", 0);
//		myGraph1.addEdge("G", "K", 0);   myGraph1.addEdge("G", "L", 0);
//		myGraph1.addEdge("H", "M", 0);   myGraph1.addEdge("H", "N", 0);
//		myGraph1.addEdge("I", "N", 0);
//
//		myGraph1.showAdjTable();
		if(fleurGraph.getStart()!=null){
			runProgram(fleurGraph);
		}
	}

	/**
	 * Run Program
	 */
	public static void runProgram(Fleur<String> graph){
		boolean running = true;
		int res = -1;
		Edge last = null;
		while(running){
			System.out.println("\nEuler Circuit Vacation Main Menu: ");

			System.out.println(
					"1. Read the graph from a text file\u2028\n" +
					"2. Display the graph\u2028\n" +
					"3. Solve the graph\u2028\n" +
					"4. Add an edge to the graph\u2028\n" +
					"5. Remove an edge from the graph\u2028\n" +
					"6. Undo the previous removal(s)\u2028\n" +
					"7. Write the graph to a text file\u2028\n" +
					"8. Quit" + "\nEnter a number:");

			res = getInteger(userScanner.nextLine());

			if(res!=-1){
				switch(res){
					case 1:
						readGraph();
						break;
					case 2:
						if(graph.isEulerCircuit()){
							displayGraph(graph);
						}else{
							System.out.println("Not an Euler Circuit! ");
						}
						break;
					case 3:
						graph.applyFleur("");
						break;
					case 4:
						System.out.println("Edge name? ");
						graph.addEdge(userScanner.nextLine(), "", 0); //add priority?
						break;
					case 5:
						System.out.println("Edge name? ");
						if(graph.remove(userScanner.nextLine(), "")){
							System.out.println("Great Success! ");
						}else {
							System.out.println("Remove unsuccessful! ");
						}
						break;
					case 6://undo
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
	public static void readGraph(){
		Scanner scan = openInputFile();
	}

	public static void displayGraph(Fleur<String> graph){
		System.out.println("1. Depth First\u2028\n" +
				"2. Breadth First\u2028\n" +
				"3. Adjacency List\u2028\n");
		int res = getInteger(userScanner.nextLine());
		switch(res){
			case 1:
				System.out.println("\nDepth First Traversal");
				graph.depthFirstTraversal(graph.getStart(), new PrintVisitor());
				break;
			case 2:
				System.out.println("\nBreadth First Traversal");
				graph.breadthFirstTraversal(graph.getStart(), new PrintVisitor());
				break;
			case 3:
				System.out.println("\nAdjacency List");
				graph.showAdjTable();
				break;
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
