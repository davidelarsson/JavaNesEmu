package cpu;

public class InstructionPHA extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionPHA(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Push the A register on stack
	 */
	@Override
	public void execute() {
		int acc = CpuRegisters.getInstance().getRegA();
		pushStack(acc);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "PHA instruction with " + mode;
	}
}
