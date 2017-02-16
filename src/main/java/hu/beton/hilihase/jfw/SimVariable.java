package hu.beton.hilihase.jfw;

public abstract  class SimVariable<ValueType, EventType> implements ISimVariable<ValueType, EventType> {
	/**
	 * ID: unique global identifier.
	 * Its the same for sysverilog communication and in JAVA  
	 */
	private final int ID;
//	private static int maxID;
	
	SimVariable(int ID){
		this.ID = ID;
	}
	
	public void set(ValueType val) {
		synchronized (this) {
			_set_(val);
		}
		processWaitOn();
	}

	protected abstract void _set_(ValueType val);
	protected abstract ValueType _get_();
	abstract boolean isEventActive(EventType event);


	public  synchronized ValueType get() {
		return _get_();
	}

	public void processWaitOn() {
		synchronized (this) {
			notifyAll();
			Global.wakeTCThreads(this.ID);
		}
		Global.waitTCs();
		return;
	}

	public synchronized void WaitOn(EventType event, TCThread thread ) {
		if(isEventActive(event)){
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
			if(isEventActive(event)){
				return;
			}
		}
	}
	
//	@Override
//	public void set(int value) {
//		set(ValueType.valueOf(value));
//	}

}
