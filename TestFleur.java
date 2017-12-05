import java.util.*;

public class TestFleur {
	
	public static void fillFleur(Fleur<String> fleur_graph, int numbernodes)
	{
		fleur_graph.clear();
		for(int i = 0; i < numbernodes; i++)
		{
			for(int j = i + 1; j < numbernodes; j++)
			{
				fleur_graph.addEdge(Character.toString((char)('A' + i)),
						    Character.toString((char)('A' + j)),
						    1.0);
			}
		}
	}
	
	public static void fillFleurDisconnected(Fleur<String> fleur_graph)
	{
		fleur_graph.clear();
		
		fleur_graph.addEdge("A", "B", 1.0);
		fleur_graph.addEdge("A", "C", 1.0);
		fleur_graph.addEdge("B", "C", 1.0);
		
		fleur_graph.addEdge("D", "E", 1.0);
		fleur_graph.addEdge("D", "F", 1.0);
		fleur_graph.addEdge("E", "F", 1.0);
	}
	
	public static void testRemove(Fleur<String> fleur_graph)
	{
		System.out.println("Removing A-C, B-C connections:");
		fleur_graph.remove("A", "C");
		fleur_graph.remove("B", "C");
		
		System.out.println("Adjacency Table after removal");
		fleur_graph.showAdjTable();
	}
	
	public static void testApplyFleur(Fleur<String> fleur_graph)
	{
		ArrayList<String> solution = fleur_graph.applyFleur("A");
		System.out.println("Fleur Solution:");
		if(solution.isEmpty())
			System.out.println("	ArrayList for solution is empty.");
		else
		{
			for(String item : solution)
			{
				System.out.println(item);
			}
		}
	}
	
	public static void runTests(Fleur<String> fleur_graph)
	{
		System.out.println("----------------------------------------------");
		System.out.println("Adjacency Table:");
		fleur_graph.showAdjTable();
		
		System.out.println("Testing Apply Fleur:");
		testApplyFleur(fleur_graph);
		
		System.out.println("");
	}
	
	public static void main(String[] args) {
		Fleur<String> fleur_graph= new Fleur<>();
		
		// Three Vertices, Complete Graph
		System.out.println("Graph with three vertices:");
		fillFleur(fleur_graph, 3);
		runTests(fleur_graph);
		
		// Five Vertices, Complete Graph
		System.out.println("Graph with five vertices:");
		fillFleur(fleur_graph, 5);
		runTests(fleur_graph);
		
		// Two Vertices, Complete Graph
		System.out.println("Graph with two vertices:");
		fillFleur(fleur_graph, 2);
		runTests(fleur_graph);
		
		// One Vertex, Complete Graph
		System.out.println("Graph with one vertex:");
		fillFleur(fleur_graph, 1);
		runTests(fleur_graph);

		// Empty Graph
		System.out.println("Graph with zero vertices:");
		fillFleur(fleur_graph, 0);
		runTests(fleur_graph);
		
		// Disconnected Graph, All even Vertices
		System.out.println("Graph with even, but disconnected, vertices:");
		fillFleurDisconnected(fleur_graph);
		runTests(fleur_graph);
				
		// Two odd vertices
		System.out.println("Graph with two odd vertices:");
		fillFleur(fleur_graph, 3);
		fleur_graph.remove("A","B");
		runTests(fleur_graph);
		
	}

}
