package os;

public class Node {
	private IntegerElement data;
	private Node link;
	
	public Node() {
		this(null);
	}
	
	public Node(IntegerElement data) {
		this.data = data;
		link = null;
	}
	
	public Node(IntegerElement data, Node link) {
		this.data = data;
		this.link = link;
	}
	
	public IntegerElement getData() {
		return data;
	}
	public void setData(IntegerElement data) {
		this.data = data;
	}
	public Node getLink() {
		return link;
	}
	public void setLink(Node link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return "Node [data=" + data + ", link=" + 
	   (link == null ? "null" : "not null")
	+ "]";
	}
	
	public void display() {
		System.out.println(toString());
	}
	
}
