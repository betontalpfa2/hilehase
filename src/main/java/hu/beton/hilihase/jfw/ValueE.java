package hu.beton.hilihase.jfw;

public class ValueE {
	public final static int low = 0;
	public final static int high = 1;
	public final static int undefined = 2;
	public final static int highz = 3;
	
	public final static ValueE LOW = new ValueE(0);
	public final static ValueE HIGH = new ValueE(1);
	public final static ValueE UNDEFINED = new ValueE(2);
	public final static ValueE HIGZ = new ValueE(3);
	
	private int val;
	ValueE(int a){
		set(a);
	}
	public int getVal() {
		return val;
	}
	
	public int toInteger() {
		return getVal();
	}
//	@Override
	public void set(int val) {
		this.val = val;
	}
		
//	@Override
	public boolean equals(int i) {
		return i == val;
	}
	
	public boolean equals(ValueE other) {
		return other.val == this.val;
	}
}
