package cpu;

import memory.CpuMem;

public class AddressingModeAbsoluteX extends AddressingMode {

	public boolean addCycle = false;
	/**
	 * Get the value that is pointed at by PC + 1
	 * 
	 * Add the X register
	 * 
	 * Return the value in memory now pointed at by address
	 * 
	 */
	@Override
	public int read() {
		int lowByte = getFirstArg();
		int highByte = getSecondArg();
		int address = highByte;
		address = address << 8;
		address |= lowByte;
		int finalAddress = address + CpuRegisters.getInstance().getRegX();
		if((finalAddress & 0xff00) != (address & 0xff00)) {
			addCycle = true;
		} else
			addCycle = false;
		return CpuMem.getInstance().readMem(finalAddress);
	}

	/**
	 * Store the value in the memory location that is pointed at by (PC + 1) + X
	 */
	@Override
	public void write(int value) {
		int lowByte = getFirstArg();
		int highByte = getSecondArg();
		int address = highByte;
		address = address << 8;
		address |= lowByte;
		address += CpuRegisters.getInstance().getRegX();
		CpuMem.getInstance().writeMem(address, value);
	}

	public String toString() {
		return "X register indexed absolute addressing mode";
	}

}
