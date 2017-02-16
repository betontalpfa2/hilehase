package hu.beton.hilihase.dut;

import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.Sample2;

public class FullDut implements Runnable{
//	List<String> signalNames = new ArrayList<String>();
//	TCThread tct;
	
	public FullDut() {
//		signalNames.add("clock_sig");
//		signalNames.add("sig1");
//		signalNames.add("sig2");
//		this.tct = tct;
	}
	
	
	@Override
	public void run() {
		Sample2.hilihase_init(1);
		
//		Global.init(false);
		Global.create_time();
//		int id = 1;
		Sample2.hilihase_register(1, "clk", (byte) 0);
		Sample2.hilihase_register(2, "en", (byte) 0);
		Sample2.hilihase_register(3, "reg", (byte) 0);
	
		
		Sample2.hilihase_start_tc("FullDutTest");

		Sample2.hilihase_step(1);
		Sample2.hilihase_step(2);
		Sample2.hilihase_read(1, (byte) 1);

		Sample2.hilihase_step(3);
		Sample2.hilihase_step(4);
		Sample2.hilihase_read(1, (byte) 0);
		
		Sample2.hilihase_step(5);
		Sample2.hilihase_step(6);
		Sample2.hilihase_read(1, (byte) 1);
		
		System.out.println("END");

		Global.joinAllTCs();

	}

}
