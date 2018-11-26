package os;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		boolean exit = false;
		
		do {
			Scanner sc = new Scanner(System.in);
			int numProcesses = 0;
			System.out.print("Enter the amount of processes [min: 10, max: 30]\n> ");
			
			try {
				numProcesses = sc.nextInt();
				
			}catch(InputMismatchException e) {
				System.out.println("Invalid argument, please try again.");
				continue;
			}
			
			if(numProcesses >= 10 && numProcesses <= 30) {
				
				//Generate Shared list
				SharedList list = new SharedList();
				Random rand = new Random(System.nanoTime());
				for(int i = 0; i < 10; i++) {
					list.insertAtBack(new IntegerElement(Math.abs(rand.nextInt() % 101)));
				}
				
				//Display shared list
				System.out.println(Report.writeln("==[Shared List: Before Processing]=============="));
				System.out.println(Report.writeln("Key\tValue"));
				Report.writeln(list.longDisplay());
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
				//Run CPU
				CPU cpu = new CPU(numProcesses, list);
				cpu.start();
				exit = true;
			}else {
				System.out.println("Please enter a valid amount of processes!");
				continue;
			}
			
			
		}while(!exit);
	}
}
