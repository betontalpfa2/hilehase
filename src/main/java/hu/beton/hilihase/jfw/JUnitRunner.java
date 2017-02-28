package hu.beton.hilihase.jfw;

// import org.junit.runner.JUnitCore;
// import org.junit.runner.Result;
// import org.junit.runner.notification.Failure;

public class JUnitRunner extends Thread {
	Class<?> tctc;
	
	@Override
	public void run() {
		/*Result result = JUnitCore.runClasses(tctc);
		    for (Failure failure : result.getFailures()) {
		      System.out.println(failure.toString());
		    }*/
	}
	
	void startTest(Class<?> tctc){
		this.tctc = tctc;
		start();
	}
}
