package cpu;

public class InstructionSTA extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;

	public InstructionSTA(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
		this.addPageBoundaryCycle = false;
	}

	/**
	 * Store the value in the A register according to the current addressing mode
	 */
	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegA();
		mode.write(value);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "STA instruction with " + mode;
	}

}
