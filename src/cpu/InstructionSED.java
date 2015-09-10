package cpu;

public class InstructionSED extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionSED(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Sets Decimal flag
	 */
	@Override
	public void execute() {

		CpuRegisters.getInstance().setDecimal(true);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "SED instruction with " + mode;
	}



}
