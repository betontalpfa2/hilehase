package hu.beton.hilihase.jfw;

import static org.junit.Assert.*;
import hu.beton.hilihase.dut.FullDut;

import org.junit.*;

public class SignalTest {


	@Test
	public void test_set_get() {
		try{
			Global.init(false);

			Signal sig1 = new Signal(0, "clock_signal", ValueE.HIGH);

			// assert statements
			ValueE val = ValueE.HIGH;
			sig1.set(val);

			assertEquals("set-get", val, sig1.get());
		} finally{
			Global.cleanup();
		}
	}

	@Test
	public void test_singleTC() {
		try{
			Global.init(false);
			Signal sig1 = new Signal(0, "clock_signal", ValueE.HIGH);
			ValueE val = ValueE.HIGH;

			TCThread tct = new TCThread() {

				@Override
				public void test() {
					sig1._set_(val);
				}
				
			};
			Global.registerTCThread(tct);

			Thread th = new Thread(tct);

			th.start();
			try {
				th.join();
			} catch (InterruptedException e) {
				assertTrue("Could not be happened", false);
				e.printStackTrace();
			}
			// assert statements
			//		sig1.set(5);

			assertEquals("set-get", val, sig1.get());
		} finally{
			Global.cleanup();
		}
	}

	@Test
	public void test_register() {
		try{
			Global.init(false);
			SimVariable<?, ?> tim = new Time(1);
			Global.register_time((Time)tim);

			String name = "clock_signal";
			Global.register_signal(1, name, ValueE.HIGH);
			ValueE val = ValueE.HIGH;

			TCThread tct = new TCThread() {

				@Override
				public void test() {
					Signal sig1;
					sig1 = Global.get(name);
					sig1._set_(val);
					assertEquals("set-get", val, sig1.get());
				}
			};
			Global.registerTCThread(tct);

			Thread th = new Thread(tct);

			th.start();
			try {
				th.join();
			} catch (InterruptedException e) {
				assertTrue("Could not be happened", false);
				e.printStackTrace();
			}
		} finally{
			Global.cleanup();
		}         
	}


	@Test
	public void test_behav() throws InterruptedException {
		try{
			QuestaBehav qb;
//			ValueE val = ValueE.HIGH;

//			TCThread tct = new TCThread() {

//				@Override
//				public void test() {	
//				}
//			};

			qb = new QuestaBehav(null);
			Thread sim = new Thread(qb);
			sim.start();

			try {
				sim.join();
			} catch (InterruptedException e) {
				assertTrue("Could not be happened", false);
				e.printStackTrace();
			}
		} finally{
			Global.cleanup();
		}         
	}
	
	
	@Test
	public void test_sample2() throws InterruptedException {
		try{
			FullDut dut;
//			ValueE val = ValueE.HIGH;

//			TCThread tct = new TCThread() {

//				@Override
//				public void test() {	
//				}
//			};

			dut = new FullDut();
			Thread sim = new Thread(dut);
			sim.start();

			try {
				sim.join();
			} catch (InterruptedException e) {
				assertTrue("Could not be happened", false);
				e.printStackTrace();
			}
		} finally{
			Global.cleanup();
		}         
	}

}
