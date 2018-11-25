package os;

import java.util.Date;
import java.util.Random;
public class Process {

	private int pid; // process id
	private Action task; //task process will do
	private int attempts; //number of attempts for lock
	private long start; //time marking of the start of a process / or arrival time
	private long end; // time marking the end of a process
	private int sleepTime; //stores how long a process should sleep / or burst time
	private Process baseAddress; //stores the address of current process

	private Random rand2 = new Random(getSeed(0, 20));
//	private static Random rand3 = new Random(getSeed(5, 6));
	public Process(int PID){		
		pid = PID; 
		generate(); 
		attempts = 0;
		end = -1;
		baseAddress = this;
	}
	
	public int getPID(){
		return pid;
	}
	public int getAttempts(){
		return attempts;
	}
	
	public long getStart(){
		return start;
	}
	
	public void setStart(long start) {
		this.start = start;
	}
	
	public long getEnd(){
		return end;
	}
	
	public int getSleepTime(){
		return sleepTime;
	}
	
	public Process getBaseAddress(){
		return baseAddress;
	}
	
	public void setEnd(long completionTime){
		end = completionTime;
	}
	
	public void setAttempts(int tries){
		attempts = tries;
	}
	
	public Action getAction(){
		return task;
	}

	public String toString(){		
		return pid + "\t" + task + "\t" + attempts + "\t\t" + sleepTime +"\t\t" +getStart() +"\t\t"+ getEnd(); 
	}
	private void generate(){
		Random randomNumber = new Random(System.nanoTime());
		
		int option = Math.abs(randomNumber.nextInt()) % 5;
		sleepTime = 1 + Math.abs(randomNumber.nextInt()) % 5;
//		start = Math.abs(rand2.nextInt(20));
		
		switch(option){
		case 0: 
			task = Action.ADD;
			break; 
		case 1: 
			task = Action.REMOVE; 
			break; 
		case 2: 
			task = Action.SORT; 
			break; 
		case 3: 
			task = Action.SEARCH; 
			break; 
		case 4: 
			task = Action.SUM; 
			break; 

		}
	}
	
	private static long getSeed(int forNum, int modulos) {
		int x = -1;
        long seed = 0;
        while(x!=forNum){

            Random s = new Random(seed++);
            x = s.nextInt(modulos);

        }
        return seed - 1;
	}

}
