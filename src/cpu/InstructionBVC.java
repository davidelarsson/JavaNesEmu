package cpu;

public class InstructionBVC extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionBVC(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * The instruction is 2 bytes in length, so PC is incremented by 2. If
	 * overflow flag is clear, add whatever mode.read() returns to PC.
	 */
	@Override
	public void execute() {
		int targetAddress = mode.read();
		int pc = CpuRegisters.getInstance().getPC();

		if (!CpuRegisters.getInstance().isOverflow()) {
			CpuRegisters.getInstance().addClockCycles(1);
			CpuRegisters.getInstance().setPC(targetAddress + length);
		} else
			CpuRegisters.getInstance().setPC(pc + length);

		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "BVC instruction with " + mode;
	}

}
