package cpu;

import debugger.DebugPane;

public class AddressingModeRelative extends AddressingMode {
	/**
	 * Get the argument, which is a signed byte.
	 */
	@Override
	public int read() {
		int value = getFirstArg();
		int pc = CpuRegisters.getInstance().getPC();
		if((value & 0x80) != 0)
			value -= 0x100;
		return ((pc + value) & 0x0000ffff);
	}

	/**
	 * We never write in realtive mode.
	 */
	@Override
	public void write(int value) {
	}

	public String toString() {
		return "zero page addressing mode";
	}



}
