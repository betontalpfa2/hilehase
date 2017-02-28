package hu.beton.hilihase.testcases;

import org.junit.Test;
import static org.junit.Assert.*;

import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.Signal;
import hu.beton.hilihase.jfw.TCThread;
import hu.beton.hilihase.jfw.ValueE;

public class ClockTest extends TCThread {

	public ClockTest() {
		super();
	}
	
	@Override
	public void test() {
		System.out.println("TEST RUNNING!!!");
		Signal clk;
		clk = Global.get("clk");

		System.out.println("ClockTEST: wait 5 ...");
		//			Global.waitSim(5, this);

		clk.drive(ValueE.HIGH);

		waitSim(5);

		clk.drive(ValueE.LOW);

		waitSim(5);

		clk.drive(ValueE.HIGH);

		waitSim(5);

		clk.drive(ValueE.LOW);

		waitSim(1);
		assertEquals("TestClock", ValueE.LOW, clk.get());

		waitSim(4);

		clk.drive(ValueE.HIGH);

		System.out.println("TC: FIN at " + Global.getTime());		

	}

	@Test
	public void testMe(){
		startHDLSim("Clock", false);
	}
	
//	@Test
//	public void testMe2(){
//		startJUnitTest(toplevelName);
//	}


}

