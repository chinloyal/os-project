package os;

public class IntegerElement {
	private int key;
	private int value;
	
	
	public IntegerElement(int value) {
		this.value = value;
		key = 0;
	}
	
	public IntegerElement(int value, int key) {
		this.value = value;
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
