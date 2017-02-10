package hu.beton.hilihase.jfw;

public class Signal {
	ValueE previous;
	ValueE val = ValueE.UNDEFINED;
	String name;
	int id;
	
	public Signal(int id, String name, ValueE val) {
		this.id = id;
		this.name = name;
		set(val);
	}

	public synchronized void set(ValueE value) {
		previous = val;
		val = value;
		notifyAll();
	}
	
	public synchronized void at(SignalEvent evt) {
		try {
			while(! check_event(evt) )
				wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean check_event(SignalEvent evt) {
		if(previous.equals(val))
			return false;
		switch (evt) {
		case POSEDGE:
			if (val.getVal()>previous.getVal()){
				return true;
			}
			return false;
		case NEGEDGE:

			if (val.getVal()<previous.getVal()){
				return true;
			}

			return false;

		default:
			return false;
		}
	}
}
