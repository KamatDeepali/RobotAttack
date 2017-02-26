import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.List;

public class Robot extends UnicastRemoteObject implements IRobot, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int ROW = 8;
	private final int MIN = 0;
	private final int MAX = 8;

	private String robotName = null;
	private String robotIPAddress = null;
	private Grid robotGrid = null;
	private Map<Grid, Boolean> newRobotGrids = null;

	private Map<String, Grid> gridLocations = null;

	private Grid targetGrid = null;
	private Grid targetGridNorth = null;
	private Grid targetGridEast = null;
	private Grid targetGridWest = null;
	private Grid targetGridSouth = null;
	private boolean targetFound = false;
	private boolean visitedBSTargetFind = false;
	private Map<String, String> targetFoundByRobots;

	private int robotGridX = 0;
	private int robotGridY = 0;
	private int targetGridX = 0;
	private int targetGridY = 0;

	private Map<String, Integer> allRobotsMDsNorth = null;
	private Map<String, Integer> allRobotsMDsEast = null;
	private Map<String, Integer> allRobotsMDsWest = null;
	private Map<String, Integer> allRobotsMDsSouth = null;

	private Integer mdNorth = null;
	private Integer mdEast = null;
	private Integer mdWest = null;
	private Integer mdSouth = null;

	private String selectedRobotNorth = null;
	private String selectedRobotEast = null;
	private String selectedRobotWest = null;
	private String selectedRobotSouth = null;

	protected Robot() throws RemoteException {
		super();
		this.targetGrid = new Grid(3, 3);
		this.gridLocations = new HashMap<String, Grid>();
		this.allRobotsMDsNorth = new LinkedHashMap<String, Integer>();
		this.allRobotsMDsEast = new LinkedHashMap<String, Integer>();
		this.allRobotsMDsWest = new LinkedHashMap<String, Integer>();
		this.allRobotsMDsSouth = new LinkedHashMap<String, Integer>();
		this.newRobotGrids = new HashMap<Grid, Boolean>();
		this.newRobotGrids.put(new Grid(0, 1), false);
		this.newRobotGrids.put(new Grid(0, 4), false);
		this.newRobotGrids.put(new Grid(0, 7), false);
		this.newRobotGrids.put(new Grid(8, 1), false);
		this.newRobotGrids.put(new Grid(8, 4), false);
		this.newRobotGrids.put(new Grid(8, 7), false);
		this.targetFoundByRobots = new HashMap<String, String>();
	}

	protected Robot(String robotName, String robotIPAddress, Grid robotGrid) throws RemoteException {
		this.robotName = robotName;
		this.robotIPAddress = robotIPAddress;
		this.robotGrid = robotGrid;
	}

	@Override
	public void setSelectedRobotNorth(String selectedRobotNorth) throws RemoteException {
		this.selectedRobotNorth = selectedRobotNorth;
	}

	@Override
	public void setSelectedRobotEast(String selectedRobotEast) throws RemoteException {
		this.selectedRobotEast = selectedRobotEast;
	}

	@Override
	public void setSelectedRobotWest(String selectedRobotWest) throws RemoteException {
		this.selectedRobotWest = selectedRobotWest;
	}

	@Override
	public void setSelectedRobotSouth(String selectedRobotSouth) throws RemoteException {
		this.selectedRobotSouth = selectedRobotSouth;
	}

	public String getSelectedRobotNorth() {
		return this.selectedRobotNorth;
	}

	public String getSelectedRobotEast() {
		return this.selectedRobotEast;
	}

	public String getSelectedRobotWest() {
		return this.selectedRobotWest;
	}

	public String getSelectedRobotSouth() {
		return this.selectedRobotSouth;
	}

	@Override
	public void updateGridLocation(String robotAddress, Grid grid) throws RemoteException {
		this.gridLocations.remove(robotAddress);
		this.gridLocations.put(robotAddress, grid);
	}

	public Grid getGridLocation(String robotAddress) {
		return this.getRobotGrid();
	}

	public void setMDNorth(Integer mdNorth) {
		this.mdNorth = mdNorth;
	}

	public void setMDEast(Integer mdEast) {
		this.mdEast = mdEast;
	}

	public void setMDWest(Integer mdWest) {
		this.mdWest = mdWest;
	}

	public void setMDSouth(Integer mdSouth) {
		this.mdSouth = mdSouth;
	}

	@Override
	public Integer getMDNorth() throws RemoteException {
		return this.mdNorth;
	}

	@Override
	public Integer getMDEast() throws RemoteException {
		return this.mdEast;
	}

	@Override
	public Integer getMDWest() throws RemoteException {
		return this.mdWest;
	}

	@Override
	public Integer getMDSouth() throws RemoteException {
		return this.mdSouth;
	}

	public Integer computeMDNorth() {
		return Math.abs(this.robotGrid.getX() - targetGridNorth.getX())
				+ Math.abs(this.robotGrid.getY() - targetGridNorth.getY());
	}

	public Integer computeMDEast() {
		return Math.abs(this.robotGrid.getX() - targetGridEast.getX())
				+ Math.abs(this.robotGrid.getY() - targetGridEast.getY());
	}

	public Integer computeMDWest() {
		return Math.abs(this.robotGrid.getX() - targetGridWest.getX())
				+ Math.abs(this.robotGrid.getY() - targetGridWest.getY());
	}

	public Integer computeMDSouth() {
		return Math.abs(this.robotGrid.getX() - targetGridSouth.getX())
				+ Math.abs(this.robotGrid.getY() - targetGridSouth.getY());
	}

	@Override
	public void addRobotMDNorth(String robotAddress, Integer mdNorth) throws RemoteException {
		this.allRobotsMDsNorth.put(robotAddress, mdNorth);
	}

	@Override
	public void addRobotMDEast(String robotAddress, Integer mdEast) throws RemoteException {
		this.allRobotsMDsEast.put(robotAddress, mdEast);
	}

	@Override
	public void addRobotMDWest(String robotAddress, Integer mdWest) throws RemoteException {
		this.allRobotsMDsWest.put(robotAddress, mdWest);
	}

	@Override
	public void addRobotMDSouth(String robotAddress, Integer mdSouth) throws RemoteException {
		this.allRobotsMDsSouth.put(robotAddress, mdSouth);
	}

	@Override
	public Map<String, Integer> getAllRobotsMDsNorth() throws RemoteException {
		return this.allRobotsMDsNorth;
	}

	@Override
	public Map<String, Integer> getAllRobotsMDsEast() throws RemoteException {
		return this.allRobotsMDsEast;
	}

	@Override
	public Map<String, Integer> getAllRobotsMDsWest() throws RemoteException {
		return this.allRobotsMDsWest;
	}

	@Override
	public Map<String, Integer> getAllRobotsMDsSouth() throws RemoteException {
		return this.allRobotsMDsSouth;
	}

	/**
	 * http://stackoverflow.com/questions/363681/generating-random-integers-in-a
	 * -range-with-java
	 */
	public Grid initializeRobotGrid() {
		Random random = new Random();
		int x = random.nextInt((MAX - MIN) + 1) + MIN;
		int y = random.nextInt((MAX - MIN) + 1) + MIN;
		return new Grid(x, y);
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public void setRobotAddress(String robotAddress) {
		this.robotIPAddress = robotAddress;
	}

	public void setRobotGrid(Grid robotGrid) {
		this.robotGrid = robotGrid;
	}

	@Override
	public String getRobotName() {
		return this.robotName;
	}

	@Override
	public String getRobotAddress() {
		return this.robotIPAddress;
	}

	public Grid getRobotGrid() {
		return this.robotGrid;
	}

	public void printRobotInfo() throws RemoteException {
		System.out.println("ROBOT NAME: " + this.getRobotName());
		System.out.println("ROBOT ADDRESS: " + this.getRobotAddress());
		System.out.println("ROBOT GRID: " + "(" + this.getRobotGrid().getX() + "," + this.getRobotGrid().getY() + ")");
	}

	/**
	 * http://stackoverflow.com/questions/12184378/sorting-linkedhashmap
	 * 
	 * @param linkedHashMap
	 * @return
	 */
	private Map<String, Integer> sortRobotMDsAllDirections(Map<String, Integer> linkedHashMap) {
		List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(linkedHashMap.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Map.Entry<String, Integer> value1, Map.Entry<String, Integer> value2) {
				return value1.getValue().compareTo(value2.getValue());
			}
		});

		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private void displayMenu() {
		System.out.println("1. Manhattan Distances");
		System.out.println("2. Obtain Consensus");
		System.out.println("3. Selected Robots");
		System.out.println("4. Enclose Target");
		System.out.println("5. Robot Grids ");
		System.out.println("6. Find Target ");
		System.out.println("7. Malicious Robots");
		System.out.println("0. Quit Program");
	}

	private void viewSelectedRobots() {
		System.out.println("NORTH TARGET ROBOT: " + this.getSelectedRobotNorth());
		System.out.println("EAST TARGET ROBOT: " + this.getSelectedRobotEast());
		System.out.println("WEST TARGET ROBOT: " + this.getSelectedRobotWest());
		System.out.println("SOUTH TARGET ROBOT: " + this.getSelectedRobotSouth());
	}

	public void updateMDs(Collection<String> ips) {
		for (String ip : ips) {
			if (!ip.equals(this.getRobotAddress())) {
				try {
					IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
					robotRef.addRobotMDNorth(this.getRobotAddress(), this.getMDNorth());
					robotRef.addRobotMDEast(this.getRobotAddress(), this.getMDEast());
					robotRef.addRobotMDWest(this.getRobotAddress(), this.getMDWest());
					robotRef.addRobotMDSouth(this.getRobotAddress(), this.getMDSouth());
					this.addRobotMDNorth(robotRef.getRobotAddress(), robotRef.getMDNorth());
					this.addRobotMDEast(robotRef.getRobotAddress(), robotRef.getMDEast());
					this.addRobotMDWest(robotRef.getRobotAddress(), robotRef.getMDWest());
					this.addRobotMDSouth(robotRef.getRobotAddress(), robotRef.getMDSouth());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void selectRobotsForTargetGrid(Collection<String> robotIPAddresses) {
		try {
			Map.Entry<String, Integer> firstNorthRobot = this.getAllRobotsMDsNorth().entrySet().iterator().next();
			Map.Entry<String, Integer> firstEastRobot = this.getAllRobotsMDsEast().entrySet().iterator().next();
			Map.Entry<String, Integer> firstWestRobot = this.getAllRobotsMDsWest().entrySet().iterator().next();
			Map.Entry<String, Integer> firstSouthRobot = this.getAllRobotsMDsSouth().entrySet().iterator().next();
			this.selectedRobotNorth = firstNorthRobot.getKey();
			this.selectedRobotEast = firstEastRobot.getKey();
			this.selectedRobotWest = firstWestRobot.getKey();
			this.selectedRobotSouth = firstSouthRobot.getKey();

			if (this.selectedRobotEast.equals(this.selectedRobotNorth)) {
				this.getAllRobotsMDsEast().remove(firstEastRobot.getKey(), firstEastRobot.getValue());
				firstEastRobot = this.getAllRobotsMDsEast().entrySet().iterator().next();
				this.selectedRobotEast = firstEastRobot.getKey();
			}

			if (this.selectedRobotWest.equals(this.selectedRobotNorth)
					|| this.selectedRobotWest.equals(this.selectedRobotEast)) {
				this.getAllRobotsMDsWest().remove(firstWestRobot.getKey(), firstWestRobot.getValue());
				firstWestRobot = this.getAllRobotsMDsWest().entrySet().iterator().next();
				this.selectedRobotWest = firstWestRobot.getKey();
			}

			if (this.selectedRobotWest.equals(this.selectedRobotNorth)
					|| this.selectedRobotWest.equals(this.selectedRobotEast)) {
				this.getAllRobotsMDsWest().remove(firstWestRobot.getKey(), firstWestRobot.getValue());
				firstWestRobot = this.getAllRobotsMDsWest().entrySet().iterator().next();
				this.selectedRobotWest = firstWestRobot.getKey();
			}

			if (this.selectedRobotSouth.equals(this.selectedRobotNorth)
					|| this.selectedRobotSouth.equals(this.selectedRobotEast)
					|| this.selectedRobotSouth.equals(this.selectedRobotWest)) {
				this.getAllRobotsMDsSouth().remove(firstSouthRobot.getKey(), firstSouthRobot.getValue());
				firstSouthRobot = this.getAllRobotsMDsSouth().entrySet().iterator().next();
				this.selectedRobotSouth = firstSouthRobot.getKey();
			}

			if (this.selectedRobotSouth.equals(this.selectedRobotNorth)
					|| this.selectedRobotSouth.equals(this.selectedRobotEast)
					|| this.selectedRobotSouth.equals(this.selectedRobotWest)) {
				this.getAllRobotsMDsSouth().remove(firstSouthRobot.getKey(), firstSouthRobot.getValue());
				firstSouthRobot = this.getAllRobotsMDsSouth().entrySet().iterator().next();
				this.selectedRobotSouth = firstSouthRobot.getKey();
			}

			if (this.selectedRobotSouth.equals(this.selectedRobotNorth)
					|| this.selectedRobotSouth.equals(this.selectedRobotEast)
					|| this.selectedRobotSouth.equals(this.selectedRobotWest)) {
				this.getAllRobotsMDsSouth().remove(firstSouthRobot.getKey(), firstSouthRobot.getValue());
				firstSouthRobot = this.getAllRobotsMDsSouth().entrySet().iterator().next();
				this.selectedRobotSouth = firstSouthRobot.getKey();
			}

			System.out.println("FIRST NORTH ROBOT: " + this.selectedRobotNorth);
			System.out.println("FIRST EAST ROBOT: " + this.selectedRobotEast);
			System.out.println("FIRST WEST ROBOT: " + this.selectedRobotWest);
			System.out.println("FIRST SOUTH ROBOT: " + this.selectedRobotSouth);

			this.reachNaiveConsensus(this.selectedRobotNorth, this.selectedRobotEast, this.selectedRobotWest,
					this.selectedRobotSouth, robotIPAddresses);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reachNaiveConsensus(String selectedRobotNorth, String selectedRobotEast, String selectedRobotWest,
			String selectedRobotSouth, Collection<String> robotIPAddresses) {
		for (String ip : robotIPAddresses) {
			if (!this.getRobotAddress().equals(ip)) {
				try {
					IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
					robotRef.setSelectedRobotNorth(selectedRobotNorth);
					robotRef.setSelectedRobotEast(selectedRobotEast);
					robotRef.setSelectedRobotWest(selectedRobotWest);
					robotRef.setSelectedRobotSouth(selectedRobotSouth);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void computeTargetEnclosure() {
		this.targetGridNorth = new Grid(this.targetGrid.getX(), this.targetGrid.getY() - 1);
		this.targetGridEast = new Grid(this.targetGrid.getX() + 1, this.targetGrid.getY());
		this.targetGridWest = new Grid(this.targetGrid.getX() - 1, this.targetGrid.getY());
		this.targetGridSouth = new Grid(this.targetGrid.getX(), this.targetGrid.getY() + 1);
	}

	public static void main(String[] args) {
		try {
			Robot robot = new Robot();
			String robotName = InetAddress.getLocalHost().getHostName();
			String robotAddress = InetAddress.getLocalHost().getHostAddress();
			Grid robotGrid = robot.initializeRobotGrid();

			robot.setRobotName(robotName);
			robot.setRobotAddress(robotAddress);
			robot.setRobotGrid(robotGrid);

			robot.printRobotInfo();

			robot.computeTargetEnclosure();

			IInitiator initiatorRef = (IInitiator) Naming.lookup("rmi://" + "129.21.30.37" + "/initiator");
			initiatorRef.setBootstrappingRobot(robot.getRobotAddress());
			Map<String, String> robotIPs = new HashMap<String, String>();
			robotIPs = initiatorRef.getRobots();
			initiatorRef.logRobot(robotName, robotAddress, robotGrid);

			Integer mdNorth = robot.computeMDNorth();
			robot.setMDNorth(mdNorth);
			robot.addRobotMDNorth(robotAddress, robot.getMDNorth());

			Integer mdEast = robot.computeMDEast();
			robot.setMDEast(mdEast);
			robot.addRobotMDEast(robotAddress, robot.getMDEast());

			Integer mdWest = robot.computeMDWest();
			robot.setMDWest(mdWest);
			robot.addRobotMDWest(robotAddress, robot.getMDWest());

			Integer mdSouth = robot.computeMDSouth();
			robot.setMDSouth(mdSouth);
			robot.addRobotMDSouth(robotAddress, robot.getMDSouth());

			IRobot robotRef = robot;
			Naming.rebind("robot", robotRef);

			robot.updateMDs(robotIPs.values());

			robot.displayMenu();

			Scanner scanner = new Scanner(System.in);
			int choice = scanner.nextInt();

			while (choice != 0) {

				if (choice == 1) {
					String bootstrapper = initiatorRef.getBootstrappingRobot();

					if (robot.getRobotAddress().equals(bootstrapper)) {
						robot.allRobotsMDsNorth = robot.sortRobotMDsAllDirections(robot.getAllRobotsMDsNorth());
						robot.allRobotsMDsEast = robot.sortRobotMDsAllDirections(robot.getAllRobotsMDsEast());
						robot.allRobotsMDsWest = robot.sortRobotMDsAllDirections(robot.getAllRobotsMDsWest());
						robot.allRobotsMDsSouth = robot.sortRobotMDsAllDirections(robot.getAllRobotsMDsSouth());

						for (String ip : initiatorRef.getRobots().values()) {
							if (!ip.equals(robot.getRobotAddress())) {
								IRobot peerRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								peerRef.setAllRobotsMDsNorth(robot.allRobotsMDsNorth);
								peerRef.setAllRobotsMDsEast(robot.allRobotsMDsEast);
								peerRef.setAllRobotsMDsWest(robot.allRobotsMDsWest);
								peerRef.setAllRobotsMDsSouth(robot.allRobotsMDsSouth);
							}
						}
					}

					System.out.println("NORTH MDs: " + robot.getAllRobotsMDsNorth().toString());
					System.out.println("EAST MDs: " + robot.getAllRobotsMDsEast().toString());
					System.out.println("WEST MDs: " + robot.getAllRobotsMDsWest().toString());
					System.out.println("SOUTH MDs: " + robot.getAllRobotsMDsSouth().toString());
				}

				else if (choice == 2)
					robot.selectRobotsForTargetGrid(initiatorRef.getRobots().values());

				else if (choice == 3)
					robot.viewSelectedRobots();

				else if (choice == 4) {
					Map<String, String> selectedRobots = new HashMap<String, String>();
					selectedRobots.put(robot.getSelectedRobotNorth(), "N");
					selectedRobots.put(robot.getSelectedRobotEast(), "E");
					selectedRobots.put(robot.getSelectedRobotWest(), "W");
					selectedRobots.put(robot.getSelectedRobotSouth(), "S");

					for (Map.Entry<String, String> selectedRobot : selectedRobots.entrySet()) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									IRobot robotRef1 = (IRobot) Naming
											.lookup("rmi://" + selectedRobot.getKey() + "/robot");
									robotRef1.encloseTarget(new ArrayList<String>(selectedRobots.keySet()),
											selectedRobot.getValue());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}

				else if (choice == 5) {
					for (Map.Entry<Grid, Boolean> entry : robot.getNewRobotGrids().entrySet()) {
						if (entry.getValue() == false) {
							robot.setRobotGrid(entry.getKey());
							robot.removeTakenRobotGrid(robot.getRobotGrid());

							for (String ip : initiatorRef.getRobots().values()) {
								if (!ip.equals(robot.getRobotAddress())) {
									try {
										IRobot peerRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
										peerRef.updateNewRobotGrids(robot.getNewRobotGrids());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							break;
						}
					}

					robot.setTargetGridX(initiatorRef.getTargetGrid().getX());
					robot.setTargetGridY(initiatorRef.getTargetGrid().getY());
					robot.setRobotGridX(robot.getRobotGrid().getX());
					robot.setRobotGridY(robot.getRobotGrid().getY());

					System.out
							.println("ROBOT LOCATION: (" + robot.getRobotGridX() + ", " + robot.getRobotGridY() + ")");
					System.out.println(
							"TARGET LOCATION: (" + robot.getTargetGridX() + ", " + robot.getTargetGridY() + ")");
				}

				else if (choice == 6 || choice == 7) {

					String bootstrapper = initiatorRef.getBootstrappingRobot();

					if (robot.getRobotAddress().equals(bootstrapper)) {
						for (String ip : initiatorRef.getRobots().values()) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									try {

										// int bsRobotGridX =
										// robot.getRobotGridX();

										// int bsRobotGridY =
										// robot.getRobotGridY();

										// robot.findTarget(initiatorRef.getRobots().values(),
										// bootstrapper, bsRobotGridX,
										// bsRobotGridY);

										if (!ip.equals(robot.getRobotAddress())) {
											IRobot peerRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
											int robotGridX = peerRef.getRobotGridX();
											int robotGridY = peerRef.getRobotGridY();

											peerRef.findTarget(new ArrayList<String>(initiatorRef.getRobots().values()),
													"", robotGridX, robotGridY, initiatorRef.getMaliciousRobot());
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
						}
					}
				}

				else if (choice == 0)
					System.exit(1);

				robot.displayMenu();
				choice = scanner.nextInt();
			}

			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void findTarget(Collection<String> robots, String bootstrapper, int robotGridX, int robotGridY,
			String maliciousRobot) {

		if (this.getRobotAddress().equals(bootstrapper) && this.visitedBSTargetFind == false) {
			this.visitedBSTargetFind = true;
		} else if (this.getRobotAddress().equals(bootstrapper) && this.visitedBSTargetFind) {
			return;
		}

		int initialRobotGridX = robotGridX;
		Grid targetLocation = new Grid();
		Grid robotYAbove = new Grid();
		Grid robotYBelow = new Grid();
		Grid robotXFront = new Grid();

		if (initialRobotGridX == 0) {
			while (robotGridX <= ROW) {
				if (this.getTargetFound() == false || maliciousRobot != null) {
					robotGridX = this.getRobotGrid().getX();
					robotGridY = this.getRobotGrid().getY();

					robotYAbove.setX(robotGridX);
					robotYAbove.setY(robotGridY - 1);

					robotYBelow.setX(robotGridX);
					robotYBelow.setY(robotGridY + 1);

					robotXFront.setX(robotGridX + 1);
					robotXFront.setY(robotGridY);

					if (robotGridX != ROW) {
						this.getRobotGrid().setX(++robotGridX);
						robotXFront.setX(robotGridX + 1);
						robotXFront.setY(robotGridY);
					}

					robotYAbove.setX(robotGridX);
					robotYAbove.setY(robotGridY - 1);

					robotYBelow.setX(robotGridX);
					robotYBelow.setY(robotGridY + 1);

					System.out.println(this.getRobotName() + " took " + this.getRobotGrid().getX() + ", " + robotGridY
							+ " to find Target");

					if (robotYAbove.getX() == targetGridX && robotYAbove.getY() == targetGridY) {
						this.targetFoundByRobots.put(this.getRobotName(), this.getRobotAddress());
						this.targetFound = !this.targetFound;
						targetLocation = robotYAbove;
					}

					else if (robotYBelow.getX() == targetGridX && robotYBelow.getY() == targetGridY) {
						this.targetFoundByRobots.put(this.getRobotName(), this.getRobotAddress());
						this.targetFound = true;
						targetLocation = robotYBelow;
					}

					else if (robotXFront.getX() == targetGridX && robotXFront.getY() == targetGridY) {
						this.targetFoundByRobots.put(this.getRobotName(), this.getRobotAddress());
						this.targetFound = true;
						targetLocation = robotXFront;
					}

					for (String ip : robots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());

								for (Map.Entry<String, String> awesomeRobots : this.targetFoundByRobots.entrySet()) {
									if (this.getTargetFound() == true
											&& awesomeRobots.getValue().equals(this.getRobotAddress())) {
										robotRef.proclaimRobotsToFindTarget(this.targetFoundByRobots);
										robotRef.setTargetGridLocation(targetLocation);
										robotRef.setTargetFound(this.getTargetFound());
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} else if (this.getTargetFound()) {
					for (Map.Entry<String, String> printRobots : this.targetFoundByRobots.entrySet())
						System.out.println("TARGET FOUND BY: " + printRobots.getKey());
					return;
				}
			}
		} else if (initialRobotGridX == 8) {
			while (robotGridX >= 0) {
				if (this.getTargetFound() == false || maliciousRobot != null) {
					robotGridX = this.getRobotGrid().getX();
					robotGridY = this.getRobotGrid().getY();

					robotYAbove.setX(robotGridX);
					robotYAbove.setY(robotGridY - 1);

					robotYBelow.setX(robotGridX);
					robotYBelow.setY(robotGridY + 1);

					robotXFront.setX(robotGridX - 1);
					robotXFront.setY(robotGridY);

					if (robotGridX != 0) {
						this.getRobotGrid().setX(--robotGridX);
						robotXFront.setX(robotGridX - 1);
						robotXFront.setY(robotGridY);
					}

					robotYAbove.setX(robotGridX);
					robotYAbove.setY(robotGridY - 1);

					robotYBelow.setX(robotGridX);
					robotYBelow.setY(robotGridY + 1);

					System.out.println(this.getRobotName() + " took " + this.getRobotGrid().getX() + ", " + robotGridY
							+ " to find Target");

					if (robotYAbove.getX() == targetGridX && robotYAbove.getY() == targetGridY) {
						this.targetFoundByRobots.put(this.getRobotName(), this.getRobotAddress());
						this.targetFound = !this.targetFound;
						targetLocation = robotYAbove;
					}

					else if (robotYBelow.getX() == targetGridX && robotYBelow.getY() == targetGridY) {
						this.targetFoundByRobots.put(this.getRobotName(), this.getRobotAddress());
						this.targetFound = true;
						targetLocation = robotYBelow;
					}

					else if (robotXFront.getX() == targetGridX && robotXFront.getY() == targetGridY) {
						this.targetFoundByRobots.put(this.getRobotName(), this.getRobotAddress());
						this.targetFound = true;
						targetLocation = robotXFront;
					}

					for (String ip : robots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());

								for (Map.Entry<String, String> awesomeRobots : this.targetFoundByRobots.entrySet()) {
									if (this.getTargetFound() == true
											&& awesomeRobots.getValue().equals(this.getRobotAddress())) {
										robotRef.proclaimRobotsToFindTarget(this.targetFoundByRobots);
										robotRef.setTargetGridLocation(targetLocation);
										robotRef.setTargetFound(this.getTargetFound());
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} else if (this.getTargetFound()) {
					for (Map.Entry<String, String> printRobots : this.targetFoundByRobots.entrySet())
						System.out.println("TARGET FOUND BY: " + printRobots.getKey());
					return;
				}
			}
		}
	}

	private boolean getTargetFound() {
		return this.targetFound;
	}

	@Override
	public void encloseTarget(Collection<String> selectedRobots, String selectedRobot) throws RemoteException {
		try {
			int robotX = this.getRobotGrid().getX();
			int robotY = this.getRobotGrid().getY();
			int targetX = 0;
			int targetY = 0;

			if (selectedRobot.equals("N")) {
				targetX = targetGridNorth.getX();
				targetY = targetGridNorth.getY();
			} else if (selectedRobot.equals("E")) {
				targetX = targetGridEast.getX();
				targetY = targetGridEast.getY();
			} else if (selectedRobot.equals("W")) {
				targetX = targetGridWest.getX();
				targetY = targetGridWest.getY();
			} else if (selectedRobot.equals("S")) {
				targetX = targetGridSouth.getX();
				targetY = targetGridSouth.getY();
			}

			if (robotX < targetX && robotY < targetY) {
				while (robotX < targetX) {
					robotX = this.getRobotGrid().getX();
					this.getRobotGrid().setX(++robotX);
					System.out.println(this.getRobotName() + " took " + this.getRobotGrid().getX() + ", " + robotY);
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				while (robotY < targetY) {
					robotY = this.getRobotGrid().getY();
					this.getRobotGrid().setY(++robotY);
					System.out.println(this.getRobotName() + " took " + robotX + ", " + this.getRobotGrid().getY());
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			else if (robotX < targetX && robotY > targetY) {
				while (robotX < targetX) {
					robotX = this.getRobotGrid().getX();
					this.getRobotGrid().setX(++robotX);
					System.out.println(this.getRobotName() + " took " + this.getRobotGrid().getX() + ", " + robotY);
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				while (robotY > targetY) {
					robotY = this.getRobotGrid().getY();
					this.getRobotGrid().setY(--robotY);
					System.out.println(this.getRobotName() + " took " + robotX + ", " + this.getRobotGrid().getY());
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			else if (robotX > targetX && robotY < targetY) {
				while (robotX > targetX) {
					robotX = this.getRobotGrid().getX();
					this.getRobotGrid().setX(--robotX);
					System.out.println(this.getRobotName() + " took " + this.getRobotGrid().getX() + ", " + robotY);
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				while (robotY < targetY) {
					robotY = this.getRobotGrid().getY();
					this.getRobotGrid().setY(++robotY);
					System.out.println(this.getRobotName() + " took " + robotX + ", " + this.getRobotGrid().getY());
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			else if (robotX > targetX && robotY > targetY) {
				while (robotX > targetX) {
					robotX = this.getRobotGrid().getX();
					this.getRobotGrid().setX(--robotX);
					System.out.println(this.getRobotName() + " took " + this.getRobotGrid().getX() + ", " + robotY);
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				while (robotY > targetY) {
					robotY = this.getRobotGrid().getY();
					this.getRobotGrid().setY(--robotY);
					System.out.println(this.getRobotName() + " took " + robotX + ", " + this.getRobotGrid().getY());
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			else if (robotX == targetX && robotY < targetY) {

				while (robotY < targetY) {
					robotY = this.getRobotGrid().getY();
					this.getRobotGrid().setY(++robotY);
					System.out.println(this.getRobotName() + " took " + robotX + ", " + this.getRobotGrid().getY());
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			else if (robotX == targetX && robotY > targetY) {

				while (robotY > targetY) {
					robotY = this.getRobotGrid().getY();
					this.getRobotGrid().setY(--robotY);
					System.out.println(this.getRobotName() + " took " + robotX + ", " + this.getRobotGrid().getY());
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			else if (robotX > targetX && robotY == targetY) {
				while (robotX > targetX) {
					robotX = this.getRobotGrid().getX();
					this.getRobotGrid().setX(--robotX);
					System.out.println(this.getRobotName() + " took " + this.getRobotGrid().getX() + ", " + robotY);
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			else if (robotX < targetX && robotY == targetY) {
				while (robotX < targetX) {
					robotX = this.getRobotGrid().getX();
					this.getRobotGrid().setX(++robotX);
					System.out.println(this.getRobotName() + " took " + this.getRobotGrid().getX() + ", " + robotY);
					for (String ip : selectedRobots) {
						if (!ip.equals(this.getRobotAddress())) {
							try {
								IRobot robotRef = (IRobot) Naming.lookup("rmi://" + ip + "/robot");
								robotRef.updateGridLocation(ip, this.getRobotGrid());
								robotRef.printGridLocation(this.getRobotAddress(), this.getRobotGrid());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printGridLocation(String ip, Grid grid) throws RemoteException {
		System.out.println(ip + " is now at " + grid.getX() + ", " + grid.getY());
	}

	@Override
	public void setAllRobotsMDsNorth(Map<String, Integer> allRobotsMDsNorth) throws RemoteException {
		this.allRobotsMDsNorth = allRobotsMDsNorth;
	}

	@Override
	public void setAllRobotsMDsEast(Map<String, Integer> allRobotsMDsEast) throws RemoteException {
		this.allRobotsMDsEast = allRobotsMDsEast;
	}

	@Override
	public void setAllRobotsMDsWest(Map<String, Integer> allRobotsMDsWest) throws RemoteException {
		this.allRobotsMDsWest = allRobotsMDsWest;
	}

	@Override
	public void setAllRobotsMDsSouth(Map<String, Integer> allRobotsMDsSouth) throws RemoteException {
		this.allRobotsMDsSouth = allRobotsMDsSouth;
	}

	@Override
	public void printHello() throws RemoteException {
		System.out.println("Hi, there!");
	}

	@Override
	public void setTargetGridLocation(Grid targetGrid) throws RemoteException {
		this.targetGrid = targetGrid;
	}

	@Override
	public Grid getTargetGridLocation(Grid targetGrid) throws RemoteException {
		return this.targetGrid;
	}

	@Override
	public void setTargetFound(boolean targetFound) throws RemoteException {
		this.targetFound = targetFound;
	}

	@Override
	public void updateNewRobotGrids(Map<Grid, Boolean> newRobotGrids) throws RemoteException {
		this.newRobotGrids = newRobotGrids;
	}

	private Map<Grid, Boolean> getNewRobotGrids() {
		return this.newRobotGrids;
	}

	@Override
	public int getRobotGridX() {
		return this.robotGridX;
	}

	@Override
	public int getRobotGridY() {
		return this.robotGridY;
	}

	private int getTargetGridX() {
		return this.targetGridX;
	}

	private int getTargetGridY() {
		return this.targetGridY;
	}

	private void setRobotGridX(int x) {
		this.robotGridX = x;
	}

	private void setRobotGridY(int y) {
		this.robotGridY = y;
	}

	private void setTargetGridX(int x) {
		this.targetGridX = x;
	}

	private void setTargetGridY(int y) {
		this.targetGridY = y;
	}

	private void removeTakenRobotGrid(Grid robotGrid) {
		this.getNewRobotGrids().remove(robotGrid);
	}

	@Override
	public void proclaimRobotsToFindTarget(Map<String, String> targetFoundByRobots) throws RemoteException {
		this.targetFoundByRobots = targetFoundByRobots;
	}

}