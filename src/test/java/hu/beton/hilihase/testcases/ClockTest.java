package hu.beton.hilihase.testcases;

import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.Signal;
import hu.beton.hilihase.jfw.TCThread;
import hu.beton.hilihase.jfw.ValueE;

public class ClockTest extends TCThread {

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
			
			waitSim(5);
			
			clk.drive(ValueE.HIGH);
			
			System.out.println("TC: FIN at " + Global.getTime());		

		}

}

