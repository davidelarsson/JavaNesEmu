package cpu;

public class InstructionCLC extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionCLC(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Clears Carry flag
	 */
	@Override
	public void execute() {

		CpuRegisters.getInstance().setCarry(false);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "CLC instruction with " + mode;
	}

}
