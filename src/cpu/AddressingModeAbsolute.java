package cpu;

import memory.CpuMem;

public class AddressingModeAbsolute extends AddressingMode {

	/**
	 * Get the value that is pointed at by PC + 1
	 */
	@Override
	public int read() {
		int lowByte = getFirstArg();
		int highByte = getSecondArg();
		int address = highByte;
		address = address << 8;
		address |= lowByte;
		return CpuMem.getInstance().readMem(address);
	}

	/**
	 * Store the value in the memory location that is pointed at by PC + 1
	 */
	@Override
	public void write(int value) {
		int lowByte = getFirstArg();
		int highByte = getSecondArg();
		int address = highByte;
		address = address << 8;
		address |= lowByte;
		CpuMem.getInstance().writeMem(address, value);
	}

	public String toString() {
		return "absolute addressing mode";
	}

}
