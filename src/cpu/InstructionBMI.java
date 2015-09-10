package cpu;

public class InstructionBMI extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionBMI(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * The instruction is 2 bytes in length, so PC is incremented by 2. If
	 * negative flag is set, add whatever mode.read() returns to PC.
	 */
	@Override
	public void execute() {
		int targetAddress = mode.read();
		int pc = CpuRegisters.getInstance().getPC();

		if (CpuRegisters.getInstance().isNegative()) {
			CpuRegisters.getInstance().addClockCycles(1);
			CpuRegisters.getInstance().setPC(targetAddress + length);
		} else
			CpuRegisters.getInstance().setPC(pc + length);

		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "BMI instruction with " + mode;
	}

}
