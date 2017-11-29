
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;
import java.text.*;


//------------------------------------------------------
public class GraphTester
{
	// -------  main --------------
	public static void main(String[] args)
	{
		
		
		Fleur<String> fleurGraph = fillFleur();
		fleurGraph.showAdjTable();
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

		myGraph1.showAdjTable();


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
		* currently adds extra empty vertices without linked edges
		* does not properly handle swapped street/intersection 100%
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
			Boolean sameFirst = false;
			Boolean sameSecond = false;
			if(previousStreets.contains(firstInterSec+"/"+streetName)){
				fullInter1 = firstInterSec+"/"+streetName;
				sameFirst = true;
				newFleur.addEdge(firstInterSec+"/"+streetName, fullInter2, 0);
			}else {
				System.out.println("adding: " + streetName+"/"+firstInterSec);
				previousStreets.add(streetName+"/"+firstInterSec);
			}
			if(previousStreets.contains(secondInterSec+"/"+streetName)) {
				fullInter2 = secondInterSec+"/"+streetName;
				sameSecond = true;
				newFleur.addEdge(secondInterSec+"/"+streetName, fullInter1, 0);

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
	public static Scanner userScanner = new Scanner(System.in);

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
