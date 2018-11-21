package os;

/**
 * This file is just to test code
 * 
 * @author Chinloyal
 *
 */
public class TestDriver {
	public static void main(String[] args) {
		SharedList sl = new SharedList();
		
		sl.insertAtBack(new IntegerElement(200, 1));
		sl.insertAtBack(new IntegerElement(25, 2));
		sl.insertAtBack(new IntegerElement(473, 3));
		sl.insertAtBack(new IntegerElement(23, 4));
		sl.insertAtBack(new IntegerElement(390, 5));
		
		System.out.println("Starting...");
		
//		System.out.println(sl.getHead().getData().getValue());
		
		sl.display();
//		System.out.println(sl.sum());
		
//		System.out.println("Search: "+ sl.search(4).getValue());
		
		System.out.println("Done!");
	}
}
