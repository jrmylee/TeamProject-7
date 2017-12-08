import java.util.*;
import java.util.Map.Entry;


/**	
 * The Fleury class
 * Implements Fleury's algorithm for solving Eulerian circuits/paths.
 *	@author	Ali Masood, Andrew Goodman
 */
class Fleury<E> extends Graph<E>
{
	E start = null;	// The starting point of the graph.
	
	public boolean isEmpty()
	{
		return vertexSet.isEmpty();
	}
	
	public E getStart() {
		return start;
	}
	
	public void setStart(E elem) {
		start = elem;
	}
	
	@Override
	public void addEdge(E source, E dest, double cost)
	{
		super.addEdge(source,dest,cost);
		if(start == null) 
		{
			setStart(source);
		}
	}
	
	/**	Applies Fleury's algorithm to build a Eulerian Circuit starting
	*	and ending at start.
	*	@param start data of type E, sets starting vertex to whichever
	*		contains E start.
	*	@return	ArrayList<E>	an array list of data elements stored
	*		of type E, in order of visitation.
	*	@author	Ali Masood, Andrew Goodman
	*/
	public ArrayList<E> applyFleury(E start)	
	{	
		// Initialize euler circuit ArrayList to be returned.
		ArrayList<E> eulerCircuit = new ArrayList<E>();		// List of the order of vertex data visited.
		
		if(vertexSet.isEmpty()
		   || !isEulerCircuit()
		   || hasDisconnectedEdges())
			return eulerCircuit;
		
		// Initialize currentVertex to that which contains E start
		Vertex<E> currentVertex = vertexSet.get(start);

		// End loop when at last vertex The last vertex is the one  with no edges,
		// since edge are removed as the graph is traversed).
		while(currentVertex.adjList.size() > 0)
		{
			// Add the current vertex's data to our circuit
			eulerCircuit.add(currentVertex.getData());
			
			// For each edge, check if it's a bridge.
			// If not, set it as the next edge to follow. In case all are
			// bridges, set the last edge checked as the next edge to traverse.
			Iterator<Entry<E, Pair<Vertex<E>, Double>>> edgeIter;
			Pair<Vertex<E>, Double> edge = null;
			
			boolean nextPathFound = false;
			edgeIter = currentVertex.iterator();
			
			while(edgeIter.hasNext() && !nextPathFound)
			{	
				// Get the next edge
				Entry<E, Pair<Vertex<E>, Double>> entry = edgeIter.next();
				edge = entry.getValue();
				
				// If this is the last edge or a non-bridge				
				if(!edgeIter.hasNext() 
				   || !isBridge(currentVertex.getData(), edge.first.getData()))
				{
					nextPathFound = true;
				}
			}
			
			// Temporarily store the next vertex, Remove the edge from the Graph, set the next vertex
			Vertex<E> temp = edge.first;
			remove(currentVertex.getData(), edge.first.getData());
			currentVertex = temp;

			
		}
		
		// Add the last vertex visited to the Euler circuit
		eulerCircuit.add(currentVertex.getData());
		return eulerCircuit;
	}

	/**
     * 	Check if the edge between the supplied source and destination
	 *	is a bridge.
	 *	@param	src	type E	source vertex
	 *	@param	dst	type E	destination vertex
	 *	@return boolean	true when the vertex between the src and dst
	 *			is a bridge
	 *	@author Ali Masood, Andrew Goodman
	 */
	public boolean isBridge(E src, E dst)
	{
		int reach_with_edge = 0;	// Count the number of edges reached from src vertex
		int reach_without_edge = 0; // Count the number of edges reached from src vertex without the potential bridge
		Fleury<E> temp_fleur_graph;	// Temporary graph used to remove edges,
		
		// Count the reachable vertices from srcFleur<E> 
		CountVisitor<E> count_visitor = new CountVisitor<E>();
		breadthFirstTraversal(src, count_visitor);
		reach_with_edge = count_visitor.get_count();
		
		
		// Make a copy, and remove the potential bridge
		temp_fleur_graph = makeDeepCopy();
		temp_fleur_graph.remove(src,dst);
		
		// Counting reachable vertices from src, without src-dst edge
		count_visitor.reset();
		temp_fleur_graph.breadthFirstTraversal(src, count_visitor);
		reach_without_edge = count_visitor.get_count();
			
		if(reach_with_edge > reach_without_edge)
			return true;
		else
			return false;	
	}

