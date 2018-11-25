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
		Action task = Action.ADD;
		
		System.out.println(task.ordinal);
	}
}
