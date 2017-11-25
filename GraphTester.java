
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.text.*;


//------------------------------------------------------
public class GraphTester
{
<<<<<<< HEAD
	// -------  main --------------
	public static void main(String[] args)
	{
		openInputFile();
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
=======
	   // -------  main --------------
	   public static void main(String[] args)
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



	   }
>>>>>>> 7168f58697558ad4df6462e968e051a603816044

}
