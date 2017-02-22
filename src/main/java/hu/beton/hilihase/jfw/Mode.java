package hu.beton.hilihase.jfw;

/**
 * Running Mode 
 * Used by initialization of GLobal.
 */
public class Mode {

	private int val;
			
	Mode(int v){
		val = v;
	}
	
	@Override
	public boolean equals(Object obj) {
		Mode md = (Mode) obj;
		return md.val == this.val;
	};
	/**
	 * Unit test starts the test-cases which initialise the Global and starts
	 * the HDL simulator
	 */
	public static final Mode JUnitTest = new Mode(1);
	
	/**
	 * The HDL simulator starts (by command line) and creates the Java virtual
	 * machine and starts the testcase. 
	 */
	public static final Mode HDLSimStarts = new Mode(2);
}