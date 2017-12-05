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
	 String name;

	 Edge( Vertex<E> src, Vertex<E> dst, Double cst, String nm)
	 {
	    source = src;
	    dest = dst;
	    cost = cst;
	    name = nm;
	 }

	 Edge( Vertex<E> src, Vertex<E> dst, Integer cst, String nm)
	 {
	    this (src, dst, cst.doubleValue(),nm);
	 }

	 Edge()
	 {
	    this(null, null, 1.,"");
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
	E start = null;
	
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
		// Initialize euler circuit ArrayList to be returned.
		ArrayList<E> eulerCircuit = new ArrayList<E>();
		
		if(vertexSet.isEmpty()
		   || !isEulerCircuit()
		   || isDisconnected())
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
				Entry<E, Pair<Vertex<E>, Double>> e = edgeIter.next();
				edge = e.getValue();
				
				// If this is the last edge or a non-bridge				
				if(!edgeIter.hasNext() 
				   || !isBridge(currentVertex.getData(), edge.first.getData()))
				{
					nextPathFound = true;
				}
			}
			
			//Temporarily store the next vertex
			Vertex<E> temp = edge.first;
			
			// Remove the edge from the Graph
			remove(currentVertex.getData(), edge.first.getData());
			
			// Use the found edge to set the next vertex
			currentVertex = temp;

			
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
		CountVisitor<E> count_visitor = new CountVisitor<E>();
		breadthFirstTraversal(src, count_visitor);
		reach_with_edge = count_visitor.get_count();
		
		
		// Remove edge, and check if is a Euler Path.
		Fleur<E> temp_fleur_graph = makeDeepCopy();
		temp_fleur_graph.remove(src,dst);
		
		// Counting reachable vertices without src-dst edge
		int reach_without_edge = 0;
		count_visitor.reset();
		temp_fleur_graph.breadthFirstTraversal(src, count_visitor);
		reach_without_edge = count_visitor.get_count();
		
		// Add edge back, and return
		addEdge(src,dst,0);	// Cost set to 0 because cost doesn't matter in this 
			
		if(reach_with_edge > reach_without_edge)
			return true;
		else
			return false;	
	}

	Fleur<E> makeDeepCopy()
	{
		Fleur nF = new Fleur();
		Iterator<Entry<E, Vertex<E>>> iter = vertexSet.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<E, Vertex<E>> v = iter.next();
			Vertex<E> newV = new Vertex<E>(v.getValue().getData());
			newV.adjList = new HashMap<E, Pair<Vertex<E>, Double> >(v.getValue().adjList);
			nF.vertexSet.put(v.getKey(), newV);
		}
		return nF;
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
		for( vertIter = vertsInGraph.entrySet().iterator(); vertIter.hasNext(); )
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
	 *  Checks if every vertex is even. 
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
		{
			return false;
		}
		for( vertIter = vertsInGraph.entrySet().iterator(); vertIter.hasNext(); )
		{
			vert = vertIter.next().getValue();
			if (vert.adjList.size() % 2 == 1)
			{
				return false;
			}
		}
		return true;
	}

	boolean isDisconnected()
	{
		CountVisitor<E> countvisitor = new Countvisitor<>();
		breadthFirstTraversal(start, countvisitor);
		if(countvisitor < vertexSet.size())
		{
			return true;
		}
		return false;
	}
}

