package cpu;

public class InstructionBCC extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionBCC(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * The instruction is 2 bytes in length, so PC is incremented by 2. If
	 * carry flag is clear, add whatever mode.read() returns to PC.
	 */
	@Override
	public void execute() {
		int targetAddress = mode.read();
		int pc = CpuRegisters.getInstance().getPC();

		if (!CpuRegisters.getInstance().isCarry()) {
			CpuRegisters.getInstance().addClockCycles(1);
			CpuRegisters.getInstance().setPC(targetAddress + length);
		} else
			CpuRegisters.getInstance().setPC(pc + length);

		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "BCC instruction with " + mode;
	}

}
