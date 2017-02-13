package hu.beton.hilihase.jfw;

import java.util.ArrayList;
import java.util.List;

public class Base {

	private static Base base = null;
	private List<Signal> signals;
	private List<WaitEvent> waitEvents;
	private Sample2 connector;
	
	private int currenTime = 0;
	
	Base(){

		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary("jfw-1.0-SNAPSHOT");
		signals = new ArrayList<Signal>();
		signals.add(0, null);
		waitEvents = new ArrayList<WaitEvent>();
		base = this;
//		this.connector = connector;
	}
	
//	Sample2 getConnector(){
//		return connector;
//	}
	
	public static Base getBase() {
		if(null == base){
			System.out.println("BASE is NULLL");
			throw new NullPointerException("Base has not been initialized!");
		}
		return base;
	}

	public Signal get(int id) {
		return signals.get(id);
	}
	
	public Signal get(String name) throws IllegalArgumentException {
		for(Signal signal: signals){
			if(null == signal){
				continue;
			}
			if(signal.getName().equals(name)){
				return signal;
			}
		}
		throw new IllegalArgumentException("Cannot found signal named: " + name);
	}
	
	void register_signal(int id, String name, ValueE val){
		signals.add(id, new Signal(id, name, val));
	}

	public void read_signal(int id, ValueE value) {
		signals.get(id).set(value);
	}
	
	public synchronized void step(int currentTime) {
		this.currenTime++;
		notifyAll();
        System.out.println("Entering a new Timeslot: " + currentTime + "  " + this.currenTime);
		assert(this.currenTime == currentTime);
		

		Signal sig = get("top_x");
		if(7 == currentTime){
			sig.drive(ValueE.LOW);
		}
		if(14 == currentTime){
			sig.drive(ValueE.HIGH);
		}
		if(21 == currentTime){
			sig.drive(ValueE.LOW);
		}
//		sig.drive(ValueE.HIGH);
		
		return;
	}

	public synchronized void startTC(String  tcName) {
		Thread tc = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					/*Signal sig;
					System.out.println("Signals: " + signals );
					System.out.println("Signals size: " + signals.size() );
//					System.out.println("[ INFO ] settin exception! at "  + getTime());
					sig = get("top_x");
					sig.drive(ValueE.LOW);
					waitSim(5);
					sig.drive(ValueE.HIGH);
					waitSim(5);
					sig.drive(ValueE.LOW);
					waitSim(5);
					sig.drive(ValueE.HIGH);*/
				} catch(Exception ex){
					System.out.println("[ CRITICAL ERROR ] unhandled exception! at "  + getTime());
					ex.printStackTrace();
				}
			}
		});
		tc.start();
	}
	
	
	public synchronized void waitSim(int timeToWait){
		
		try {
			if(timeToWait < 1){
				return;
			}
			wait();
			timeToWait--;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getTime() {
		return currenTime;
	}

	public static void initBase() {
		base = new Base();
	}
	public static void destroyBase() {
		base = null;
	}


}
