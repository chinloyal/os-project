package os;


public class Queue {

	private Process front; 
	private Process tail; 
	int capacity; 
	
	public Queue(){		
		front = null; 
		tail  = null;
		capacity = 0; 
	}
	
	public boolean isEmpty(){
		return front == null; 
	}
	
	public boolean isFull(){
		return (new Process(0) == null);
	}
	
	public int getCapacity(){return capacity;}
	
	public void enqueue(Process temp){
		if(isFull())
			return ;
		else{
			if(isEmpty()){
			  front = temp; 
			  tail = front; 
			}
			else{
				temp.setPreviousProcess(tail);
				tail.setNextProcess(temp); 
				tail = temp; 
			}
			capacity++; 
		}
	}
	
	public Process deqeue(){
		if(isEmpty())
			return null; 
		
		Process temp = front; 

		if(front == tail){
			front = null; 
			tail = front;
			capacity--; 
			return temp; 
		}
		else
			front = front.getNextProcess(); 
		capacity--;
		return temp; 
	}
	
	public Process highestPriority(){
		Process temp = front; 
		Process highest = front; 
		
		while(temp != null){
			if(temp.getAction() < highest.getAction())
				highest = temp; 
			temp = temp.getNextProcess(); 
		}
		return highest; 
	}
	
	public void delete(Process highest)
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
	}
	
	public void display()
	{
		Process temp = front; 
		System.out.printf("\n%s\t%s\t%s\t%s\t%s\t%10s\n", "PID", "TASK", "ATTEMPTS","SLEEPTIME","START","END");
		while(temp != null)
		{
			
			System.out.println(temp); 
			temp = temp.getNextProcess(); 
		}
	}
}

