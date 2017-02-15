package hu.beton.hilihase.jfw;

public abstract  class SimVariable {
	
	
	protected synchronized void set(int val) {
		_set_();
		processWaitOn();
	}

	protected abstract void _set_();
	protected abstract int _get_();


	protected synchronized int get() {
		return _get_();
	}

	@Override
	public
	synchronized void processWaitOn() {
		notifyAll();
		Global.waitTCs();
		return;
	}

	@Override
	public synchronized void WaitOn(Integer event, Object thread ) { //TODO void?
		if(isEventActive(event))
			return;
		
		Global.changeControl(thread, this);

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

	private boolean isEventActive(Integer event) {
		// TODO Auto-generated method stub
		return event.equals(val);
		//return false;
	}
}
