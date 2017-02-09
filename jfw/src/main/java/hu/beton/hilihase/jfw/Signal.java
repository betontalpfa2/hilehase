package hu.beton.hilihase.jfw;

public class Signal {
	Value val;
	String name;
	int id;
	
	public Signal(int id, String name, Value val) {
		this.id = id;
		this.name = name;
		this.val = val;
	}

	public void set(Value value) {
		val = value;
	}
}
