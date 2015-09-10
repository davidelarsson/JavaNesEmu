package cpu;

import memory.CpuMem;

public class AddressingModeZeroPage extends AddressingMode {

	/**
	 * Get the value that is pointed at in zero page by PC + 1
	 * 
	 * Get the value at PC + 1
	 * Return the value in memory that address now points at
	 */
	@Override
	public int read() {
		int address = getFirstArg();
		return CpuMem.getInstance().readMem(address);
	}

	/**
	 * Store the value in the memory location that is pointed to by PC + 1
	 */
	@Override
	public void write(int value) {
		int address = getFirstArg();
		CpuMem.getInstance().writeMem(address, value);
	}

	public String toString() {
		return "zero page addressing mode";
	}

}
