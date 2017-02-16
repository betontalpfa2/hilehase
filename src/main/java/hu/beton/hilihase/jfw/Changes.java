package hu.beton.hilihase.jfw;

class Changes {
	ValueE level;
	int time;
	
	public Changes(ValueE val) {
		this(val, Global.getTime());
	}
	
	public Changes(ValueE val, int time) {
		set(val, time);
	}
	
	void set(ValueE val, int time){
		this.level = val;
		this.time = time;
	}
}
