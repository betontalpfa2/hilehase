package hu.beton.hilihase.testcases;

import org.junit.Test;

import static org.junit.Assert.*;
import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.Signal;
import hu.beton.hilihase.jfw.TCThread;
import hu.beton.hilihase.jfw.ValueE;

public class FullStackJunitTest extends TCThread {

	@Override
	public void test() {
		System.out.println("Full stact Junit TEST RUNNING!!!");
		Signal x, y, cin, cout, sum;
		
		x = Global.get("top_x");		
		y = Global.get("top_y");		
		cin = Global.get("top_cin");
		sum = Global.get("out");
		cout = Global.get("carryout");
		

		cin.drive(ValueE.LOW);
		x.drive(ValueE.HIGH);
		waitSim(1);
		assertEquals("Sum 1", ValueE.HIGH, sum.get());
		assertEquals("Sum 1", ValueE.LOW, cout.get());
		
		y.drive(ValueE.HIGH);
		waitSim(1);
		assertEquals("Sum 1", ValueE.LOW, sum.get());
		assertEquals("Sum 1", ValueE.HIGH, cout.get());

		System.out.println("ClockTEST: wait 5 ...");

		
	}
	
	@Test
	public void testMe(){
		Me("full_adder_tb");
	}
}
