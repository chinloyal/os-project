package os;

import java.util.Iterator;
import java.util.regex.Pattern;

public class SharedList implements Iterable<IntegerElement> {
	private Node head;

	public SharedList() {
		head = null;
	}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}
	public boolean isEmpty() {
		return head == null;
	}

	public boolean isFull() {
		return new Node() == null;
	}

	public synchronized void insertAtBack(IntegerElement elem) {
		if (isFull()) {
			System.out.println("Unable to insert node into full list");
		} else {
			Node temp = new Node(elem);
			if (isEmpty()) {
				head = temp;
			} else {
				Node current = head;
				while (current.getLink() != null) {
					current = current.getLink();
				}
				current.setLink(temp);
			}
		}

	}

	public synchronized IntegerElement deleteFromBack() {
		IntegerElement elem = null;
		if (isEmpty()) {
			System.out.println("List is empty");
		} else {
			// check if only one element in list
			if (head.getLink() == null) {
				elem = head.getData();
				head = null;
			} else {
				Node prev = head;
				Node current = head;
				while (current.getLink() != null) {
					prev = current;
					current = current.getLink();
				}
				prev.setLink(null);
				elem = current.getData(); // get data before deleting
				current = null;// delete current;
			}
		}
		return elem;
	}

	public void insertAtFront(IntegerElement elem) {
		if (isFull()) {
			System.out.println("List is full");
		} else {
			Node temp = new Node(elem);
			if (isEmpty()) {
				head = temp;
			} else {
				// let new node link to the current beginning of list
				temp.setLink(head);
				// let head store the address of the new node
				head = temp;
			}
		}
	}

	public IntegerElement deleteFromFront() {
		IntegerElement elem = null;
		if (isEmpty()) {
			System.out.println("List is empty");
		} else {
			Node current = head;
			head = current.getLink();
			elem = current.getData();
			current = null;
		}
		return elem;
	}

	/***
	 * Search for a IntegerElement by key
	 * 
	 * @param key  - key of IntegerElement
	 */
	public IntegerElement search(int key) {
		if(isEmpty()) {
			System.out.println("List is empty, integer element with key "+ key + " is not found.");
			return null;
		}
		
		for(IntegerElement i: this)
			if(i.getKey() == key)
				return i;
		
		return null;
	}
	
	public void display() {
		StringBuilder result = new StringBuilder();
		if(isEmpty())
			return;
		
		for(IntegerElement i: this) {
			result.append("{" + i.getKey() + ": " + i.getValue() + "}" + "->");
		}
		System.out.println(result.toString().replaceFirst("\\->$", ""));
	}

	public void destroy() {
		if (isEmpty()) {
			System.out.println("List is empty");
		} else {
			Node prev = head;
			Node current = head;
			while (current != null) {
				prev = current;
				current = current.getLink();
				prev = null;
			}
			head = null;
		}
	}
	
	public synchronized Node getMiddle(Node node) {
        if (node == null) 
            return node;
        
        Node fastptr = node.getLink(); 
        Node slowptr = node; 
          
        // Move fastptr by two and slow ptr by one 
        // Finally slowptr will point to middle node 
        while (fastptr != null) { 
            fastptr = fastptr.getLink(); 
            if(fastptr != null) { 
                slowptr = slowptr.getLink(); 
                fastptr = fastptr.getLink(); 
            } 
        } 
        
        return slowptr; 
    }
	
	public synchronized Node merge(Node left, Node right)  { 
		
		Node tempHead = new Node(), curr;
		
		curr = tempHead;
		
		while(left != null && right != null) {
			if(left.getData().getValue() <= right.getData().getValue()) {
				curr.setLink(left);
				left = left.getLink();
			}else {
				curr.setLink(right);
				right = right.getLink();
			}
			
			curr = curr.getLink();
		}
		
		curr.setLink((left == null) ? right : left);
		
		return tempHead.getLink();
  
    }
	
	/**
	 * This method utilizes merge sort algorithm
	 * 
	 * @param node - Pass in head first
	 * 
	 */
	public synchronized Node sort(Node node) {
        // Base case : if head is null 
        if (node == null || node.getLink() == null)
            return node; 
  
        // get the middle of the list 
        Node middle = getMiddle(node);
        Node nextofmiddle = middle.getLink();
        
        // set the next of middle node to null 
        middle.setLink(null);
  
        // Merge the left and right lists 
        return merge(sort(node), sort(nextofmiddle));
    }

	@Override
	public Iterator<IntegerElement> iterator() {
		
		return new Iterator<IntegerElement>(){
			Node current = head;
			
			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public IntegerElement next() {
				if(hasNext()) {
					IntegerElement data = current.getData();
					current = current.getLink();
					
					return data;
				}
				return null;
			}
			
		};
	}
	
	public int sum() {
		int result = 0;
		if(isEmpty())
			return result;
		
		for(IntegerElement i : this) {
			result += i.getValue();
		}
		
		return result;
	}
}
