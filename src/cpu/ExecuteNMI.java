package cpu;

import memory.CpuMem;

public class ExecuteNMI extends InstructionHandler {
	private int cycles;

	/**
	 * 
	 * @param cycles
	 *            - used, always set to 6.
	 */
	public ExecuteNMI(int cycles) {
		this.cycles = cycles;
	}

	/**
	 * First, push flags on stack. Then return address. Then fetch the BRK
	 * vector at $FFFE and transfer it to PC.
	 */
	@Override
	public void execute() {

//		CpuRegisters.getInstance().setBreakFlag(false);
		int pc = CpuRegisters.getInstance().getPC();
		int lowVector = CpuMem.getInstance().readMemQuiet(0xFFFA);
		int highVector = CpuMem.getInstance().readMemQuiet(0xFFFB);
		CpuRegisters.getInstance().setBreakFlag(false);
		int flagByte = CpuRegisters.getInstance().getFlagByte();
		flagByte &= 0xFF - 0x0;

		// Push the return address on stack
		pushStack(pc >> 8);
		pushStack(pc & 0x00ff);

		// Push the flags on stack, with the interrupt enabled
		CpuRegisters.getInstance().setInterrupt(true);
		pushStack(flagByte);

		// Now take us to the interrupt routine
		int address = (highVector << 8) | lowVector;
		CpuRegisters.getInstance().setPC(address);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "NMI handler";
	}

}
