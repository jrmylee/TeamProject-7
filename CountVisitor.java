/**
 * Implements Visitor>Vertex> and counts the number of vertices visited
 * @author Ali Masood
 */
class CountVisitor<Vertex> implements  Visitor<Vertex>
{
    int count;	// Counts the number of times visit has been called

	/**
 	 * Constuctor initializes ocunt to 0.
 	 */
	public Vertex()
	{
		reset();
	}

	/**
 	 * Increments count by 1.
 	 * @param vertex Vertex objct to be visited
 	 */
	public void visit(Vertex vertex)
	{
		count++;	
	}		
	
	public int get_count()
	{
		return count;
	}

	public void set_count(int n)
	{
		count = n;
	}

	public void reset()
	{
		set_count(0);
	}
}
