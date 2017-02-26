import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

public class Initiator extends UnicastRemoteObject implements IInitiator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MIN = 0;
	private static final int MAX = 8;
	private String initiatorName = null;
	private String initiatorAddress = null;
	private boolean bootstrapper = false;
	private String bootstrappingRobot = null;
	private Map<String, String> robots = new HashMap<String, String>();
	private Grid targetGrid = null;
	private String maliciousRobot = null;

	protected Initiator() throws RemoteException {
		super();
		robots = new HashMap<String, String>();
		this.targetGrid = new Grid();
	}

	/**
	 * http://stackoverflow.com/questions/363681/generating-random-integers-in-a
	 * -range-with-java
	 */
	public Grid initializeTargetGrid() {
		Random random = new Random();
		int x = random.nextInt((MAX - MIN) + 1) + MIN;
		int y = random.nextInt((MAX - MIN) + 1) + MIN;
		return new Grid(x, y);
	}

	@Override
	public void logRobot(String robotName, String robotAddress, Grid robotGrid) throws RemoteException {
		System.out.println("ROBOT NAME: " + robotName + ", " + "ROBOT ADDRESS: " + robotAddress + ", " + "ROBOT GRID: "
				+ "(" + robotGrid.getX() + "," + robotGrid.getY() + ")");
		robots.put(robotName, robotAddress);
	}

	@Override
	public Map<String, String> getRobots() throws RemoteException {
		return this.robots;
	}

	@Override
	public String getInitiatorName() throws RemoteException {
		return this.initiatorName;
	}

	@Override
	public String getInitiatorAddress() throws RemoteException {
		return this.initiatorAddress;
	}

	public void setInitiatorName(String serverName) {
		this.initiatorName = serverName;
	}

	public void setInitiatorAddress(String serverAddress) {
		this.initiatorAddress = serverAddress;
	}

	@Override
	public String getBootstrappingRobot() throws RemoteException {
		return this.bootstrappingRobot;
	}

	@Override
	public void setBootstrappingRobot(String robot) throws RemoteException {
		if (bootstrapper == false) {
			this.bootstrappingRobot = robot;
			this.bootstrapper = true;
		}
	}

	public void printInitiatorInfo() throws RemoteException {
		System.out.println("INITIATOR NAME: " + this.getInitiatorName());
		System.out.println("INITIATOR ADDRESS: " + this.getInitiatorAddress());
		System.out.println(
				"UNKNOWN TARGET: " + "(" + this.getTargetGrid().getX() + ", " + this.getTargetGrid().getY() + ")");
	}

	private void setTargetGrid(Grid targetGrid) {
		this.targetGrid = targetGrid;
	}

	@Override
	public Grid getTargetGrid() throws RemoteException {
		return this.targetGrid;
	}

	@Override
	public String getMaliciousRobot() throws RemoteException {
		this.maliciousRobot = this.getRobots().get(3);
		return this.maliciousRobot;
	}

	public static void main(String[] args) {
		try {
			Initiator initiator = new Initiator();
			String initiatorName = InetAddress.getLocalHost().getHostName();
			String initiatorAddress = InetAddress.getLocalHost().getHostAddress();

			initiator.setInitiatorName(initiatorName);
			initiator.setInitiatorAddress(initiatorAddress);

			Grid targetGrid = initiator.initializeTargetGrid();
			initiator.setTargetGrid(targetGrid);

			initiator.printInitiatorInfo();

			IInitiator initiatorRef = initiator;
			Naming.rebind("initiator", initiatorRef);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
