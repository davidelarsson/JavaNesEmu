package cpu;

public class InstructionIllegal extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	/**
	 * Do nothing, for now.
	 * 
	 * @param mode
	 * @param length
	 * @param cycles
	 */
	public InstructionIllegal(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Doesn't do anything. Like a NOP
	 */
	@Override
	public void execute() {
		CpuRegisters.getInstance().setPC(
				CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "Illegal instruction marker with " + mode;
	}

}
