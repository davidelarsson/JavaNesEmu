package cpu;

import memory.CpuMem;

public class AddressingModeZeroPageX extends AddressingMode {

	/**
	 * Get the value that is pointed at in zero page by (PC + 1) + regX
	 * 
	 * Get the of PC + 1
	 * Add the contents of the X register
	 * Read the value in memory that address now points at
	 */
	@Override
	public int read() {
		int address = getFirstArg();
		address += CpuRegisters.getInstance().getRegX();
		// Make sure we stay in zero page
		address &= 0xff;
		return CpuMem.getInstance().readMem(address);
	}

	/**
	 * Store the value in the memory location that is pointed to by (PC + 1) + regX
	 */
	@Override
	public void write(int value) {
		int address = getFirstArg();
		address += CpuRegisters.getInstance().getRegX();
		address &= 0xff;
		CpuMem.getInstance().writeMem(address, value);
	}

	public String toString() {
		return "zero page indexed X addressing mode";
	}

}
