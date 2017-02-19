package hu.beton.hilihase.jfw;

import static org.junit.Assert.*;
import hu.beton.hilihase.dut.Clock;
import hu.beton.hilihase.dut.FullDut;

import org.junit.Test;

public class NativeInterfaceTest {


	@Test
	public void test_a_minmal() throws InterruptedException {
		FullDut dut;

		dut = new FullDut();
		Thread sim = new Thread(dut);
		sim.start();

		try {
			sim.join();
		} catch (InterruptedException e) {
			assertTrue("Could not be happened", false);
			e.printStackTrace();
		}

	}
	
	@Test
	public void test_clock() throws InterruptedException {
		Clock dut;

		dut = new Clock();
		Thread sim = new Thread(dut);
		sim.start();

		try {
			sim.join();
		} catch (InterruptedException e) {
			assertTrue("Could not be happened", false);
			e.printStackTrace();
		}

	}
}
