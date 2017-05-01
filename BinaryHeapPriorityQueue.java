import java.util.*;
import java.lang.*;

public class BinaryHeapPriorityQueue<K,Double> implements PriorityQueue<K>
{
	//colleaction of out PQEntries
	protected ArrayList<PQEntry<K,Double>> heap = new ArrayList<>();
	//comparator defining ordering of priorities in PQ
	private Comparator<Double> comp;
	//map that will store our items and their index positions
	protected HashMap<K, Integer> map = new HashMap();


	protected BinaryHeapPriorityQueue(Comparator<Double> c)
	{
		comp = c;
	}
	protected BinaryHeapPriorityQueue()
	{
		this(new DefaultComparator<Double>());
	}


	/////////////////////////////////////////////////

	public static void main(String[] args)
	{
		BinaryHeapPriorityQueue test = new BinaryHeapPriorityQueue<>();
		test.add("test1", 4);
		test.add("test2", 5);
		test.add("test3", 6);
		test.add("test4", 13);
		test.add("test5", 11);
		test.add("test6", 14);
		test.add("test7", 16);
		test.add("test8", 25);
		test.add("test9", 15);
		test.add("test10", 20);
		test.add("test11", 7);
		test.add("test12", 9);
		test.add("test13", 12);
		test.add("test14", 1);
		System.out.println(test.map);
		System.out.println();
		System.out.println(test.heap);
		System.out.println(test.findItem("test14"));

	}


	///////////////My Methods/////////////////////////////////////////

	
	protected int parent(int j)
	{
		return (j-1)/2;
	}
	protected int left (int j)
	{
		return 2*j + 1;
	}
	protected int right(int j)
	{
		return 2*j +2;
	}
	protected boolean hasLeft(int j)
	{
		return left(j)< heap.size();
	}
	protected boolean hasRight(int j)
	{
		return right(j) < heap.size();
	}


	protected int compare(Entry<K,Double> a, Entry<K,Double> b)
	{
		return java.lang.Double.compare(a.getValue(), b.getValue());
	}

	
	
	// //checks given priorities to make sure it is a valid one.
	protected boolean checkKeyString(K key) throws IllegalArgumentException
	{
		try
		{
			K newest = key;
			String newString = (String) newest;
			return (newString.compareTo(newString) == 0); //see if value (priority) can be compared to itself
		}
		catch (ClassCastException e)
		{
			throw new IllegalArgumentException("Incompatible key.");

		}
	}
	

	//makes sure entry is location aware
	protected PQEntry<K,Double> validate(Entry<K,Double> entry) throws IllegalArgumentException
	{
		if(!(entry instanceof PQEntry))
			throw new IllegalArgumentException("Invalid Entry.");
		PQEntry<K,Double> locator = (PQEntry<K,Double>) entry;
		int j = locator.getIndex();
		if(j >= heap.size() || heap.get(j) != locator)
			throw new IllegalArgumentException("Invalid Entry.");
		return locator;
	}


	//Exchanges the entries at indices i and j of the arraylist
	protected void swap(int i, int j)
	{
		PQEntry<K,Double> temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
		((PQEntry<K,Double>) heap.get(i)).setIndex(i);
		((PQEntry<K,Double>) heap.get(j)).setIndex(j);
		map.put(heap.get(i).getKey(), heap.get(i).getIndex());
		map.put(heap.get(j).getKey(), heap.get(j).getIndex());	
	}


	protected void bubble(int j)
	{
		if(j > 0 && compare( heap.get(j), heap.get(parent(j)) ) <0)
			upheap(j);
		else
			downheap(j);
	}

	//removes given entry
	public void remove(Entry<K,Double> entry) throws IllegalArgumentException
	{
		PQEntry<K,Double> locator = validate(entry);
		int j = locator.getIndex();
		if(j==heap.size()-1)
			heap.remove(heap.size()-1);
		else
		{
			swap(j, heap.size()-1);
			heap.remove(heap.size()-1);
			bubble(j);
		}
	}

