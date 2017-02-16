package hu.beton.hilihase.testcases;

import static org.junit.Assert.assertEquals;
import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.Signal;
import hu.beton.hilihase.jfw.SignalEvent;
import hu.beton.hilihase.jfw.TCThread;
import hu.beton.hilihase.jfw.ValueE;

public class FullDutTest extends TCThread {

	@Override
	public void test() {
		System.out.println("TEST RUNNING!!!");
		Signal clk, en;
		clk = Global.get("clk");
		en = Global.get("en");
		System.out.println("POS");
		clk.WaitOn(SignalEvent.POSEDGE, this);
		assertEquals("simtime", 2, Global.getTime());
		assertEquals("set-get", ValueE.high, clk.get().toInteger());
		
		en.drive(ValueE.HIGH);
		
		System.out.println("TC: FIN at " + Global.getTime());		

	}

}
