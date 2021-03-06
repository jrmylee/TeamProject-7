


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
		boolean running = true; int res;
		Fleury<String> graph = new Fleury<>();
		StackInterface<String> removed = new LinkedStack<>();

		while(running){
			outputMainMenu();
			res = getInteger(userScanner.nextLine());
			if(res!=-1){
				switch(res){
					case 1:
						graph = fillFleury();
						break;
					case 2:
						displayGraph(graph);
						break;
					case 3:
						menuSolveGraph(graph);
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
						menuWriteAdj(graph);
						break;
					case 8:
						running = false;
						break;
				}
			}else{
				System.out.println("\nEnter a Valid Menu Option!");
			}
			pauseMenu();
		}
	}
	/**	Menu option for writing to solve graph.
	 * @param graph graph to undo edge removal on
	 *	@author	Jeremy Lee
	 */
	public static void menuSolveGraph(Fleury<String> graph){
		if(!graph.isEmpty() && graph.isEulerCircuit()){
			System.out.println(solveGraph(graph));

			System.out.println("\nWrite solution to file?(t/f)");
			String res1 = userScanner.nextLine();
			if(res1.equals("t")){
				System.out.println("Enter a filename: ");
				String name = userScanner.nextLine();
				if(!name.isEmpty()){
					try {
						graph.writeToTextFile(new FileWriter(new File(name)), solveGraph(graph));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}else{
					System.out.println("Enter a valid name! ");
				}
			}
		}else if(!graph.isEulerCircuit()){
			System.out.println("Not an Euler Circuit!");
		}else{
			System.out.println("Nothing in graph!");
		}
	}

	/**	Menu option for writing to adjkist.
	 * @param graph graph to undo edge removal on
	 *	@author	Jeremy Lee
	 */
	public static void menuWriteAdj(Fleury<String> graph){
		System.out.println("Enter a filename: ");
		String name = userScanner.nextLine();
		if(!name.isEmpty()){
			try {
				graph.writeToTextFile(new FileWriter(new File(name)), graph.adjTableStr());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}else{
			System.out.println("Enter a valid name! ");
		}

	}
	/**	Prints Main Menu Screen
	 *	@author	Jeremy Lee
	 */
	public static void outputMainMenu(){
		System.out.println("Euler Circuit Vacation Main Menu: \n");

		System.out.println(
				"1. Read the graph from a text file\n" +
						"2. Display the graph\n" +
						"3. Solve the graph\n" +
						"4. Add an edge to the graph\n" +
						"5. Remove an edge from the graph\n" +
						"6. Undo the previous removal(s)\n" +
						"7. Write the graph to a text file\n" +
						"8. Quit" + "\nEnter a number:");
	}
	
	/**
	 * Pauses the program until the user hits enter.
	 * @author Ali Masood
	 */
	public static void pauseMenu() {
		Scanner user_input = new Scanner(System.in);
		System.out.println("Hit Enter to continue.\n");
		user_input.nextLine();
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
	public static void displayGraph(Fleury<String> graph){
		if(!graph.isEmpty()){
			System.out.println("1. Depth First\n" +
					"2. Breadth First\n" +
					"3. Adjacency List\n");
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
			System.out.println("Graph is empty! ");
		}
		System.out.println();
	}

	/**	Solves and outputs solved graph using Fleury's Algorithm
	 * @param graph graph to be solved
	 *	@author	Jeremy Lee
	 */
	public static String solveGraph(Fleury<String> graph){
		String output = ""; 
		Fleury<String> temp = graph.makeDeepCopy();
		ArrayList<String> list = temp.applyFleury(temp.getStart());
		String next = "";
		output += ("\nBeginning intersection: " + list.get(0) + "\n");
		for(int i = 0; i < list.size()-1; i++){
			String[] allAtInter1 = list.get(i).split("/");
			String[] allAtInter2 = list.get(i+1).split("/");
			for(int x = 0 ; x < allAtInter1.length; x++) {
				for(int y = 0; y < allAtInter2.length; y++) {
					if(allAtInter1[x].compareTo(allAtInter2[y]) == 0) {
						next = allAtInter1[x];
					}
				}
			}
			output += "take \"" + next + "\" from \"";
			for(int x = 0; x < allAtInter1.length; x++) {
				if(allAtInter1[x].compareTo(next) != 0) {
						output += allAtInter1[x] + " and ";
				}
			}
			output = output.substring(0, output.length() - 5);
			output += "\" to \"";
			for(int x = 0; x < allAtInter2.length; x++) {
				if(allAtInter2[x].compareTo(next) != 0) {
					output += allAtInter2[x] + " and ";
				}
			}
			output = output.substring(0, output.length() - 5);
			output += "\"\n";
		}
		output += "\nEnding intersection: " + list.get(list.size()-1) + "\n";
		
		return output;
	}

	/**	Prompts user to add an edge.
	 * @param graph graph to add an edge
	 *	@author	Jeremy Lee
	 */
	public static void addEdge(Fleury<String> graph){
		System.out.println("First Intersection? ");
		String firstInter = userScanner.nextLine();
		System.out.println("Second Intersection? ");
		String secondInter = userScanner.nextLine();
		graph.addEdge(firstInter, secondInter, 0); //add priority?
		System.out.println(firstInter + " " + secondInter + " added!");
		System.out.println();

	}

	/**	Prompts user to remove an edge.
	 * @param graph graph to remove an edge
	 *	@author	Jeremy Lee
	 */
	public static void removeEdge(Fleury<String> graph, StackInterface<String> removed){
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
	public static void undoEdgeRemoval(Fleury<String> graph, StackInterface<String> removed){
		if(!removed.isEmpty()){
			String first = removed.pop(); String second = removed.pop();
			System.out.println("Undo of street between " + second + " and " + first + " successful!");
			graph.addEdge(first, second, 0);
			System.out.println();
		}else{
			System.out.println("Nothing to undo!");
		}
	}
	/**	fills a new Fleury object filled with the information from the input txt file. Each line is read in and split into the name of the
	* 	street itself and the two other streets it crosses. Vertices are named using the name of the street/the other street it crosses.
	* 	(Ex. street 1 crosses street 2 and street 3 the two vertices are street 1/street 2 and street 1/street 3)
	* 	These pieces are then checked to ensure that an intersection is not the same as another swapped(Ex. Street 1/street 2 == street 2/street 1)
	* 	and corrected if so. All relevant edges are then added to the Fleury object
	*	@return	Fleury<String> a new fleury graph that has been filled with the information from the txt file that was
	*	read in
	*	@author	Andrew Goodman
	*
	*/
	
	public static Fleury<String> fillFleury(){
		/* TO-DO
		*Should now be working 100% let me know if you find an issue
		*/
		Scanner fileScan = openInputFile();
		Fleury<String> newFleury = new Fleury<String>();
		ArrayList<String> previousStreets = new ArrayList<String>();
		while(fileScan != null && fileScan.hasNext()) {
			String line = fileScan.nextLine().toLowerCase();
			String[] streetName = {line.split("from")[0].trim()};
			//String firstInterSec = line.split("from")[1].split("to")[0].trim();
			String[] listOfFirstIntersects = line.split("from")[1].split("to")[0].trim().split("and");
			listOfFirstIntersects = addToEnd(streetName,listOfFirstIntersects);
			for(int i = 0;i <listOfFirstIntersects.length; i++) {
				listOfFirstIntersects[i] = listOfFirstIntersects[i].trim();
			}
			//String secondInterSec = line.split("from")[1].split("to")[1].trim();
			String[] listOfSecondIntersects = line.split("from")[1].split("to")[1].trim().split("and"); 
			listOfSecondIntersects = addToEnd(streetName,listOfSecondIntersects);
			for(int i = 0;i <listOfSecondIntersects.length; i++) {
				listOfSecondIntersects[i] = listOfSecondIntersects[i].trim();
			}
			String[] permutesOfFirst = getPermuationsOfIntersections(listOfFirstIntersects);
			String[] permutesOfSecond = getPermuationsOfIntersections(listOfSecondIntersects);
			String fullInter1 = permutesOfFirst[0];
			String fullInter2 = permutesOfSecond[0];
			for(int i = 0; i < permutesOfFirst.length; i++) {
				if(previousStreets.contains(permutesOfFirst[i])) {
					fullInter1 = permutesOfFirst[i];
				}else if(i == permutesOfFirst.length-1) {
					System.out.println("adding: " + fullInter1);
					previousStreets.add(fullInter1);
				}
			}
			for(int i = 0; i < permutesOfSecond.length; i++) {
				if(previousStreets.contains(permutesOfSecond[i])) {
					fullInter2 = permutesOfSecond[i];
				}else if(i == permutesOfSecond.length-1) {
					System.out.println("adding: " + fullInter2);
					previousStreets.add(fullInter2);
				}
			}
			newFleury.addEdge(fullInter1,fullInter2, 0);

			
		}
		return newFleury;
		
	}
	
	static String[] getPermuationsOfIntersections(String[] input) {
	      String[] words = permute(input, 0);
	      String[] intersects = new String[facto(input.length)];
	      for(int i = 0; i < intersects.length; i++){
	        String concated = "";
	        for(int x = i * (input.length); x < (i+1) * input.length; x++){
	        	concated += words[x] + "/";
	        }
	        concated = concated.substring(0,concated.length()-1);
	        intersects[i] = concated;
	      }
	      return intersects;
	}
	
	static String[] permute(String[] arr, int k){
	      String[] returnVal = new String[0];
	        for(int i = k; i < arr.length; i++){
	            swap(arr, i, k);
	            returnVal = addToEnd(returnVal,permute(arr, k+1));
	            swap(arr, k, i);
	        }
	        if (k == arr.length -1){
	          	returnVal = arr;
	            //System.out.println(java.util.Arrays.toString(arr));
	        }
	      return returnVal;
	    }

	static String[] addToEnd(String[] adee, String[] adder){
	  	String[] newStringArr = new String[adee.length+adder.length];
	    for(int i = 0; i < adee.length; i++){
	    	newStringArr[i] = adee[i];
	    }
	    for(int i = adee.length; i < newStringArr.length; i++){
	    	newStringArr[i] = adder[i-adee.length];
	    }
	    return newStringArr;
	  }

	static int facto(int i){
	    for(int x = i-1; x > 0; x--){
	    	i *= x;;
	    }
	    return i;
	  }
	
	static void swap(String[] words, int first, int second){
	    String temp = words[first];
	    words[first] = words[second];
	      words[second] = temp;
	  }

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
}
/*
Euler Circuit Vacation Main Menu:

1. Read the graph from a text file
2. Display the graph
3. Solve the graph
4. Add an edge to the graph
5. Remove an edge from the graph
6. Undo the previous removal(s)
7. Write the graph to a text file
8. Quit
Enter a number:
1
Enter the input filename: src/graph data1.txt
adding: west 46th street/7th ave
adding: west 46th street/broadway
adding: west 46th street/broadway
adding: west 46th street/8th ave
adding: 8th ave/west 47th street
adding: west 47th street/broadway
adding: broadway/west 48th street
adding: west 48th street/7th ave
adding: 7th ave/west 47th street
adding: west 47th street/broadway
adding: broadway/west 45th street
adding: broadway/west 45th street
adding: broadway/west 44th street
adding: west 44th street/7th ave
adding: 7th ave/west 45th street
adding: 7th ave/west 45th street
adding: 7th ave/west 47th street
adding: west 47th street/6th ave
adding: 6th ave/west 46th street
adding: west 46th street/7th ave
Hit Enter to continue.


Euler Circuit Vacation Main Menu:

1. Read the graph from a text file
2. Display the graph
3. Solve the graph
4. Add an edge to the graph
5. Remove an edge from the graph
6. Undo the previous removal(s)
7. Write the graph to a text file
8. Quit
Enter a number:
1
Enter the input filename: graph data2.txt
Can't open input file

Hit Enter to continue.


Euler Circuit Vacation Main Menu:

1. Read the graph from a text file
2. Display the graph
3. Solve the graph
4. Add an edge to the graph
5. Remove an edge from the graph
6. Undo the previous removal(s)
7. Write the graph to a text file
8. Quit
Enter a number:
1
Enter the input filename: src/graph data2.txt
adding: street 1/street 4/street 9
adding: street 1/street 10/street 2
adding: street 1/street 10/street 2
adding: street 2/street 3/street 9
adding: street 2/street 3/street 9
adding: street 3/street 4/street 10
adding: street 3/street 4/street 10
adding: street 1/street 4/street 9
adding: street 5/street 8/street 9
adding: street 5/street 6/street 10
adding: street 6/street 7/street 9
adding: street 5/street 6/street 10
adding: street 6/street 11
adding: street 6/street 7/street 9
adding: street 6/street 12
adding: street 6/street 7/street 9
adding: street 7/street 8/street 10
adding: street 7/street 8/street 10
adding: street 5/street 8/street 9
adding: street 5/street 8/street 9
adding: street 8/street 11
adding: street 8/street 12
adding: street 1/street 4/street 9
adding: street 2/street 3/street 9
adding: street 1/street 4/street 9
adding: street 5/street 8/street 9
adding: street 2/street 3/street 9
adding: street 6/street 7/street 9
adding: street 1/street 10/street 2
adding: street 3/street 4/street 10
adding: street 5/street 6/street 10
adding: street 1/street 10/street 2
adding: street 3/street 4/street 10
adding: street 7/street 8/street 10
Hit Enter to continue.


Euler Circuit Vacation Main Menu:

1. Read the graph from a text file
2. Display the graph
3. Solve the graph
4. Add an edge to the graph
5. Remove an edge from the graph
6. Undo the previous removal(s)
7. Write the graph to a text file
8. Quit
Enter a number:
2
1. Depth First
2. Breadth First
3. Adjacency List

1

Depth First Traversal

street 1/street 4/street 9
street 5/street 8/street 9
street 8/street 11
street 6/street 11
street 5/street 6/street 10
street 6/street 7/street 9
street 2/street 3/street 9
street 1/street 10/street 2
street 3/street 4/street 10
street 7/street 8/street 10
street 8/street 12
street 6/street 12


Hit Enter to continue.



Euler Circuit Vacation Main Menu:

1. Read the graph from a text file
2. Display the graph
3. Solve the graph
4. Add an edge to the graph
5. Remove an edge from the graph
6. Undo the previous removal(s)
7. Write the graph to a text file
8. Quit
Enter a number:
3

Beginning intersection: street 1/street 4/street 9
take "street 9" from "street 1 and street 4" to "street 5 and street 8"
take "street 8" from "street 5 and street 9" to "street 11"
take "street 11" from "street 8" to "street 6"
take "street 6" from "street 11" to "street 5 and street 10"
take "street 5" from "street 6 and street 10" to "street 8 and street 9"
take "street 8" from "street 5 and street 9" to "street 7 and street 10"
take "street 7" from "street 8 and street 10" to "street 6 and street 9"
take "street 9" from "street 6 and street 7" to "street 2 and street 3"
take "street 9" from "street 2 and street 3" to "street 1 and street 4"
take "street 1" from "street 4 and street 9" to "street 10 and street 2"
take "street 2" from "street 1 and street 10" to "street 3 and street 9"
take "street 3" from "street 2 and street 9" to "street 4 and street 10"
take "street 10" from "street 3 and street 4" to "street 1 and street 2"
take "street 10" from "street 1 and street 2" to "street 5 and street 6"
take "street 6" from "street 5 and street 10" to "street 7 and street 9"
take "street 6" from "street 7 and street 9" to "street 12"
take "street 12" from "street 6" to "street 8"
take "street 8" from "street 12" to "street 7 and street 10"
take "street 10" from "street 7 and street 8" to "street 3 and street 4"
take "street 4" from "street 3 and street 10" to "street 1 and street 9"

Ending intersection: street 1/street 4/street 9


Write solution to file?(t/f)
t
Enter a filename:
solvegraph2.txt
Hit Enter to continue.


Euler Circuit Vacation Main Menu:

1. Read the graph from a text file
2. Display the graph
3. Solve the graph
4. Add an edge to the graph
5. Remove an edge from the graph
6. Undo the previous removal(s)
7. Write the graph to a text file
8. Quit
Enter a number:
7
Enter a filename:
adjlistGraph2.txt
Hit Enter to continue.


Euler Circuit Vacation Main Menu:

1. Read the graph from a text file
2. Display the graph
3. Solve the graph
4. Add an edge to the graph
5. Remove an edge from the graph
6. Undo the previous removal(s)
7. Write the graph to a text file
8. Quit
Enter a number:
6
Nothing to undo!
Hit Enter to continue.

5
Euler Circuit Vacation Main Menu:

1. Read the graph from a text file
2. Display the graph
3. Solve the graph
4. Add an edge to the graph
5. Remove an edge from the graph
6. Undo the previous removal(s)
7. Write the graph to a text file
8. Quit
Enter a number:
8
Hit Enter to continue.

 */