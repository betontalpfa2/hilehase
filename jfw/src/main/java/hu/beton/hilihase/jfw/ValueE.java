package hu.beton.hilihase.jfw;

public enum ValueE {

	LOW		(0),
	HIGH	(1),
	UNDEFINED	(2),
	HIGZ	(3);
	
	private final int val;
	ValueE(int a){
		this.val = a;
	}
	public int getVal() {
		return val;
	}
	
	static ValueE ValueOf(int val){
		return ValueE.values()[val];
	}
		
}
