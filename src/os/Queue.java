package os;

public class Queue {

	private ProcessNode front;
	private int capacity; 
	
	public Queue(){		
		front = null;
		capacity = 0; 
	}
	
	public boolean isEmpty(){
		return front == null; 
	}
	
	public ProcessNode getFront() {
		return front;
	}

	public boolean isFull(){
		return (new ProcessNode() == null);
	}
	
	public int getCapacity(){return capacity;}
	
	public void enqueue(Process temp){
		if(isFull())
			System.out.println("Queue Is Full...");
		else {
			ProcessNode newNode = new ProcessNode(temp);
			if(isEmpty())
				 front = newNode;
			else {
				ProcessNode current = front;
				while(current.getNext() != null) {
					current = current.getNext();
				}
				current.setNext(newNode);
			}
			
			capacity++;
		}
	}
	
	public Process dequeue(){
		Process value = null;
		if(isEmpty()) return null;

		value = front.getProcess();
		front = front.getNext();
		capacity--;
		
		return value; 
	}
	
	/*public Process highestPriority(){
		Process temp = front; 
		Process highest = front; 
		
		while(temp != null){
			if(temp.getAction() < highest.getAction())
				highest = temp; 
			temp = temp.getNextProcess(); 
		}
		return highest; 
	}*/
	
	/*public void delete(Process highest)
	{
		if(isEmpty())
			return; 
		
		if(front == tail)
		{
			front = null; 
			tail = front; 
			return ; 
		}
		
		highest.getPreviousProcess().setNextProcess(highest.getNextProcess()); 
		highest.getNextProcess().setNextProcess(highest.getPreviousProcess());
		highest = null; 
		capacity--; 
	}*/
	
	public void display()
	{
		ProcessNode current = front;
		System.out.printf("\n%s\t%s\t%s\t%s\t%s\t%10s\n", "PID", "TASK", "ATTEMPTS","SLEEPTIME","START","END");
		while(current != null)
		{
			
			System.out.println(current.getProcess()); 
			current = current.getNext(); 
		}
	}
	
	/*public void sort() {
		sort(this, capacity);
	}
	
	public void sort(Queue q, int size) {
		for(int i = 0; i < size - 1; i ++) {
			Process min = findMin(q, size - 1, size);
			reorder(q, min, size);
		}
	}
	
	public Process findMin(Queue q, int k, int size) {
		Process min = new Process(1);
		min.setStart(Integer.MAX_VALUE);
		
		for(int i = 1; i < size; i++) {
			Process curr = q.dequeue();
			
			if(curr.getStart() < min.getStart() && i <= k) {
				min = curr;
			}
			
			q.enqueue(curr);
		}
		
		return min;
	}
	
	public void reorder(Queue q, Process min, int size) {
		for(int i = 1; i < size; i++) {
			Process curr = q.dequeue();
			
			if(curr.getStart() != min.getStart()) {
				q.enqueue(curr);
			}
		}
		
		q.enqueue(min);
	}*/
	
	public void sort(String sortBy) {
		switch(sortBy) {
		case "start":
			sort(this, sortBy);
			break;
		}
		
	}
	
	private void sort(Queue toSort, String by) {
	    // Prepare split parts for later merging   
	    Queue m1 = new Queue(), m2 = new Queue();

	    // Return if there's only 1 element in the queue
	    // since it's essentially "sorted".
	    if(singleItem(toSort))
	        return;

	    // Split toSort into 2 parts
	    split(toSort, m1, m2);
	    // Sort each part recursively (by splitting and merging)
	    sort(m1, by);
	    sort(m2, by);
	    // Merge each part into 1 sorted queue by start time
	    if(by.equals("start"))
	    	mergeByStart(toSort,  m1, m2);
	}

	private boolean singleItem(Queue q) {
	    Process temp = q.dequeue();
	    boolean retVal = q.isEmpty();
	    q.enqueue(temp);
	    return retVal;
	}

	private void mergeByStart(Queue toSort, Queue m1, Queue m2) {
	    // Notice that m1 and m2 are already sorted, and now we need
	    // to merge them.
	    // In the case that one of them is empty and the other one isn't,
	    // simply append all remaining "higher" values into toSort.
	    if(m1.isEmpty()) {
	        appendQueue(m2, toSort);
	        return;
	    }
	    else if (m2.isEmpty()) {
	        appendQueue(m1, toSort);
	        return;
	    }
	    // The critical comparison part...
	    if(m1.getFront().getProcess().getStart() < m2.getFront().getProcess().getStart())
	        toSort.enqueue(m1.dequeue());
	    else
	        toSort.enqueue(m2.dequeue());
	    // Continue merging elements into toSort.
	    mergeByStart(toSort, m1, m2);
	}

	// Split toSort into m1 and m2 as equally as possible
	private void split(Queue toSort, Queue m1, Queue m2) {
	    if(toSort.isEmpty())
	        return;
	    m1.enqueue(toSort.dequeue());
	    split(toSort,  m2, m1);
	}
	
	
	// Enqueue everything in src to dest.
	private void appendQueue(Queue src, Queue dest) {
	    if (src.isEmpty())
	        return;
	    dest.enqueue(src.dequeue());
	    appendQueue(src, dest);
	}
}

