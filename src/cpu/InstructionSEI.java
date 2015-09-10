package cpu;

public class InstructionSEI extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionSEI(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Sets Interrupt flag
	 */
	@Override
	public void execute() {

		CpuRegisters.getInstance().setInterrupt(true);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "SEI instruction with " + mode;
	}


}
