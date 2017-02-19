package hu.beton.hilihase.jfw;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public abstract  class SimVariable<ValueType, EventType> implements ISimVariable<ValueType, EventType> {
	/**
	 * ID: unique global identifier.
	 * Its the same for system-verilog communication and in JAVA  
	 */
	final int ID;

	/**
	 * See get() for more details.
	 */
	Map<Long, Boolean> getValueCurent;

	/**
	 * A thread can pass only maximum one event in each time cycle on each signal.
	 */
	Map<Long, Boolean> activateEventCurrent;


	SimVariable(int ID){
		this.ID = ID;
		activateEventCurrent = new Hashtable<Long, Boolean>();
		getValueCurent = new Hashtable<Long, Boolean>();
	}

	void set(ValueType val) {
		synchronized (this) {
			_set_(val);
		}
		processWaitOn();
		Global.processDriveList();
	}

	protected abstract void _set_(ValueType val);
	@Deprecated
	protected abstract ValueType _get_();
	protected abstract ValueType _get_(int fromNow);
	abstract boolean isEventActive(EventType event);


	/**
	 * get():
	 * returns the value of the current (aka. new) or the last time cycle.:
	 * 	Current:
	 * 		- If the signal had been changed by wait for time. This means that
	 * 			if a TC has wait on time and drive a given signal after this
	 * 			wait the get() will returns the current (new) value of this
	 * 			signal for all TC thread in the current time cycle.
	 * 		- If a TCthread waits on this signal the get() will returns the
	 * 			current (new) value of this signal for that (not all) TC thread
	 * 			in the current time cycle.
	 * 		- Assertions and display, and any passive expression can uses the
	 * 			new value.
	 * 		- If a thread set this signal (Not implemented, blocking...)
	 * 	Last time value:
	 * 		- Any other case. Ex.: another TC thread set the value, the
	 * 		simulation sets the value etc.
	 */
	public  synchronized ValueType get() {
		if(amITheTime()){
			return _get_();
		}

		try{
			long id = Thread.currentThread().getId();
//		    System.out.println("GET: thread:" + th.getClass());
//			TCThread tct = (TCThread) Thread.currentThread();
			if(getValueCurent.get(id)){
				return _get_(0);
			}
			return _get_(-1);
		} catch (Exception ex){
			return _get_(-1);
		}
	}

//	void fill(Collection<Boolean> coll, boolean val){
//		for(Boolean elem : coll){
//			elem = val;
//		}
//	}
	
	void step(){
		for (Entry<Long, Boolean> entry : activateEventCurrent.entrySet()) {
			entry.setValue(Boolean.TRUE);
		}
		for (Entry<Long, Boolean> entry : getValueCurent.entrySet()) {
			entry.setValue(Boolean.FALSE);
		}
//		activateEventCurrent.replaceAll((k, v) -> Boolean.TRUE);
//		getValueCurent.replaceAll((k, v) -> Boolean.FALSE);
			
//		Collections.fill(activateEventCurrent.values(), Boolean.TRUE);
//		Collections.fill(getValueCurent.values(), Boolean.FALSE);
	}
	/*
	public  synchronized ValueType get(int ) {
		return _get_();
	}
	 */
	public void processWaitOn() {
		synchronized (this) {
			notifyAll();
			Global.wakeTCThreads(this.ID);
		}
		Global.waitTCs();
		return;
	}

	public synchronized void WaitOn(EventType event, TCThread thread ) {
		if (isEventActive(event, thread.getID())){
			return;
		}

		Global.tcThreadToSleep(thread, ID);

		while(true){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isEventActive(event, thread.getID())){
				return;
			}
		}
	}

	private boolean isEventActive(EventType event, long l) {
		if(activateEventCurrent.get(l)){
			if(isEventActive(event)){
				activateEventCurrent.replace(l, false);
				getValueCurent.replace(l, true);
				if(amITheTime()){
					for (Entry<Long, Boolean> entry : activateEventCurrent.entrySet()) {
						entry.setValue(Boolean.TRUE);
					}
//					activateEventCurrent.replaceAll((k, v) -> Boolean.TRUE);
//					Collections.fill(getValueCurent, true);
				}				
				return true;
			}
		}
		return false;
	}

	private boolean amITheTime() {
		return 0 == ID;
	}

	protected void set(int value){
		ValueType vt = valueOf(value);
		set(vt);
	}

	abstract ValueType valueOf(int value);

	public abstract void drive(ValueE high);

	public void drive(int val) {
		Global.addDriveList(ID, val);
	}

	public void registerTCThread(long id) {
		activateEventCurrent.put(id, true);
		getValueCurent.put(id, false);
	}

	//	public void set(int value) {
	//		// TODO Auto-generated method stub
	//		
	//	}

	//	@Override
	//	public void set(int value) {
	//		set(ValueType.valueOf(value));
	//	}

}
