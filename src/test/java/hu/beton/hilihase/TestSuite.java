package hu.beton.hilihase;

import hu.beton.hilihase.jfw.NativeInterfaceTest;
import hu.beton.hilihase.jfw.SignalTest;
import hu.beton.hilihase.testcases.ClockTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	SignalTest.class,
	ClockTest.class,
	NativeInterfaceTest.class
})

public class TestSuite {   
}  	