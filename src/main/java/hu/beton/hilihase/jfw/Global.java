package hu.beton.hilihase.jfw;

import hu.beton.hilihase.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Global {

	enum ThreadState {
		Running,
		Wait
	}
	
	
	private static Mode mode;
	private static Map<Long, TCThreadInfo> tcThreadsState; // = new ArrayList<String>();
	//	private int[] tcThreads;
	private boolean waitingSigTh = false;
	private static Global me = null;
	private static Time simTimeSignal = null;
	private static List<SimVariable<?, ?>> signals;
	private static List<SignalDrv> signalDrvQueue;
	int runningCount;
	String testcasePath = null;
	static boolean  loadLibraries = true;
//	private static Boolean setDone = Boolean.FALSE;

	class TCThreadInfo{
//		TCThread tct;
		ThreadState state;
		int waitOn;// Negative means a stopped thread or error
		//		static int runningCount;
		final TCThread tct;

		TCThreadInfo(ThreadState state, int waitOn, TCThread tct){
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



	Global(Mode mode, boolean loadlibraries) {
		Global.mode = mode;
		Global.loadLibraries = loadlibraries;
		if(loadlibraries){
			System.out.println(System.getProperty("java.library.path"));
			System.loadLibrary("jfw-1.0-SNAPSHOT");
		}
		else{
			System.err.println("WARNING: Libraries wasn't loaded. Use this mod is only for test/debug.");
		}
		tcThreadsState  = new Hashtable<Long, TCThreadInfo>();
		signals  = new ArrayList<SimVariable<?, ?>>();
		register_time(new Time(0));
		me = this;
		runningCount = 0;
		signalDrvQueue = Collections.synchronizedList(new ArrayList<SignalDrv>());
	}

	static void init(Mode mode){
		init(mode, true);
	}
	
	static void init(Mode mode, boolean loadlibraries) {
		if (null == me){
//			Global.loadLibraries = loadlibraries;
			new Global(mode, loadlibraries);
		} else{
			System.err.println("Multiple init is disabled...");
		}
	}

	static void cleanup(){
		me = null;
		simTimeSignal = null;
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
		for(TCThreadInfo tcth : tcThreadsState.values()){
			tcth.wakeIF(on);
		}
	}

	public static void tcThreadToSleep(long tcThreadId, int on) {
		me._tcThreadToSleep_(tcThreadId, on);
	}

	private synchronized void _tcThreadToSleep_(long tcThreadId, int on) {
		tcThreadsState.get(tcThreadId).set(ThreadState.Wait, on);
		// The simulator threads aka. Signals are waiting on this class (aka.
		// Global). If there is no running TC, let wake up them. to continue the
		// simulation (maybe with the next time-step)
		if(getCoutOfRunningTcs() == 0){
			notifyAll();
		}
	}

	static void registerTCThread(TCThread thread, TCThread parent) {
		me._registerTCThread_(thread, parent);
	}


	static void registerTCThread(TCThread thread) {
		me._registerTCThread_(thread, null);
	}

	synchronized void _registerTCThread_(TCThread thread, TCThread parent) {
//		thread.setID(tcThreadsState.size());
		if(null == parent){
			Util.assertUtil(0 == tcThreadsState.size());
		}
		tcThreadsState.put(thread.getId(), new TCThreadInfo(ThreadState.Running, 0, thread));
		for (SimVariable<?, ?> signal : signals){
			signal.registerTCThread(thread.getID());
		}
		
		thread.setParent(parent);
	}

	public static void waitSim(int time) {
		waitSimUntil(time + getTime());
	}
	
	public static void waitSimUntil(int time) {
		simTimeSignal.waitSim(time);
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
		Signal sig = new Signal(id, name, val);
		for(Long k: tcThreadsState.keySet()){
			sig.registerTCThread(k);
		}
		signals.add(id, sig);
		
	}

	static void register_time(Time time){
		if(null != simTimeSignal){
			throw new IllegalArgumentException("Only one time variable supported!");
		}
		if( time.ID != 0){
			throw new IllegalArgumentException("The id of the time must be '0'! The current id is:" + time.ID);
		}
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
		process_post_time();
//		assert(time == getTime()+1);
		Global.get(0).set(time);

		for (SimVariable<?, ?> signal : signals){
			signal.step();
		}
	}

	private static void process_post_time() {
		// TODO Auto-generated method stub
		
	}

	public static int getTime() {
		return simTimeSignal._get_();
	}

	public static void create_time() {
		register_time(new Time(0));
	}

	protected static void startTC(String tcName) {
		if(mode.equals(Mode.JUnitTest)){
			TCThread tct = getMainTct();
			System.err.println("Testcase has overriden (Junittest mode) " + tct.getName() + " will startwd instead of " + tcName);
			tct.start();
//			setupDone();
			return;
		}
		TCThread tct = findTc(tcName);
		Global.registerTCThread(tct);
		tct.start();
//		setupDone();
		return;		
	}

	private static TCThread getMainTct() {
//		tcThreadsState.entrySet().iterator().
//		for(TCThreadInfo tctinfo : tcThreadsState.values()){
//			tctinfo.tct.getParent();
//		}
		return tcThreadsState.entrySet().iterator().next().getValue().tct;
	}

	private static TCThread findTc(String tcName) {
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
					return (TCThread) testInst;
//					Global.registerTCThread(tct);
//					tct.start();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		throw new IllegalArgumentException("No testcase found with the following name: " + tcName);
	}

	public static void joinAllTCs() {
		for(TCThreadInfo tc : tcThreadsState.values()){
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

	public static void startHDLSim(String toplevelName) {
		if(loadLibraries){
			NativeInterface.startHDLSim(toplevelName);
		}
		else{
			// test
			NativeInterface.startHDLSim_debug(toplevelName);
		}
	}

	/*public static void waitsetup() {
		synchronized (setDone) {
			while(!setDone){
				try {
					setDone.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}*/

	/*public static void setupDone() {
		synchronized (setDone) {
			setDone.notifyAll();
			setDone= true;
		}
	}*/

}
