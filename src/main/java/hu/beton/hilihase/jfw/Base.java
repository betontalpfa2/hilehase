package hu.beton.hilihase.jfw;

import java.util.ArrayList;
import java.util.List;

public class Base {

	static Base base = null;
	List<Signal> signals;
	
	Base(){
		signals = new ArrayList<Signal>();
		base = this;
	}
	
	public static Base getBase() {
		return base;
	}
	
	void register_signal(int id, String name, ValueE val){
		signals.add(id, new Signal(id, name, val));
	}

	public void read_signal(int id, ValueE value) {
		signals.get(id).set(value);
	}
}