	/**
	 * Make a deep copy of the passed Fleury graph.
	 * return	nF	new Fleury graph
	 * @author Andrew Goodman, Ali Masood
	 */
	public Fleury<E> makeDeepCopy()
	{
		Fleury<E> newFleury = new Fleury<>(); // New Fleury graph that will be deep copied
		Iterator<Entry<E, Vertex<E>>> vertSetIter = vertexSet.entrySet().iterator(); //Iterator for vertexSet
		Entry<E,Vertex<E>> vertexSetEntry;
		Vertex<E> currVertex;
		E currData;

		Iterator<Entry<E, Pair<Vertex<E>, Double>>> adjListIterator;
		Entry<E, Pair<Vertex<E>, Double>> adjListEntry;
	    Pair<Vertex<E>, Double> adjListEntryPair;
	    E adjData;
		
		while(vertSetIter.hasNext()) {
			vertexSetEntry = vertSetIter.next();
			currVertex = vertexSetEntry.getValue();
			currData = currVertex.getData();
			
			adjListIterator = currVertex.adjList.entrySet().iterator();
			while(adjListIterator.hasNext())
			{
				adjListEntry = adjListIterator.next();
		        adjListEntryPair = adjListEntry.getValue();
				adjData = adjListEntryPair.first.getData();
				newFleury.addEdge(currData, adjData, 0.0);
			}
		}
		
		newFleury.setStart(start);
		return newFleury;
	}


	/**	
	 * Checks if there are 0, or 2, odd vertices.
	 * @return	boolean	true when 0 or 2 vertices are odd.
	 * @author	Ali Masood	
	 */
	public boolean isEulerPath()
	{
		Iterator<Entry<E, Vertex<E>>> vertIter;	// Iterator for 
		Vertex<E> vert;	// holds vertex returned by iterator
		int oddVertices = 0; // The number of odd vertices

		if (vertexSet.isEmpty())
			return false;
		
		for( vertIter = vertexSet.entrySet().iterator(); vertIter.hasNext(); )
		{
			vert = vertIter.next().getValue();
			if (vert.adjList.size() % 2 == 0)	
				++oddVertices;
		}
		
		if(oddVertices == 2 || oddVertices == 0)
			return true;
		else
			return false;	
	}

	/**	
	 * Checks if every vertex is even. 
	 * @return	boolean		true when all vertices are even
	 * @author	Ali Masood
	 */
	public boolean isEulerCircuit()
	{
		Iterator<Entry<E, Vertex<E>>> vertIter;	// Iterator for vertexSet
		Vertex<E> vert; // holds vertex returned by iterator

		if (vertexSet.isEmpty())
		{
			return false;
		}
		for( vertIter = vertexSet.entrySet().iterator(); vertIter.hasNext(); )
		{
			vert = vertIter.next().getValue();
			if (vert.adjList.size() % 2 == 1)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if the graph is disconnected.
	 * @return boolean	true if the graph is disconnected.
	 * @author Ali Masood
	 */
	public boolean isDisconnected()
	{
		// Count visitor used to count number of vertices reachable.
		CountVisitor countvisitor = new CountVisitor();	
		
		breadthFirstTraversal(start, countvisitor);
		if(countvisitor.get_count() < vertexSet.size())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if there are any edges disconnected from the main graph.
	 * @return boolean	returns true if there any edges disconnected from the starting vertex.
	 * @author Ali Masood
	 */
	public boolean hasDisconnectedEdges()
	{
		if(isDisconnected())
		{
			Iterator<Entry<E, Vertex<E>>> vertSetIter = vertexSet.entrySet().iterator(); //Iterator for vertexSet
			Entry<E,Vertex<E>> vertexSetEntry;
			Vertex<E> currVertex;
			
			// Visit every vertex reachable from start
			breadthFirstTraversal(start, new CountVisitor());
			
			// For every vertex
			while(vertSetIter.hasNext())
			{
				vertexSetEntry = vertSetIter.next();
				currVertex = vertexSetEntry.getValue();
				
				// If it it is visited && it has ANY edges, return false
				if(!currVertex.isVisited()
				    && currVertex.adjList.size() > 0)
				{
					return true;
				}
			}
		}
		
		// Otherwise return false
		return false;
	}
}

