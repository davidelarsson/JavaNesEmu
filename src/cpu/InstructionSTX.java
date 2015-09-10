package cpu;

public class InstructionSTX extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;

	public InstructionSTX(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
		this.addPageBoundaryCycle = false;
	}

	/**
	 * Store the value in the X register according to the current addressing mode
	 */
	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegX();
		mode.write(value);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "STX instruction with " + mode;
	}

}
