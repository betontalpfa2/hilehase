package hu.beton.hilihase.jfw;

public abstract class TCThread implements Runnable {
	private int ID;
	
	void waitSim(int time){
		Global.waitSim(time, this);
	}
	
	@Override
	public abstract void run();

	int getID() {
		return this.ID;
	}
	
	void setID(int ID) {
		this.ID = ID; 
	}

}
