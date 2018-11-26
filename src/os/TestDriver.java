package os;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * This file is just to test code
 * 
 * @author Chinloyal
 *
 */
public class TestDriver {
	public static void main(String[] args) {
		
		
		/*class Test {
			private List<Integer> myList;
			
			Test(List<Integer> list){
				myList = list;
			}
			
			public void modify(List<Integer> list, int num) {
				list.add(num);
			}
			
			public void modify2(List<Integer> list, int num) {
				myList.add(num);
			}
		}
		
		List<Integer> list = new ArrayList<>();
		list.add(20);
		list.add(32);
		
		Test test1 = new Test(list);
		test1.modify(list, 777);
		
		
		Test test2 = new Test(list);
		test2.modify2(list, 35);
		
		
		for(Integer i : list) {
			System.out.println(i);
		}*/
		
		/*Queue q = new Queue();
		
		q.enqueue(new Process(1));
		q.enqueue(new Process(2));
		q.enqueue(new Process(3));
		
		System.out.println("start sorting...");
		q.sort("start");
		
//		Process curr = q.dequeue();
		
//		System.out.println("Just dequeued process with start time: " + curr.getStart());
		q.display();*/
		Process a = new Process(1);
		Process b = new Process(2);
		Process c = new Process(3);
		Process d = new Process(4);
		Process e = new Process(5);
		
		long start = System.currentTimeMillis();
		
		a.setStart(start);
		b.setStart(start + 2000);
		c.setStart(start + 4000);
		d.setStart(start + 6000);
		e.setStart(start + 8000);
		
		a.setSleepTime(1);
		b.setSleepTime(4);
		c.setSleepTime(4);
		d.setSleepTime(5);
		e.setSleepTime(2);
		
		Queue readyQueue = new Queue();
		readyQueue.enqueue(a);
		readyQueue.enqueue(b);
		readyQueue.enqueue(c);
		readyQueue.enqueue(d);
		readyQueue.enqueue(e);
		
		/*try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		rr(readyQueue, 3);*/
		
		System.out.printf("Hey %s my age is %d", new Object[] {"Sam", 22});
		
	}
	
	public static void rr(Queue readyQueue, int quantum) {
		readyQueue.sort("start");
		
		long endTime = 0, coreStartTime = readyQueue.getFront().getProcess().getStart(); // This var is to set the start time of the next process
		double totalWT, totalRT, totalBT, totalTAT = totalWT = totalRT = totalBT = 0;
		int initialCapacity = readyQueue.getCapacity(),
			completionTime = 0;
		Queue requestQueue = new Queue(), 
			waitingQueue = new Queue(),
			completedQueue = new Queue();
		Process current, request = current = null;
		
		while(!readyQueue.isEmpty() || !waitingQueue.isEmpty()) {
			
			
			while(!readyQueue.isEmpty()){// Add processes to the ready queue first
				current = readyQueue.getFront().getProcess();
				System.out.println(current.getSleepTime());
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
				
				//set start here
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
					long wt = (request.getStart() - request.getArrivalTime()) /1000; //The wt an rt are the same due to non-preemptive nature
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
					
					
					
					System.out.printf("%s\t%s\t%s\t%s\t%s\t%d\t%s\t%d\t%d\t%d\t%s\t%s\n", 
							request.getPID(), request.getAction(), Thread.currentThread().getName(), 
							df.format(atDate), df.format(stDate), duration, df.format(etDate), tt, wt, rt, input, output);

					endTime = request.getEnd();
					totalTAT += tt;
					totalWT += wt;//not used - 1 diff
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
					//						Date etDate = new Date(current.getEnd());
					DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
					df.setTimeZone(TimeZone.getTimeZone("EST"));

					long rt = 0;
					if(request.getEnd() == 0)
						rt = (request.getStart() - request.getArrivalTime()) /1000;
					
					System.out.printf("%s\t%s\t%s\t%s\t%s\t%d\t%s\t\t\t\t%s\t\t%s\t\t%s\t%s\t%s\n", 
							request.getPID(), request.getAction(), Thread.currentThread().getName(), 
							df.format(atDate), df.format(stDate), quantum, "-", "-", "-", "-", input, output);

					/*long wt = (request.getStart() - arrivalTime) / 1000; //The wt an rt are the same due to non-preemptive nature
					long tt = wt + quantum; // Turnaround time
*/					
					request.setEnd(end);
					completionTime += quantum;
					request.setSleepTime(request.getSleepTime() - quantum);
					endTime = end;/*
					totalTAT += tt;
					totalWT += wt;*/
					totalRT += rt;
					waitingQueue.enqueue(request);
					completedQueue.enqueue(request);
				}
			}
			
			
//			}
			
//			}while(!requestQueue.isEmpty());
		}
		completedQueue.destroy();
		System.out.println("==["+Thread.currentThread().getName()+ "]==========================");
		System.out.println("Average Response Time: " + (totalRT / initialCapacity)
				+ "\nAverage Waiting Time: " + ((totalTAT - completionTime) / initialCapacity) 
				+ "\nAverage Turnaround Time: " + (totalTAT / initialCapacity));
	}
	
	public static IntegerElement executeTask(Process process) {
		//Generate Shared list
		SharedList list = new SharedList();
		Random rand1 = new Random(System.nanoTime());
		for(int i = 0; i < 10; i++) {
			list.insertAtBack(new IntegerElement(Math.abs(rand1.nextInt() % 101)));
		}
		
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
