package cpu;

import memory.CpuMem;

public class InstructionJMPABS extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;
	/** 
	 * Okay, it's a little ugly, because this class is only used for indirect mode.
	 * 
	 * @param mode		- indirect
	 * @param length	- not used
	 * @param cycles	- used, always set to 3
	 */
	public InstructionJMPABS(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 */
	@Override
	public void execute() {
		int pc = CpuRegisters.getInstance().getPC();
		int address = CpuMem.getInstance().readMem(pc + 1);
		address += (CpuMem.getInstance().readMem(pc + 2) << 8);
		CpuRegisters.getInstance().setPC(address);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "JMP instruction with " + mode;
	}


}
