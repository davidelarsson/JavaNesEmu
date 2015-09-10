package cpu;

public class InstructionSTY extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;

	public InstructionSTY(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
		this.addPageBoundaryCycle = false;
	}

	/**
	 * Store the value in the Y register according to the current addressing mode
	 */
	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegY();
		mode.write(value);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "STY instruction with " + mode;
	}

}
