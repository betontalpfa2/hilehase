package hu.beton.hilihase.jfw;

public abstract class TCThread extends Thread   {
//	private int ID;
	
//	TCThread(){
////		Thread th = new Thread();
//		
//	}
	
	public void waitSim(int time){
		Global.waitSim(time);
	}
	
	public abstract void test();
	
	@Override
	public void run(){
		test();
		Global.tcThreadToSleep(getID(), -1);		
	}

	long getID() {
		return this.getId();
	}
	
//	void setID(int ID) {
//		this.ID = ID; 
//	}
	
	
	

}
