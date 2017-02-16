package hu.beton.hilihase.jfw;

public interface ISimVariable<ValueType, EventType> {
//	void set(ValueType val) ;
//	void set(int value);

//	abstract void _set_(ValueType val);
//	abstract ValueType _get_();


	ValueType get() ;

	public void processWaitOn() ;

	public void WaitOn(EventType event, TCThread thread );


////	protected boolean isEventActive(EventType event);
//	
//
//	protected void _set_(ValueType val);
//	protected ValueType _get_();

}
