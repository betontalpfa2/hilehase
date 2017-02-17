package hu.beton.hilihase.testcases;

import static org.junit.Assert.assertEquals;
import hu.beton.hilihase.jfw.Global;
import hu.beton.hilihase.jfw.Signal;
import hu.beton.hilihase.jfw.SignalEvent;
import hu.beton.hilihase.jfw.TCThread;
import hu.beton.hilihase.jfw.ValueE;

/**
 * This is the first testcase which uses the full stack of Hilehase. (including
 * questa.)
 * 
 * Simpler/Higher-abstraction test: FullDutTest
 * 
 * @author ebenera
 *
 */
public class Minimal extends TCThread {

	@Override
	public void test() {
		System.out.println("Minimal TEST RUNNING!!!");
		Signal x;
		x = Global.get("top_x");
//		y = Global.get("top_y");
		System.out.println("POS");
		x.WaitOn(SignalEvent.POSEDGE, this);
        if(Global.getTime() != 2){
            System.err.println("ERROROROROR: simtime at " + Global.getTime());
        }
        if(ValueE.high != x.get().toInteger()){
            System.err.println("ERROROROROR: set-get at " + Global.getTime());
        }
		// assert("simtime", 2, );
		
		
		System.out.println("TC: FIN at " + Global.getTime());		

	}

}
