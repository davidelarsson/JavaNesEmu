package cpu;

public class InstructionCLD extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionCLD(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Clears Decimal flag
	 */
	@Override
	public void execute() {

		CpuRegisters.getInstance().setDecimal(false);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "CLD instruction with " + mode;
	}

}
