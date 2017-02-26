import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

public interface IRobot extends Remote {

	public String getRobotName() throws RemoteException;

	public String getRobotAddress() throws RemoteException;

	public void addRobotMDNorth(String robotAddress, Integer md) throws RemoteException;

	public void addRobotMDEast(String robotAddress, Integer md) throws RemoteException;

	public void addRobotMDWest(String robotAddress, Integer md) throws RemoteException;

	public void addRobotMDSouth(String robotAddress, Integer md) throws RemoteException;

	public void updateGridLocation(String robotAddress, Grid grid) throws RemoteException;

	public Integer getMDNorth() throws RemoteException;

	public Integer getMDEast() throws RemoteException;

	public Integer getMDWest() throws RemoteException;

	public Integer getMDSouth() throws RemoteException;

	public void setSelectedRobotNorth(String selectedRobotNorth) throws RemoteException;

	public void setSelectedRobotEast(String selectedRobotEast) throws RemoteException;

	public void setSelectedRobotWest(String selectedRobotWest) throws RemoteException;

	public void setSelectedRobotSouth(String selectedRobotSouth) throws RemoteException;

	public void encloseTarget(Collection<String> selectedRobots, String selectedRobot) throws RemoteException;

	public Map<String, Integer> getAllRobotsMDsNorth() throws RemoteException;

	public Map<String, Integer> getAllRobotsMDsEast() throws RemoteException;

	public Map<String, Integer> getAllRobotsMDsWest() throws RemoteException;

	public Map<String, Integer> getAllRobotsMDsSouth() throws RemoteException;

	public void setAllRobotsMDsNorth(Map<String, Integer> allRobotsMDsNorth) throws RemoteException;

	public void setAllRobotsMDsEast(Map<String, Integer> allRobotsMDsEast) throws RemoteException;

	public void setAllRobotsMDsWest(Map<String, Integer> allRobotsMDsWest) throws RemoteException;

	public void setAllRobotsMDsSouth(Map<String, Integer> allRobotsMDSouth) throws RemoteException;

	public void printHello() throws RemoteException;

	public void printGridLocation(String ip, Grid grid) throws RemoteException;

	public void setTargetGridLocation(Grid targetGrid) throws RemoteException;

	public Grid getTargetGridLocation(Grid targetGrid) throws RemoteException;

	public void setTargetFound(boolean targetFound) throws RemoteException;

	public void updateNewRobotGrids(Map<Grid, Boolean> newRobotGrids) throws RemoteException;

	public void findTarget(Collection<String> robots, String bootstrapper, int robotGridX, int robotGridY, String maliciousRobot)
			throws RemoteException;

	public int getRobotGridX() throws RemoteException;

	public int getRobotGridY() throws RemoteException;

	public void proclaimRobotsToFindTarget(Map<String, String> targetFoundByRobots) throws RemoteException;

}