	public String toString()
	{
		return this.heap.toString();
	}

	//moves entry at index j higher to restore heap property
	protected void upheap(int j)
	{
		while (j > 0)
		{
			int p = parent(j);
			if(compare(heap.get(j), heap.get(p)) >= 0) break; //heap property satisfied
			swap(j, p);
			j = p;
		}
	}


	protected void downheap(int j)
	{
		while(hasLeft(j))
		{
			int leftIndex = left(j);
			int smallChildIndex = leftIndex;
			if(hasRight(j))
			{
				int rightIndex = right(j);
				if(compare(heap.get(leftIndex), heap.get(rightIndex)) > 0)
				{
					smallChildIndex = rightIndex;
				}
			}
			if(compare(heap.get(smallChildIndex), heap.get(j)) >= 0) break;
			swap(j, smallChildIndex);
			j = smallChildIndex;
		}
	}

	
	//looks into our hashmap (key = K, value = index), and uses the .get() method to get the
	//index value associated with the given key. Runs in constant time. 
	protected int findItem(K key)
	{
		if(map.containsKey(key))
		{
			return map.get(key);
		}
		else
		{
			return -1;
		}
	}

	protected boolean isEmpty()
	{
		return size() == 0;
	}



	///////////////////////////////////////////////////////////////////////////////




	/////// ////Interface Methods  *CANNOT CHANGE ANYTHING ABOUT THE METHODS//////////////
	public K extractMin()
	{
		if(heap.isEmpty())
		{
			return null;
		}
		K answer = heap.get(0).getKey();
		swap(0, heap.size() - 1);
		heap.remove(heap.size() - 1);
		downheap(0);
		return answer;
	}	
	public double peekMin()
	{
		if (size() == 0)
	    {
			throw new NoSuchElementException();
	    }
	    return heap.get(0).getValue();
	}

	public void add(K item, double pri)
	{

		checkKeyString(item);
		PQEntry<K,Double> newest = new PQEntry(item, pri, heap.size());
		heap.add(newest);
		map.put(item, newest.getIndex());
		upheap(size()-1);
		
	}
	public double getPriority(K item)
	{
		int i = findItem(item);
		if(i == -1)
		{
			throw new NoSuchElementException();
		}
		return(heap.get(i).getValue());
	}
	public void decreasePriority(K item, double pri)
	{
		int i = findItem(item);
		if(heap.get(i).getValue() > pri)
		{
			heap.get(i).setValue(pri);
			upheap(i);
		}
		else
		{
			System.out.println("Invalid entry.");
		}
	}
	public boolean contains(K item)
	{
		int i = findItem(item);
		if(i != -1)
		{
			return true;
		}
		else
			return false;
	}
	public int size()
	{
		return heap.size();
	}
	///////////End of Interface Methods/////////////

}

class Entry<K,Double>
{
	//data members for PQEntry
	private K k;
	private double v;
	

	//constructor for PQEntry
	public Entry(K key, double pri)
	{
		k = key;
		v = pri;
	
	}

	//methods for PQEntry
	public K getKey()
	{
		return k;
	}
	public double getValue()
	{
		return v;
	}
	public void setValue(double priority)
	{
		v = priority;
	}
	protected void setKey(K key)
	{
		k = key;
	}


}

class PQEntry<K,Double> extends Entry<K,Double>
{
	private int index;

	public PQEntry(K key, double pri, int j)
	{
		super(key, pri);
		index = j;
	}

	public int getIndex()
	{
		return index;
	}
	public void setIndex(int j)
	{
		index = j;
	}

	public String toString()
	{
		return "<" + "Key: " + this.getKey() + " Value: " + this.getValue() + " Index: " + this.getIndex() +  ">";
	}
}

class DefaultComparator<E> implements Comparator<E>
{
	public int compare(E a, E b) throws ClassCastException
	{
		return ((Comparable<E>) a).compareTo(b);
	}
}