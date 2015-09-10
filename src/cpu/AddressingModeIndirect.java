package cpu;

import memory.CpuMem;

public class AddressingModeIndirect extends AddressingMode {

	/**
	 * First, get the 16-bit argument to the instruction
	 * 
	 * Use this 16-bit argument as an address to get the resulting address that
	 * is returned
	 * 
	 * There's an emulated bug here. We always stay within a 0x100 page. So, for
	 * example, if we have JMP ($22FF), the resulting address is fetched from
	 * $22FF (low byte) and $2200 (high byte), and not $22FF and $2300, as might
	 * be expected.
	 */
	@Override
	public int read() {
		boolean bug = false;
		int lowByte = getFirstArg();
		int highByte = getSecondArg();
		int address = highByte;

		if (lowByte == 0xff)
			bug = true;
		address = address << 8;
		address |= lowByte;

		lowByte = CpuMem.getInstance().readMem(address);
		if (bug)
			address -= 0x100;
		highByte = CpuMem.getInstance().readMem(address + 1);

		address = highByte << 8;
		address |= lowByte;
		return address;
	}

	/**
	 * Dummy implementation of write() - JMP is the only instruction using the
	 * indirect addressing mode, and it doesn't write
	 */
	@Override
	public void write(int value) {
	}

	public String toString() {
		return "indirect addressing mode";
	}

}
