package cpu;

public class AddressingModeDummy extends AddressingMode {

	/**
	 * This addressing mode don't do shit!
	 */
	@Override
	public int read() {
		return 0;
	}

	@Override
	public void write(int value) {
	}
	
	public String toString() {
		return "dummy addressing mode";
	}

}
