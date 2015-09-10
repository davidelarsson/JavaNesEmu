package cpu;

public class InstructionNOP extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	/**
	 * Do nothing.
	 * 
	 * @param mode
	 * @param length
	 * @param cycles
	 */
	public InstructionNOP(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Doesn't do anything
	 */
	@Override
	public void execute() {
		CpuRegisters.getInstance().setPC(
				CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "NOP instruction with " + mode;
	}

}
