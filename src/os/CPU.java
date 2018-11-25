package os;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;

public class CPU {
	private final static int NUMBER_OF_CORES = 2; // 2 cpu cores
	private static int memory = 200 * NUMBER_OF_CORES; // 200MB of memory for each core
	private Queue jobQueue;
	private Core[] cores;
	
	public CPU(int numProcesses, SharedList list) {
		this();
		cores = new Core[NUMBER_OF_CORES];
		generateProcesses(numProcesses);
		int index = 0;
		for(Core core : cores) {
			cores[index] = new Core(index + 1, list);
			index++;
		}
		
		cores[0].setSchedulingAlgorithm("fcfs"); // Non-preemptive
		cores[1].setSchedulingAlgorithm("roundRobin"); // Preemptive
		
		if(jobQueue.getCapacity() > 0) {
			int capacity = jobQueue.getCapacity(),
				leftHalf = capacity / 2,
				rightHalf = capacity - leftHalf;
			for(int i = 0; i < leftHalf; i++) {
				cores[0].readyQueue.enqueue(jobQueue.dequeue());
			}

			for(int i = 0; i < rightHalf; i++) {
				cores[1].readyQueue.enqueue(jobQueue.dequeue());
			}
			
			
		}
	}
	private CPU() {
		jobQueue = new Queue();
	}
	
	private void generateProcesses(int numProcesses) {
		//Generate processes
		Random rand = new Random(System.nanoTime());
		Process prev = null;
		final long cpuStartTime = System.currentTimeMillis();
		for(int i = 0; i < numProcesses; i++) {	
			Process process = null;
			if(prev == null) { // First process
				process = new Process(i + 1);
				process.setStart(cpuStartTime);
				prev = process;
				
			}else { // Other Processes
				long eet = (prev.getSleepTime() * 1000) + prev.getStart();//Last process expected end time in milliseconds
				int lastEET = (int)((eet - cpuStartTime) / 1000);//Last process expected end time in seconds
				
				process = new Process(i + 1);
				process.setStart(eet - (Math.abs(rand.nextInt() % lastEET) * 1000));
				prev = process;
			}
			
			jobQueue.enqueue(process);
		}
	}
	
	public void start() {
		int index = 0;
		for(Core core : cores){
			index++;
			Thread t = new Thread(core, "CPU-" + index);
			
			t.start();
			
			
		}
	}
	
	
	public class Core implements Runnable{
		private int id;
		private SharedList list;
		private String schedulingAlgorithm;
		public Queue readyQueue;
		
		public Core() {
			readyQueue = new Queue();
		}
		
		public Core(int core_id) {
			this();
			id = core_id;
		}
		
		public Core(int core_id, SharedList list) {
			this(core_id);
			this.list = list;
		}
		

		public SharedList getList() {
			return list;
		}

		public void setList(SharedList list) {
			this.list = list;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public String getSchedulingAlgorithm() {
			return schedulingAlgorithm;
		}

		public void setSchedulingAlgorithm(String schedulingAlgorithm) {
			this.schedulingAlgorithm = schedulingAlgorithm;
		}

		public void run() {
			switch(schedulingAlgorithm) {
			case "fcfs":
				fcfs(readyQueue);
				break;
			case "roundRobin":
				roundRobin(readyQueue, 3);
				break;
			}
			
		}
		
		public void fcfs(Queue readyQueue) {
			readyQueue.sort("start"); //sort queue by start/arrival time
			
			boolean firstTime = true;
			long endTime = 0; // This var is to set the start time of the next process
			double totalTAT = 0;
			double totalWT = 0;
			int initialCapacity = readyQueue.getCapacity();
			while(!readyQueue.isEmpty()) {
				Process current = readyQueue.dequeue();
				long arrivalTime = current.getStart(); //This is when the process actually came
				long start = System.currentTimeMillis();
				
				if(!firstTime)
					current.setStart(endTime);// But this is when the process actually started due to the non preemptive nature
				
				
				IntegerElement ie = executeTask(current);
				String output, input = output = "None";
				
				if(ie != null) {
					switch(current.getAction()) {
					case ADD :
						input = ""+ie.getValue();
						break;
					case REMOVE: case SUM:
						output = ""+ie.getValue();
						break;
					case SEARCH:
						input = ""+ie.getKey();
						output = ""+ie.getValue();
						break;
					default:
						break;
					}
				}
				
				try {
					Thread.sleep(current.getSleepTime() * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				long end = System.currentTimeMillis();
				
				int duration = (int) ((end - current.getStart())  / 1000); // This is the actual time the process took to execute

				current.setEnd(end);
				
				long wt = (current.getStart() - arrivalTime) / 1000; //The wt an rt are the same due to non-preemptive nature
				long tt = wt + duration; // Turnaround time
				
				//Convert timestamps to dates
				Date atDate = new Date(arrivalTime);
				Date stDate = new Date(current.getStart());
				Date etDate = new Date(current.getEnd());
				DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
				df.setTimeZone(TimeZone.getTimeZone("EST"));
				
				System.out.printf("%s\t%s\t%s\t%s\t%s\t%d\t%s\t%d\t%d\t%d\t%s\t%s\n", 
						current.getPID(), current.getAction(), Thread.currentThread().getName(), 
						df.format(atDate), df.format(stDate), duration, df.format(etDate), tt, wt, wt, input, output);
				
				firstTime = false;
				endTime = current.getEnd();
				totalTAT += tt;
				totalWT += wt;
			}
			
			System.out.println("==["+Thread.currentThread().getName()+ "]==========================");
			System.out.println("Average Response Time: " + (totalWT / initialCapacity)
					+ "\nAverage Waiting Time: " + (totalWT / initialCapacity) 
					+ "\nAverage Turnaround Time: " + (totalTAT / initialCapacity));
			
			//Display shared list
			System.out.println("==[Shared List: After Processing]==============");
			System.out.println("Key\tValue");
			list.longDisplay();
			
		}
		
		public void roundRobin(Queue readyQueue, int quantum) {
			/*readyQueue.sort("start");
			
			boolean firstTime = true;
			long endTime = 0; // This var is to set the start time of the next process
			double totalTAT = 0;
			double totalWT = 0;
			int initialCapacity = readyQueue.getCapacity();
			
			while(!readyQueue.isEmpty()) {
				Process current = readyQueue.dequeue();
				
				if(current.getSleepTime() <= quantum) {
					IntegerElement ie = executeTask(current);
					String output, input = output = "None";
					
					if(ie != null) {
						switch(current.getAction()) {
						case ADD :
							input = ""+ie.getValue();
							break;
						case REMOVE: case SUM:
							output = ""+ie.getValue();
							break;
						case SEARCH:
							input = ""+ie.getKey();
							output = ""+ie.getValue();
							break;
						default:
							break;
						}
					}
					
					try {
						Thread.sleep(current.getSleepTime() * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					long end = System.currentTimeMillis();
					current.setEnd(end);
				}
			}*/
			
		}
		
		public IntegerElement executeTask(Process process) {
			Random rand = new Random(new Date().getTime());
			Action task = process.getAction();
			switch(task) {
			case ADD:
				IntegerElement integer = new IntegerElement(Math.abs(rand.nextInt() % 101));
				list.insertAtBack(integer);
				return integer;
			case REMOVE:
				return list.deleteFromBack();
			case SORT:
				list.setHead(
					list.sort(
						list.getHead()
					)
				);
				break;
			case SUM:
				return new IntegerElement(list.sum());
			case SEARCH:
				return list.search(Math.abs(1 + rand.nextInt() % 10));
			default:
				break;
			}
			
			return null;
		}
		
		
	}
}
