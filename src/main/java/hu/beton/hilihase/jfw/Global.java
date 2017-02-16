package hu.beton.hilihase.jfw;

import java.util.ArrayList;
import java.util.List;

public class Global {
	
	enum ThreadState {
		Running,
		Wait
	}
	
	private List<ThreadStateC> tcThreadsState; // = new ArrayList<String>();
//	private int[] tcThreads;
	private boolean waitingSigTh = false;
	private static Global me = null;
	private static Time simTimeSignal;
	private static List<ISimVariable<?, ?>> signals;
	int runningCount;
	
	class ThreadStateC{
		ThreadState state;
		int waitOn;	// Negative means a stopped thread or error
//		static int runningCount;
		
		ThreadStateC(ThreadState state, int waitOn){
			set(state, waitOn);
		}
		
		public void set(ThreadState state, int waitOn) {
			setState(state);
			this.waitOn = waitOn;
		}
		
		private void setState( ThreadState state){
			if(ThreadState.Running == state & 
					ThreadState.Running != this.state)
			Global.this.runningCount++;
			if(ThreadState.Running != state & 
					ThreadState.Running == this.state)
				Global.this.runningCount--;
			this.state = state;
		}

		public void wakeIF(int on) {
			if(on==waitOn){
				setState(ThreadState.Running);
			}
		}
	}
	

	
	Global() {
		tcThreadsState  = new ArrayList<ThreadStateC>();
		signals  = new ArrayList<>();
		me = this;
		runningCount = 0;
	}
	
	static void init() {
		if (null == me)
			new Global();
	}
	
	static void cleanup(){
		me = null;
	}
	
	public static void waitTCs() {
		me._waitTCs_();
	}
	
	private synchronized void _waitTCs_() {
		if(waitingSigTh){
			return;
		}
		if(0 == getCoutOfRunningTcs()){
			return;
		}
		try {
			waitingSigTh = true;
			wait();
			waitingSigTh = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int getCoutOfRunningTcs() {
		return runningCount;
	}

	public static void wakeTCThreads(int on) {
		me._wakeTCThreads_(on);
	}

	private synchronized void _wakeTCThreads_(int on) {
		for(ThreadStateC tcth : tcThreadsState){
			tcth.wakeIF(on);
		}
	}

	public static void tcThreadToSleep(TCThread thread, int on) {
		me._tcThreadToSleep_(thread, on);
	}

	private synchronized void _tcThreadToSleep_(TCThread thread, int on) {
		tcThreadsState.get(thread.getID()).set(ThreadState.Wait, on);
		// The simulator threads aka. Signals are waiting on this class (aka.
		// Global). If there is no running TC, let wake up them. to continue the
		// simulation (maybe with the next time-step)
		if(getCoutOfRunningTcs() == 0){
			notifyAll();
		}
	}
	

	synchronized static void registerTCThread(TCThread thread) {
		me._registerTCThread_(thread);
	}
	
	synchronized void _registerTCThread_(TCThread thread) {
		thread.setID(tcThreadsState.size());
		tcThreadsState.add(new ThreadStateC(ThreadState.Running, 0));
	}

	public static void waitSim(int time, TCThread tcThread) {
		simTimeSignal.waitSim(time, tcThread);
	}

	public static Signal getSignal(int id) {
		ISimVariable<?, ?> ret1 = signals.get(id);
//		Time ret = (Time) ret1;
		return (Signal) ret1;
	}
	
	public static ISimVariable<?, ?> get(int id) {
		ISimVariable<?, ?> ret = signals.get(id);
		return ret;
	}
	
	public static Signal get(String name) throws IllegalArgumentException {
		for(ISimVariable<?, ?> signal: signals){
			
			if(null == signal){
				continue;
			}
			if(!(signal instanceof Signal)){
				continue;
			}

			Signal sig = (Signal) signal;
			
			if(sig.getName().equals(name)){
				return sig;
			}
		}
		throw new IllegalArgumentException("Cannot found signal named: " + name);
	}
	
	static void register_signal(int id, String name, ValueE val){
		signals.add(id, new Signal(id, name, val));
	}
	
	static void register_time(Time time){
		signals.add(0, time);
		simTimeSignal = time;
	}

//	public void read_signal(int id, ValueE value) {
//		signals.get(id).set(value);
//	}
	

	public static int getTime() {
		return simTimeSignal.get();
	}

	public static void create_time() {
		register_time(new Time(0));
	}


}
