package hu.beton.hilihase.rmi;

import hu.beton.hilihase.jfw.IJunitHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ListIterator;


public class Server implements IJUnitRMI {
	Process process = null;
	Registry registry = null;
//	private List<AssertionError> errors;
	IJunitHandler handler;

	public Server(IJunitHandler handler) {
		this.handler = handler;
	}


	public void handle(AssertionError err) {
//		if(null == handler || this == handler){
//			errors.add(err);
//		}
//		else{
			handler.handle(err);
//		}
	}

	public void startServer(){
		try{
//			String javaHome = System.getenv("JAVA_HOME");
//			String rmic = javaHome + "\\bin\\rmic";
			ProcessBuilder builder = new ProcessBuilder(
					"rmiregistry", "2001", "-J-Djava.rmi.server.codebase=file:./bin/");
			builder.redirectErrorStream(true);
			process = builder.start();
			System.out.println("Started...");
			BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			/*while (true) {
//					r.
					line = r.readLine();
					if (line == null) { break; }
						System.out.println(line);
						return;
				}*/
		}
		catch(Exception e){
			e.printStackTrace();
			return;
			//			System.out.println(e);
		}

		try {
			System.out.println("Server......");
			//				obj = new MyServer();
			IJUnitRMI stub = (IJUnitRMI) UnicastRemoteObject.exportObject(this, 0);

			// Bind the remote object's stub in the registry
			registry = LocateRegistry.getRegistry("127.0.0.1", 2001);
			registry.rebind("hilehase", stub);

			System.err.println("Server ready");


			Thread.sleep(10000);
			System.err.println("Destroying...");


		} catch (Exception e) {
			//				System.err.println("Server exception: " + e.toString());
			e.printStackTrace();

			close();
		}
		finally{
			
		}

	}

	void close(){
		System.err.println("Destroying...");
		process.destroy();

		try {
			UnicastRemoteObject.unexportObject(this, false);
			//				registry.unbind("Hello");

			return;
			//				Thread.sleep(1000);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
//	private void analize() {
//		ListIterator<AssertionError> iter = errors.listIterator(errors.size());
//
//		while (iter.hasPrevious()) {
//			AssertionError err = iter.previous();
//			err.printStackTrace();
//			if(iter.previousIndex() == -1){
//				throw err;
//			}
//		}
//	}


}
