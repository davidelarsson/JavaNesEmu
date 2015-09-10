package cpu;

public class InstructionPLP extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;

	public InstructionPLP(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Push the status register on stack
	 */
	@Override
	public void execute() {
		// Save Break flag
		boolean breakFlag = CpuRegisters.getInstance().isBreakFlag();

		int flags = pullStack();
		CpuRegisters.getInstance().setFlagByte(flags);
		
		// Make sure Break flag is not touched.
		CpuRegisters.getInstance().setBreakFlag(breakFlag);
		
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "PLP instruction with " + mode;
	}

}
