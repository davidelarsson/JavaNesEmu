package cpu;

public class InstructionPHP extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;

	public InstructionPHP(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
		this.addPageBoundaryCycle = false;
	}

	/**
	 * Push the status register on stack. PHP always sets the Break flag before
	 * pushing to stack.
	 */
	@Override
	public void execute() {

		CpuRegisters.getInstance().setBreakFlag(true);
		int value = CpuRegisters.getInstance().getFlagByte();
		pushStack(value);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "PHP instruction with " + mode;
	}

}
