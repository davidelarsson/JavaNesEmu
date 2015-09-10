package cpu;

public class AddressingModeImmediate extends AddressingMode {
	/**
	 * Get the value in memory at IP + 1
	 */
	@Override
	public int read() {
		int value = getFirstArg();
		return value;
	}

	/**
	 * Dummy implementation of write() - naturally we can't write with immediate
	 * addressing mode!
	 */
	@Override
	public void write(int value) {
	}

	public String toString() {
		return "immediate addressing mode";
	}

}
