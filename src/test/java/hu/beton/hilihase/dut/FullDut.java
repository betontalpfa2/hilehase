package hu.beton.hilihase.dut;

import static org.junit.Assert.assertEquals;
import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.NativeInterface;
import hu.beton.hilihase.jfw.ValueE;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FullDut implements Runnable{
	List<String> signalNames = new ArrayList<String>();
	int localTime = 0;
	private List<ValueE> signals;
	
	public FullDut() {
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
		
//		NativeInterface.hilihase_register(2, "en", (byte) 0);
//		NativeInterface.hilihase_register(3, "reg", (byte) 0);
	
		
		NativeInterface.hilihase_start_tc("FullDutTest");

		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_step(++localTime);
		NativeInterface.hilihase_read(1, (byte) 1, localTime);
		
		assertEquals("helo", ValueE.high, signals.get(2).toInteger());

		NativeInterface.hilihase_step(3);
		NativeInterface.hilihase_step(4);
		NativeInterface.hilihase_read(1, (byte) 0, localTime);
		
		NativeInterface.hilihase_step(5);
		NativeInterface.hilihase_step(6);
		NativeInterface.hilihase_read(1, (byte) 1, localTime);
		
		System.out.println("END");

		Global.joinAllTCs();
		}finally{
			NativeInterface.hilihase_close(-1);
		}
	}

}
