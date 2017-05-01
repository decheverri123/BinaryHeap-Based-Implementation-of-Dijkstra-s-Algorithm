import java.util.*;

public class DijkstraSingleSourceShortestPaths<E> implements SingleSourceShortestPaths<E>
{

	private BinaryHeapPriorityQueue pq;
	
	private Map<E,E> arrivedFrom;

	private Map<E, Double> arrivalTime;

	private E origin;

	private Map<E, Map<E, Double>> pqTokens;

	private HashSet settled;

	private double[] distances;


	public DijkstraSingleSourceShortestPaths(Map<E, Map<E, Double>> edges, E source)
	{
		////////////////////////////////////////
		origin = source;
		//a priority queue for our entries
		pq = new BinaryHeapPriorityQueue();

		arrivedFrom = new HashMap<E,E>();

		arrivalTime = new HashMap<E, Double>();

	
		////////////////////////////////////////

		


		pq.add(source, 0);
		arrivedFrom.put(source, null);
		while(pq.size() > 0)
		{
			double distance = pq.peekMin();
			E vertex = (E) pq.extractMin();
			arrivalTime.put(vertex, distance);
			for(E vertices : edges.get(vertex).keySet())
			{
				if(!(arrivalTime.containsKey(vertices)))
				{
					if(!pq.contains(vertices))
					{
						pq.add(vertices, distance + (edges.get(vertex).get(vertices)));
						arrivedFrom.put(vertices, vertex);
					}
					else
					{
						if(pq.getPriority(vertices) > distance + (edges.get(vertex).get(vertices)))
						{
							pq.decreasePriority(vertices, distance + (edges.get(vertex).get(vertices)));
							arrivedFrom.put(vertices, vertex);
						}
					}
				}
			}
		}

		



		
		

	

	}

	public static void main(String[] args)
	{
		String origin = args[0];
		String destination = args[1];


		

	}

	


	//////////////Interface Methods *DO NOT CHANGE/////////////////////////////////////////////
	public List<E> getPath(E dest)
	{
	
		List<E> path = new LinkedList();
		path.add(dest);
		E prev = arrivedFrom.get(dest);

		while(prev != null)
		{
			path.add(0, prev);
			prev = arrivedFrom.get(prev);
		}
		if(path.get(0).equals(origin))
		{
			return path;
		}
		return null;
	}

	public double getDistance(E dest)
	{
		
		return arrivalTime.get(dest);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
}












