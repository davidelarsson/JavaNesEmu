package cpu;

import memory.CpuMem;

public class AddressingModeIndirectIndexed extends AddressingMode {

	/**
	 * Get our argument.
	 * 
	 * This argument points to a 16-bit address in zero page.
	 * 
	 * Add the Y register to this 16-bit address.
	 * 
	 * Return the value to which address now points.
	 */
	@Override
	public int read() {

		int arg = getFirstArg();

		// Get the two bytes in zero page that is our start address
		int lowAddress = CpuMem.getInstance().readMem(arg);
		int highAddress = CpuMem.getInstance().readMem((arg + 1) & 0xFF);
		int address = highAddress << 8;
		address |= lowAddress;

		// Now, add our Y register to this address
		address += CpuRegisters.getInstance().getRegY();

		return CpuMem.getInstance().readMem(address);
	}

	public void write(int value) {
		int arg = getFirstArg();
		int lowAddress = CpuMem.getInstance().readMem(arg);
		int highAddress = CpuMem.getInstance().readMem((arg + 1) & 0xFF);
		int address = highAddress << 8;
		address |= lowAddress;
		address += CpuRegisters.getInstance().getRegY();
		CpuMem.getInstance().writeMem(address, value);

	}

	public String toString() {
		return "Indirect Indexed addressing mode";
	}

}
