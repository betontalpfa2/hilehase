package hu.beton.hilihase.jfw;

import java.util.ArrayList;
import java.util.List;

public class QuestaBehav implements Runnable {
	List<String> signalNames = new ArrayList<String>();
	TCThread tct;
	
	public QuestaBehav(TCThread tct) {
		signalNames.add("clock_sig");
		signalNames.add("sig1");
		signalNames.add("sig2");
		this.tct = tct;
	}
	
	
	@Override
	public void run() {
		Global.init();
		Global.create_time();
		int id = 1;
		for(String signalName : signalNames){
			Global.register_signal(id, signalName, ValueE.LOW);
		}
		
		Global.registerTCThread(tct);
		Thread th = new Thread(tct);
		th.start();
		
		Signal clk = Global.get("clock_sig");
		Time time = (Time) Global.get(0);
		
		System.out.println("HELLO");

		System.out.println(Global.getTime() + " QB: setting 1 ...");
		time.set(1);
		System.out.println(Global.getTime() + "QB: set(1);");
		time.set(2);
		System.out.println(Global.getTime() + "QB: set(2);");
		time.set(3);
		System.out.println(Global.getTime() + "QB: set(3);");
		time.set(4);
		System.out.println(Global.getTime() + "QB: set(4);");
		time.set(5);
		System.out.println(Global.getTime() + "QB: set(5);");
		clk.set(ValueE.HIGH);

		System.out.println(Global.getTime() + "QB: ValueE.HIGH");
		time.set(6);
		System.out.println(Global.getTime() + "QB: set(6);");
		time.set(7);
		System.out.println(Global.getTime() + "QB: set(7);");
		time.set(8);
		System.out.println(Global.getTime() + "QB: set(8);");
		time.set(9);
		System.out.println(Global.getTime() + "QB: set(9);");
		time.set(10);
		clk.set(ValueE.LOW);
		System.out.println("END");

		try {
			th.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
