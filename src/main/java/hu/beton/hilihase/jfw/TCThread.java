package hu.beton.hilihase.jfw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;


public abstract class TCThread extends Thread implements IJunitHandler   {
	IJunitHandler handler = null;
	/**
	 * Stores sub test threads errors.
	 */
	private List<AssertionError> errors;
	
	private boolean parentFlag = false;

	public TCThread() {
		setName(getClass().getName());
	}
	
	public void waitSim(int time){
		Global.waitSim(time);
	}

	public abstract void test();

	@Override
	public void run(){
		if(!parentFlag){
			throw new AssertionError("Parent flag!!!");
		}
		try{
//			Global.waitsetup();
			test();
		} catch (AssertionError ex){
			handler.handle(ex);
		}
		Global.tcThreadToSleep(getID(), -1);
//		analize();
	}

	private void analize() {
		ListIterator<AssertionError> iter = errors.listIterator(errors.size());

		while (iter.hasPrevious()) {
			AssertionError err = iter.previous();
			err.printStackTrace();
			if(iter.previousIndex() == -1){
				throw err;
			}
		}
	}

	long getID() {
		return this.getId();
	}

	public void handle(AssertionError err){
		if(null == handler || this == handler){
			errors.add(err);
		}
		else{
			handler.handle(err);
		}
	}

	void setParent(IJunitHandler handler){
		parentFlag = true;
		if(null == handler || this ==handler){
			errors = Collections.synchronizedList(new ArrayList<AssertionError>());
		}
		this.handler = handler;
	}
	
	

	public void Me(String toplevelName) {
		Global.init(Mode.JUnitTest, false);		//TODO
//		setParent(null);
		Global.registerTCThread(this, this);
//		start();
		Global.startHDLSim(toplevelName);
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		analize();
//		run();
	}

	public IJunitHandler getParent() {
		return handler;
	}

	

}
