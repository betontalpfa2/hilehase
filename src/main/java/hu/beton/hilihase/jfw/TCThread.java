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
//		Global.registerTCThread(this);
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
		finished();
//		analize();
	}

	private void finished() {
		Global.succesFinish(this);
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
		/*if(Global.getSuccesFinished() != Global.getThreadCount()){
			throw new AssertionError("Some threads hasnot finished correctly...");
		}*/
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
			this.handler = this;
			return;
		}
		this.handler = handler;
	}
	
	

	public void startHDLSim(String toplevelName, boolean loadLibraries) {
		Global.init(Mode.JUnitTestVirtual, loadLibraries);		//TODO
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

	public void startJUnitTest() {
		Global.init(Mode.JUnitTest2, true);		//TODO
//		setParent(null);
		Global.registerTCThread(this, this);
		start();
//		Global.startHDLSim(toplevelName);
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		analize();
//		run();
	}
	

}
