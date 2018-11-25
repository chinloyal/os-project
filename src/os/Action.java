package os;

public enum Action {ADD(1), REMOVE(2), SORT(3), SUM(4), SEARCH(5);
	int ordinal;
	
	Action(int ord){
		ordinal = ord;
	}
	
}
