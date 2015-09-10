package memory;

public class PRGROM {

	private int[] ROM = new int[0x8000];

	private static PRGROM instance;

	private PRGROM() {
	}

	public static PRGROM getInstance() {
		if (instance == null) {
			instance = new PRGROM();
		}
		return instance;
	}

	public int read(int pos) {
		return ROM[pos];
	}

	public void write(int pos, int val) {
		ROM[pos] = val;
	}

}
