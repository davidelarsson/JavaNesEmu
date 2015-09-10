package cpu;

import memory.CpuMem;

public class AddressingModeAbsoluteY extends AddressingMode {

	/**
	 * Get the value that is pointed at by PC + 1
	 * 
	 * Add the Y register
	 * 
	 * Return the value in memory now pointed at by address
	 * 
	 */
	@Override
	public int read() {
		int lowByte = getFirstArg();
		int highByte = getSecondArg();
		int address = highByte << 8;
		address |= lowByte;
		address += CpuRegisters.getInstance().getRegY();
		return CpuMem.getInstance().readMem(address);
	}

	/**
	 * Store the value in the memory location that is pointed at by (PC + 1) + Y
	 */
	@Override
	public void write(int value) {
		int lowByte = getFirstArg();
		int highByte = getSecondArg();
		int address = highByte << 8;
		address |= lowByte;
		address += CpuRegisters.getInstance().getRegY();
		CpuMem.getInstance().writeMem(address, value);
	}

	public String toString() {
		return "Y register indexed absolute addressing mode";
	}

}
