package hu.beton.hilihase.jfw;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Signal extends SimVariable<ValueE, SignalEvent> {
	private List<Changes> vals;
	//	private List<ValueE> vals;
	String name;
	//	ValueE val2;

	Signal(int ID, String name, ValueE val) {
		super(ID);
		vals = new ArrayList<Changes>();
		this.name = name;
		_set_(val);
	}

	@Override
	protected void _set_(ValueE val) {
		this.vals.add(new Changes(val));
	}

	@Override
	protected ValueE _get_() {
		return _get_(0);
		//		return vals.get(vals.size()-1);
	}

	protected ValueE _get_(int fromNow) {
		return _get_at_(Global.getTime()+fromNow);
	}


	protected ValueE _get_at_(int time) {
		try{
			ListIterator<Changes> iter = vals.listIterator(vals.size());

			while (true) {
				Changes ch = iter.previous();
				if(ch.time<=time){	// Throws NoSuchElementException 
					return ch.level; 
				}
			}
		}catch(Exception ex){
			return ValueE.UNDEFINED;
		}
	}


	@Override
	boolean isEventActive(SignalEvent event) {
		switch (event) {
		case POSEDGE:
			boolean ret = _get_(-1) == ValueE.LOW & _get_(0) != ValueE.LOW ;
			return ret;
		case NEGEDGE:
			return _get_(-1) == ValueE.HIGH & _get_(0) != ValueE.HIGH ;
		default:
			throw new IllegalArgumentException("Event type is not supported or not implemented.");
//			break;
		}
//		return event.equals(_get_());
	}

	public String getName() {
		return name;
	}

}
