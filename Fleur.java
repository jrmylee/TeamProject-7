import java.util.*;
import java.util.Map.Entry;

/**	
 * The Edge Class
 *	@author	Cynthia Lee-Klawender
 */
class Edge<E> implements Comparable< Edge<E> >
{
	 Vertex<E> source, dest;
	 double cost;

	 Edge( Vertex<E> src, Vertex<E> dst, Double cst)
	 {
	    source = src;
	    dest = dst;
	    cost = cst;
	 }

	 Edge( Vertex<E> src, Vertex<E> dst, Integer cst)
	 {
	    this (src, dst, cst.doubleValue());
	 }

	 Edge()
	 {
	    this(null, null, 1.);
	 }

	 public String toString(){ return "Edge: "+source.getData() + " to " + dest.getData()
			 + ", distance: " + cost;
	 }

	 public int compareTo( Edge<E> rhs )
	 {
	    return (cost < rhs.cost? -1 : cost > rhs.cost? 1 : 0);
	 }
}


/**	
 * The Fleur class
 * Implements Fleury's algorithm for solving Eulerian circuits/paths.
 *	@author	Ali Masood
 */
class Fleur<E> extends Graph<E>
{
	/**	Applies Fleur's algorithm to build a Eulerian Circuit starting
	*	and ending at start.
	*	@param start data of type E, sets starting vertex to whichever
	*		contains E start.
	*	@return	ArrayList<E>	an array list of data elements stored
	*		of type E, in order of visitation.
	*	@author	Ali Masood
	*/
	ArrayList<E> applyFleur(E start)	
	{
		
		// Check if the graph is a Eulerian Circuit.
		if(!isEulerCircuit())
			return null;

		// Initialize currentVertex to that which contains E start
		currentVertex = vertexSet.get(start);

		// Initialize euler circuit ArrayList to be returned.
		ArrayList<E> eulerCircuit = new ArrayList<E>();

		// End loop when at last vertex The last vertex is the one  with no edges,
		// since edge are removed as the graph is traversed).
		while(currVertex.adjList().size() > 0)
		{
			// Add the current vertex's data to our circuit
			eulerCircuit.add(currentVertex.getData());
			
			// For each edge, check if it's a bridge.
			// If not, set it as the next edge to follow. In case all are
			// bridges, set the last edge checked as the next edge to traverse.
			Iterator<Entry<E, Pair<Vertex<E>, Double>>> edgeIter;
			Pair<Vertex<E>, Double> edge;
			boolean nextNonBridgeFound = false;
			for( edgeIter = currentVertex.adjList.entrySet().iterator();
			     edgeIter.hasNext() && !nextNonBridgeFound; ) 
			{
				// Get the next edge
				edge = edgeIter.next().getValue();
				
				// If this is the last edge or a non-bridge				
				if(!edgeIter.hasNext() || !isBridge(currentVector.getData(), edge.first))
					nextNonBridgeFound = true;
			}
			// Use the found edge to set the next vertex
			currentVertex = edge.first;

			// Remove the edge from the Graph
			remove(currentVertex.getData() ,edge.first);
		}
		return eulerCircuit;
	}

	/**
     * 	Check if the edge between the supplied source and destination
	 *	is a bridge.
	 *	@param	src	type E	source vertex
	 *	@param	dst	type E	destination vertex
	 *	@return boolean	true when the vertex between the src and dst
	 *			is a bridge
	 */
	boolean isBridge(E src, E dst)
	{
		// Count the number of vertices reachable from src
		// remove the src-dest edge . Count the number of
		// vertices reachable from src. If it's less, then
		// src-dst is a bridge.
		int reach_with_edge = 0;
		CountVisitor<Vertex> count_visitor();
		breadthFirstTraversal(src, count_visitor);
		reach_with_edge = count_visitor.get_count();
		
		
		// Remove edge, and check if is a Euler Path.
		remove(src,dst);
		
		// Counting reachable vertices without src-dst edge
		int reach_without_edge = 0;
		count_visitor.reset();
		breadthFirstTraversal(src, count_visitor);
		reach_without_edge = count_visitor.get_count();
		
		// Add edge back, and return
		addEdge(src,dst,0);	// Cost set to 0 because cost doesn't matter in this 
			
		if(reach_with_edge > reach_without_edge)
			return true;
		else
			return false;	
	}

	/**	Checks if there are 0, or 2, odd vertices.
	*	@return	boolean	true when 0 or 2 vertices are odd.
	*	@author	Ali Masood	
	*/
	boolean isEulerPath()
	{
		HashMap<E, Vertex<E>> vertsInGraph;
		Iterator<Entry<E, Vertex<E>>> vertIter;
		Vertex<E> vert;
		int oddVertices = 0;
		vertsInGraph = vertexSet;

		if (vertexSet.isEmpty())
			return false;
		for( vertIter = vertsInGraph.entrySet().iterator(); vertIterator.hasNext(); )
		{
			vert = vertIter.next();
			if (vert.adjList.size() % 2 == 0)	
				++oddVertices;
		}
		if(oddVertices == 2 || oddVertices == 0)
			return true;
		else
			return false;	
	}

	/**	Checks if every vertex is even. Doesn't check for the option of
	*	2 odd vertices, since we're looking for a Eulerian circuit,
	*	not a path.
	*	@return	boolean		true when all vertices are even
	*	@author	Ali Masood
	*/
	boolean isEulerCircuit()
	{
		HashMap<E, Vertex<E>> vertsInGraph;
		Iterator<Entry<E, Vertex<E>>> vertIter;
		Vertex<E> vert;
		vertsInGraph = vertexSet;

		if (vertexSet.isEmpty())
			return false;
		for( vertIter = vertsInGraph.entrySet().iterator(); vertIterator.hasNext(); )
		{
			vert = vertIter.next();
			if (vert.adjList.size() % 2 == 0)	
				return false;
		} 
		return true;
	}
}

