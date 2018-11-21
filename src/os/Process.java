package os;

import java.util.Date;
import java.util.Random;
public class Process {

	private enum Action{ADD, REMOVE, SORT,SUM, SEARCH}; 

	private int pid; // process id
	private Action task; //task process will do
	private int attempts; //number of attempts for lock
	private int  start; //time marking of the start of a process / or arrival time
	private int end; // time marking the end of a process
	private int sleepTime; //stores how long a process should sleep / or burst time
	private Process baseAddress; //stores the address of current process
	Process nextProcess; 
	Process previousProcess; 

	public Process(int PID)
	{
		pid = PID; 
		generate(); 
		attempts = 0; 
		end = -1;
		baseAddress = this; 
		nextProcess = null; 
		previousProcess = null;
	}

	public Process getPreviousProcess() {return previousProcess;}
	public void setPreviousProcess(Process previous) {previousProcess = previous;	}
	public int getPID(){return pid;}
	public int getAttempts(){return attempts;}
	public int getStart(){return start;}
	public int getEnd(){return end;}
	public int getSleepTime(){return sleepTime;}
	public Process getBaseAddress(){return baseAddress;}
	public void setEnd(int completionTime){end = completionTime;}
	public void setAttempts(int tries){attempts = tries;}
	public Process getNextProcess(){return nextProcess;}
	public void setNextProcess(Process next){nextProcess = next;}
	public int getAction(){return task.ordinal();}

	public String toString()
	{
		return pid + "\t" + task.ordinal()+ "\t" + attempts + "\t\t" + sleepTime +"\t\t" + getStart() +"\t\t"+ getEnd(); 
	}
	private void generate()
	{
		Random randomNumber = new Random(); 
		randomNumber.setSeed(new Date().getTime());

		int option = randomNumber.nextInt() % 5;
		sleepTime = 1 + randomNumber.nextInt() % 5; 
		start = 1 + randomNumber.nextInt() % 60; 
		switch(option)
		{
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

}
