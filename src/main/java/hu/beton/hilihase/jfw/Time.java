package hu.beton.hilihase.jfw;

public class Time extends SimVariable<Integer, Integer> {
	private int time;
	
	Time(int ID) {
		super(ID);
	}

	@Override
	protected void _set_(Integer val) {
		this.time = val;
	}

	@Override
	protected Integer _get_() {
		return time;
	}

	public void waitSim(int time, TCThread tcThread) {
		WaitOn(time, tcThread);		
	}

	@Override
	boolean isEventActive(Integer event) {
		return event.equals(_get_());
	}

	@Override
	public void set(int value) {
		set(Integer.valueOf(value));
	}

	@Override
	Integer valueOf(int value) {
		return new Integer(value);
	}

	@Override
	public void drive(ValueE high) {
		assert(false); // Time cannot be driven...
	}

	@Override
	protected Integer _get_(int fromNow)  {
		throw new IllegalAccessError("Wron useage of time");
//		return null;
	}

	

}
