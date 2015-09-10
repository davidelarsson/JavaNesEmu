package cpu;

public class InstructionSEC extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionSEC(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Sets Carry flag
	 */
	@Override
	public void execute() {

		CpuRegisters.getInstance().setCarry(true);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "SEC instruction with " + mode;
	}

}
