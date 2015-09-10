package cpu;

import memory.CpuMem;

public class AddressingModeIndexedIndirect extends AddressingMode {

	/**
	 * Get the first argument. Add the X register to it.
	 * 
	 * Now, read the 16-bit (low-endian!) address that 'address' points to
	 * 
	 * Get the value that this 16-bit address points to, and return that value.
	 * 
	 */
	@Override
	public int read() {
		int address = getFirstArg();
		address += CpuRegisters.getInstance().getRegX();
		// Make sure we wrap around in zero page!
		address &= 0xff;
		int lowByte = CpuMem.getInstance().readMem(address);
		int highByte = CpuMem.getInstance().readMem((address + 1) & 0xFF);

		address = (highByte << 8);
		address |= lowByte;
		return CpuMem.getInstance().readMem(address);

	}

	/**
	 * 
	 */
	@Override
	public void write(int value) {
		int address = getFirstArg();
		address += CpuRegisters.getInstance().getRegX();
		// Make sure we wrap around in zero page!
		address &= 0xff;
		int lowByte = CpuMem.getInstance().readMem(address);
		int highByte = CpuMem.getInstance().readMem((address + 1) & 0xFF);

		address = (highByte << 8);
		address |= lowByte;
		CpuMem.getInstance().writeMem(address, value);
	}

	public String toString() {
		return "Indexed Indirect addressing mode";
	}

}
