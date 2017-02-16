package hu.beton.hilihase.jfw;

import static org.junit.Assert.*;
import hu.beton.hilihase.dut.FullDut;

import org.junit.Test;

public class NativeInterfaceTest {


	@Test
	public void test_sample2() throws InterruptedException {
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
}
