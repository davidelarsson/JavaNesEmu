package memory;

public class RAM {

	private int[] memory = new int[0x800];
	
	private static RAM instance;
	private RAM() {		
	}
	public static RAM getInstance() {
		if(instance == null) {
			instance = new RAM();
		}
		return instance;
	}
	
	public int read(int pos) {
		return memory[pos];
	}
	
	public void write(int pos, int val) {
		memory[pos] = val;
	}
	
	
	
	
}
