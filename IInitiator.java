import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IInitiator extends Remote {
	public void logRobot(String robotName, String robotAddress, Grid robotGrid) throws RemoteException;

	public Map<String, String> getRobots() throws RemoteException;

	public String getInitiatorName() throws RemoteException;

	public String getInitiatorAddress() throws RemoteException;

	String getBootstrappingRobot() throws RemoteException;

	void setBootstrappingRobot(String robot) throws RemoteException;

	public Grid getTargetGrid() throws RemoteException;

	public String getMaliciousRobot() throws RemoteException;

}
