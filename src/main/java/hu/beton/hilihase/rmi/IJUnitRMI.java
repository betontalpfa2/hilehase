package hu.beton.hilihase.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IJUnitRMI  extends Remote{
	

	void handleRemote(AssertionError err) throws RemoteException;
}
