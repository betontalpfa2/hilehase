package hu.beton.hilihase.dut;

import static org.junit.Assert.assertEquals;
import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.NativeInterface;
import hu.beton.hilihase.jfw.ValueE;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Clock implements Runnable{
	List<String> signalNames = new ArrayList<String>();
	int localTime = 0;
	private List<ValueE> signals;
	
	
	
	public Clock() {
		signals = new ArrayList<ValueE>();
		signalNames.add("__simtime__");
		signalNames.add("clk");
		signalNames.add("en");
		signalNames.add("reg");
	}
	
	
//	@Override
	@Test
	public void run() {
		try{
		NativeInterface.hilihase_init_debug(1, signals);
		
//		Global.init(false);
		Global.create_time();
//		int id = 1;
		signals.add(0, null);
		signals.add(1, ValueE.LOW);
		signals.add(2, ValueE.LOW);
		signals.add(3, ValueE.LOW);
		
		for(int i = 1; i < signals.size(); i++ ){
			NativeInterface.hilihase_register(i, signalNames.get(i), (byte) signals.get(i).toInteger());
		}
		
		NativeInterface.hilihase_start_tc("ClockTest");

		System.out.println("ClockDUT: stepping 1 ...");
		NativeInterface.hilihase_step(++localTime);
		System.out.println("ClockDUT: stepping 2 ...");
		NativeInterface.hilihase_step(++localTime);
		System.out.println("ClockDUT: stepping 3 ...");
		NativeInterface.hilihase_step(++localTime);

		System.out.println("ClockDUT: checking ...");
		assertEquals("helo", ValueE.high, signals.get(1).toInteger());	// clk
		
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		
		assertEquals("helo", ValueE.low, signals.get(1).toInteger());	// clk
		
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		

		assertEquals("helo", ValueE.high, signals.get(1).toInteger());	// clk

		
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		
		assertEquals("helo", ValueE.low, signals.get(1).toInteger());	// clk
		
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		

		assertEquals("helo", ValueE.high, signals.get(1).toInteger());	// clk
		System.out.println("END");


		}finally{
			NativeInterface.hilihase_close(-1);
		}
	}

}
