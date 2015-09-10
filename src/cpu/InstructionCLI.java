package cpu;

public class InstructionCLI extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionCLI(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Clears Interrupt flag
	 */
	@Override
	public void execute() {

		CpuRegisters.getInstance().setInterrupt(false);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "CLI instruction with " + mode;
	}

}
