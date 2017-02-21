package hu.beton.hilihase.testcases;

import static org.junit.Assert.assertEquals;
import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.Signal;
import hu.beton.hilihase.jfw.SignalEvent;
import hu.beton.hilihase.jfw.TCThread;
import hu.beton.hilihase.jfw.ValueE;

/**
 * This is the first testcase which uses the NativeInterface methods. Note that
 * it does not use the dynamic library, just call the Java method (and test
 * methods) directly.
 * 
 * Simpler/Higher-abstraction test: IndiTest
 * Next level test:					Minimal 
 * 
 * @author ebenera
 *
 */
public class FullDutTest extends TCThread {

	@Override
	public void test() {
		System.out.println("TEST RUNNING!!!");
		Signal clk, en;
		clk = Global.get("clk");
		en = Global.get("en");
		System.out.println("POS");
		clk.WaitOn(SignalEvent.POSEDGE);
		assertEquals("simtime", 2, Global.getTime());
		assertEquals("set-get", ValueE.high, clk.get().toInteger());
		
		en.drive(ValueE.HIGH);
		
		System.out.println("TC: FIN at " + Global.getTime());		

	}

}
