package hu.beton.hilihase.jfw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Global {

	enum ThreadState {
		Running,
		Wait
	}
	
	

	private static List<TCThreadStateC> tcThreadsState; // = new ArrayList<String>();
	//	private int[] tcThreads;
	private boolean waitingSigTh = false;
	private static Global me = null;
	private static Time simTimeSignal;
	private static List<SimVariable<?, ?>> signals;
	private static List<SignalDrv> signalDrvQueue;
	int runningCount;
	String testcasePath = null;
	static boolean  loadLibraries = true;

	class TCThreadStateC{
		ThreadState state;
		int waitOn;// Negative means a stopped thread or error
		//		static int runningCount;
		final TCThread tct;

		TCThreadStateC(ThreadState state, int waitOn, TCThread tct){
			set(state, waitOn);
			this.tct = tct;
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



	Global(boolean loadlibraries) {
		Global.loadLibraries = loadlibraries;
		if(loadlibraries){
			System.out.println(System.getProperty("java.library.path"));
			System.loadLibrary("jfw-1.0-SNAPSHOT");
		}
		else{
			System.err.println("WARNING: Libraries wasn't loaded. Use this mod is only for test/debug.");
		}
		tcThreadsState  = new ArrayList<TCThreadStateC>();
		signals  = new ArrayList<SimVariable<?, ?>>();
		register_time(new Time(0));
		me = this;
		runningCount = 0;
		signalDrvQueue = Collections.synchronizedList(new ArrayList<SignalDrv>());
	}

	static void init(){
		init(true);
	}
	
	static void init(boolean loadlibraries) {
		if (null == me){
//			Global.loadLibraries = loadlibraries;
			new Global(loadlibraries);
		}
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
		for(TCThreadStateC tcth : tcThreadsState){
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
		tcThreadsState.add(new TCThreadStateC(ThreadState.Running, 0, thread));
	}

	public static void waitSim(int time, TCThread tcThread) {
		simTimeSignal.waitSim(time, tcThread);
	}

	public static Signal getSignal(int id) {
		SimVariable<?, ?> ret1 = signals.get(id);
		//		Time ret = (Time) ret1;
		return (Signal) ret1;
	}

	public static SimVariable<?, ?> get(int id) {
		SimVariable<?, ?> ret = signals.get(id);
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

	public static void read_signal(final int id, final int value, final int time) {
		if(time != getTime()){
			stepTime(time);
		}
		signals.get(id).set(value);
	}


	static void stepTime(final int time) {
		assert(time == getTime()+1);
		Global.get(0).set(time);
	}

	public static int getTime() {
		return simTimeSignal.get();
	}

	public static void create_time() {
		register_time(new Time(0));
	}

	protected static void startTC(String tcName) {
		List<Class<?>> classes = ClassFinder.find("hu.beton.hilihase.testcases");
        try{
			System.out.println("Finding testclass: " + tcName);
        } catch(NullPointerException ex){
			System.out.println("The name of the testcase is null!!! Please specify a valid testcase name");
            throw ex;
        }
		for(Class<?> cl : classes){
			System.out.println("Classname: " + cl.getName());
			if(cl.getName().endsWith(tcName)){
				System.out.println("Class math: " + cl.getName() + " instatniating...");
				try {
					Object testInst = cl.newInstance();
					TCThread tct = (TCThread) testInst;
					Global.registerTCThread(tct);
					Thread th = new Thread(tct);
					th.start();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
		throw new IllegalArgumentException("No testcase found with the following name: " + tcName);

	}

	public static void joinAllTCs() {
		for(TCThreadStateC tc : tcThreadsState){
			try {
				tc.tct.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void addDriveList(int iD, int val) {
		signalDrvQueue.add(new SignalDrv(iD, val));
	}
	
	protected static void processDriveList() {
		while(true){
			try{
				SignalDrv sdrv = signalDrvQueue.remove(0);
				if(loadLibraries){
					NativeInterface.hilihase_drve(sdrv.ID, (byte) sdrv.val);
					}
				else{
					// test
					NativeInterface.hilihase_drve_debug(sdrv.ID, (byte) sdrv.val);
				}
			} catch (IndexOutOfBoundsException ex){
				return;
			}
		}
	}


}
