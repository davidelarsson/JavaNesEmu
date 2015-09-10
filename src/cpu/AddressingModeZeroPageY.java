package cpu;

import memory.CpuMem;

public class AddressingModeZeroPageY extends AddressingMode {
	/**
	 * Get the value that is pointed at in zero page by (PC + 1) + regY
	 * 
	 * Get the of PC + 1
	 * Add the contents of the Y register
	 * Read the value in memory that address now points at
	 */
	@Override
	public int read() {
		int address = getFirstArg();
		address += CpuRegisters.getInstance().getRegY();
		// Make sure we stay in zero page
		address &= 0xff;
		return CpuMem.getInstance().readMem(address);
	}

	/**
	 * Store the value in the memory location that is pointed to by (PC + 1) + regY
	 */
	@Override
	public void write(int value) {
		int address = getFirstArg();
		address += CpuRegisters.getInstance().getRegY();
		address &= 0xff;
		CpuMem.getInstance().writeMem(address, value);
	}

	public String toString() {
		return "zero page indexed Y addressing mode";
	}

}
