package other;
public class NesFile {
	
	private static NesFile instance;

	private int numOfPRG;
	private int numOfCHR;
	private int mapper;
	private boolean batteryBacked;
	private int mirroring;
	
	public static int HORIZONTAL = 0;
	public static int VERTICAL = 1;

	/*
	 * Set to either NesFile.HORIZONTAL or NesFile.VERTICAL
	 */
	public int getMirroring() {
		return mirroring;
	}

	/*
	 * Returns either NesFile.HORIZONTAL or NesFile.VERTICAL
	 */
	public void setMirroring(int mirroring) {
		this.mirroring = mirroring;
	}

	public static NesFile getInstance() {
		if (instance == null)
			instance = new NesFile();
		return instance;
	}
	
	private NesFile() {
		
	}
	
	public int getNumOfPRG() {
		return numOfPRG;
	}

	public void setNumOfPRG(int numOfPRG) {
		this.numOfPRG = numOfPRG;
	}

	public int getNumOfCHR() {
		return numOfCHR;
	}

	public void setNumOfCHR(int numOfCHR) {
		this.numOfCHR = numOfCHR;
	}

	public int getMapper() {
		return mapper;
	}

	public void setMapper(int mapper) {
		this.mapper = mapper;
	}

	public boolean isBatteryBacked() {
		return batteryBacked;
	}

	public void setBatteryBacked(boolean batteryBacked) {
		this.batteryBacked = batteryBacked;
	}

}
