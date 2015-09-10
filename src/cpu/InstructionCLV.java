package cpu;

public class InstructionCLV extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionCLV(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Clears Overflow flag
	 */
	@Override
	public void execute() {

		CpuRegisters.getInstance().setOverflow(false);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "CLV instruction with " + mode;
	}


}
