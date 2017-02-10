package hu.beton.hilihase.jfw;

public class Signal {
	private ValueE previous;
	private ValueE val = ValueE.UNDEFINED;
	private int lastChange;
	private String name;
	private int id;
	
	public Signal(int id, String name, ValueE val) {
		this.id = id;
		this.name = name;
		set(val);
	}
	

	public synchronized void drive(ValueE value) {
//		Base.getBase().getConnector().hilihase_drve(id, (byte) value.getVal());
		Sample2.hilihase_drve(id, (byte) value.getVal());
		
//		set(value);
	}



	public synchronized void set(ValueE value) {
		if(previous == val){
			return;
		}
		previous = val;
		val = value;
		lastChange = Base.getBase().getTime();
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
	
	public synchronized byte drive(){
		try {
			while(true){
				if(lastChange == Base.getBase().getTime())
					return (byte) val.getVal();
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (byte) 255;
		}
	}

	public String getName() {
		return name;
	}
}
