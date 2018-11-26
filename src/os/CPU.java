package os;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class CPU {
	private final static int NUMBER_OF_CORES = 2; // 2 cpu cores
	private static int memory = 200 * NUMBER_OF_CORES; // 200MB of memory for each core
	private Queue jobQueue;
	private Core[] cores;
	private List<CPURecord> records;
	private SharedList cpuList;
	
	public CPU(int numProcesses, SharedList list) {
		this();
		cores = new Core[NUMBER_OF_CORES];
		cpuList = list;
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
		records = new ArrayList<>();
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
				process.setStart(eet - ((Math.abs(rand.nextInt() % lastEET) * 1000)));
				prev = process;
			}
			
			jobQueue.enqueue(process);
		}
	}
	
	public void start() {
		int index = 0;
		Thread [] coreThreads = new Thread[NUMBER_OF_CORES];
		
		for(Core core : cores){
			coreThreads[index] = new Thread(core, "CPU-" + (index + 1));
			coreThreads[index].start();
			index++;
		}
		
		for(Thread t : coreThreads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(CPURecord rec : records) {
			System.out.println(Report.writeln("==["+rec.getCPUName()+ "]=========================="));
			System.out.printf("Average Response Time: %.2f"
					+ "\nAverage Waiting Time: %.2f"
					+ "\nAverage Turnaround Time: %.2f"
					+ "\nAlgorithm: %s\n", rec.getResponseTime(), rec.getWaitingTime(), rec.getTurnAroundTime(), rec.getAlgorithm());
			
			Report.writef("Average Response Time: %.2f"
					+ "\r\nAverage Waiting Time: %.2f"
					+ "\r\nAverage Turnaround Time: %.2f"
					+ "\r\nAlgorithm: %s\r\n", rec.getResponseTime(), rec.getWaitingTime(), rec.getTurnAroundTime(), rec.getAlgorithm());
		}
		
		
		
		
		//Display shared list
		System.out.println(Report.writeln("==[Shared List: After Processing]=============="));
		System.out.println(Report.writeln("Key\tValue"));
		Report.writeln(cpuList.longDisplay());
		
		Report.close();
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
				CPURecord rec1 = fcfs(readyQueue);
				rec1.setAlgorithm("First Come First Serve");
				records.add(rec1);
				break;
			case "roundRobin":
				CPURecord rec2 = roundRobin(readyQueue, 3);
				rec2.setAlgorithm("Round Robin");
				records.add(rec2);
				break;
			}
			
		}
		
		public CPURecord fcfs(Queue readyQueue) {
			readyQueue.sort("start"); //sort queue by start/arrival time
			
			boolean firstTime = true;
			long endTime = 0; // This var is to set the start time of the next process
			double totalTAT = 0;
			double totalWT = 0;
			int initialCapacity = readyQueue.getCapacity();
			while(!readyQueue.isEmpty()) {
				Process current = readyQueue.dequeue();
				long arrivalTime = current.getStart(); //This is when the process actually came
				
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
				long end = current.getStart() + (current.getSleepTime() * 1000);
				
				int duration = (int) ((end - current.getStart())  / 1000); // This is the actual time the process took to execute

				current.setEnd(end);
				
				long tt = (end - arrivalTime) / 1000; // Turnaround time
				long wt = (tt - current.getSleepTime()); //The wt an rt are the same due to non-preemptive nature
				
				
				//Convert timestamps to dates
				Date atDate = new Date(arrivalTime);
				Date stDate = new Date(current.getStart());
				Date etDate = new Date(current.getEnd());
				DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
				df.setTimeZone(TimeZone.getTimeZone("EST"));
				
				Report.writef("%s\t%s\t%s\t%s\t%s\t%d\t%s\t%d\t%d\t%d\t%s\t%s\r\n", 
						current.getPID(), current.getAction(), Thread.currentThread().getName(), 
						df.format(atDate), df.format(stDate), duration, df.format(etDate), tt, wt, wt, input, output);
				
				System.out.printf("%s\t%s\t%s\t%s\t%s\t%d\t%s\t%d\t%d\t%d\t%s\t%s\n", 
						current.getPID(), current.getAction(), Thread.currentThread().getName(), 
						df.format(atDate), df.format(stDate), duration, df.format(etDate), tt, wt, wt, input, output);
				
				firstTime = false;
				endTime = current.getEnd();
				totalTAT += tt;
				totalWT += wt;
			}
			
			CPURecord rec = new CPURecord();
			rec.setCPUName(Thread.currentThread().getName());
			rec.setResponseTime(totalWT / initialCapacity);
			rec.setTurnAroundTime(totalTAT / initialCapacity);
			rec.setWaitingTime(totalWT / initialCapacity);
		
			return rec;
			
		}
		
		public CPURecord roundRobin(Queue readyQueue, int quantum) {
			readyQueue.sort("start");
			
			long coreStartTime = readyQueue.getFront().getProcess().getStart(); // This var is to set the start time of the next process
			double totalTAT, totalRT = totalTAT = 0;
			int initialCapacity = readyQueue.getCapacity(),
				completionTime = 0;
			Queue requestQueue = new Queue(), 
				waitingQueue = new Queue(),
				completedQueue = new Queue();
			Process current, request = current = null;
			
			while(!readyQueue.isEmpty() || !waitingQueue.isEmpty()) {
				
				
				while(!readyQueue.isEmpty()){// Add processes to the ready queue first
					current = readyQueue.getFront().getProcess();
					long arrivalTime = current.getStart(); //This is when the process actually came
					
					if(((arrivalTime - coreStartTime) / 1000) <= completionTime) {
						requestQueue.enqueue(current);
						readyQueue.dequeue();
					}else {
						break;
					}
				}
				
				while(!waitingQueue.isEmpty()) {
					requestQueue.enqueue(waitingQueue.dequeue());
				}
				
				while(!requestQueue.isEmpty()) {
					request = requestQueue.dequeue();
					if(request.getArrivalTime() == 0)
						request.setArrivalTime(request.getStart());
					
					if(!completedQueue.isEmpty())
						request.setStart(completedQueue.dequeue().getEnd());
					
					if(request.getSleepTime() <= quantum) {
						IntegerElement ie = executeTask(request);
						String output, input = output = "None";

						if(ie != null) {
							switch(request.getAction()) {
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
							Thread.sleep(request.getSleepTime() * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						long end = request.getStart() + (request.getSleepTime() * 1000);

						int duration = (int) ((end - request.getStart())  / 1000); // This is the actual time the process took to execute
						
						long tt = (end - request.getArrivalTime()) / 1000; // Turnaround time
						long wt = (request.getStart() - request.getArrivalTime()) /1000;
						long rt = 0;
						if(request.getEnd() == 0)
							rt = (request.getStart() - request.getArrivalTime()) /1000;
						
						request.setEnd(end);


						//Convert timestamps to dates
						Date atDate = new Date(request.getArrivalTime());
						Date stDate = new Date(request.getStart());
						Date etDate = new Date(request.getEnd());
						DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
						df.setTimeZone(TimeZone.getTimeZone("EST"));
						
						Report.writef("%s\t%s\t%s\t%s\t%s\t%d\t%s\t%d\t%d\t%d\t%s\t%s\r\n", 
								request.getPID(), request.getAction(), Thread.currentThread().getName(), 
								df.format(atDate), df.format(stDate), duration, df.format(etDate), tt, wt, rt, input, output);
						
						System.out.printf("%s\t%s\t%s\t%s\t%s\t%d\t%s\t%d\t%d\t%d\t%s\t%s\n", 
								request.getPID(), request.getAction(), Thread.currentThread().getName(), 
								df.format(atDate), df.format(stDate), duration, df.format(etDate), tt, wt, rt, input, output);

						totalTAT += tt;
						totalRT += rt;
						completionTime += duration;
						completedQueue.enqueue(request);
					}else { // Greater than
						String output, input = output = "None";
						try {
							Thread.sleep(quantum * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						long end = request.getStart() + (request.getSleepTime() * 1000);
						//Convert timestamps to dates
						Date atDate = new Date(request.getArrivalTime());
						Date stDate = new Date(request.getStart());
						
						DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
						df.setTimeZone(TimeZone.getTimeZone("EST"));

						long rt = 0;
						if(request.getEnd() == 0)
							rt = (request.getStart() - request.getArrivalTime()) /1000;
						
						Report.writef("%s\t%s\t%s\t%s\t%s\t%d\t%s\t\t\t\t%s\t%s\t%s\t%s\t%s\r\n", 
								request.getPID(), request.getAction(), Thread.currentThread().getName(), 
								df.format(atDate), df.format(stDate), quantum, "-", "-", "-", "-", input, output);
						
						System.out.printf("%s\t%s\t%s\t%s\t%s\t%d\t%s\t\t\t\t%s\t%s\t%s\t%s\t%s\n", 
								request.getPID(), request.getAction(), Thread.currentThread().getName(), 
								df.format(atDate), df.format(stDate), quantum, "-", "-", "-", "-", input, output);
						
						request.setEnd(end);
						completionTime += quantum;
						request.setSleepTime(request.getSleepTime() - quantum);
						totalRT += rt;
						waitingQueue.enqueue(request);
						completedQueue.enqueue(request);
					}
					
				}
				
				
			}
			completedQueue.destroy();
			
			CPURecord rec = new CPURecord();
			rec.setCPUName(Thread.currentThread().getName());
			rec.setResponseTime(totalRT / initialCapacity);
			rec.setTurnAroundTime(totalTAT / initialCapacity);
			rec.setWaitingTime(((totalTAT - completionTime) / initialCapacity));
			return rec;
		}
		
		public IntegerElement executeTask(Process process) {
			Random rand = new Random(process.getPID() + System.nanoTime());
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
