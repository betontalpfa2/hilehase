package hu.beton.hilihase.rmi;

import hu.beton.hilihase.jfw.IJunitHandler;

import java.rmi.Naming;


public class Client implements IJunitHandler {

	public void handle(AssertionError err) {
		try{

			IJUnitRMI stub=(IJUnitRMI)Naming.lookup("rmi://localhost:2001/hilehase");
			stub.handle(err);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
