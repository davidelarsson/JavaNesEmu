package cpu;

public class InstructionBIT extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionBIT(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * AND the value read from memory with the accumulator, without saving the
	 * result. If result it zero, set zero flag. Then copy bit 6 of memory value
	 * to overflow flag, and bit 7 to negative flag.
	 */
	@Override
	public void execute() {
		int mem = mode.read();
		int acc = CpuRegisters.getInstance().getRegA();
		acc &= mem;
		if (acc != 0)
			CpuRegisters.getInstance().setZero(false);
		else
			CpuRegisters.getInstance().setZero(true);

		if ((mem & 0x40) != 0)
			CpuRegisters.getInstance().setOverflow(true);
		else
			CpuRegisters.getInstance().setOverflow(false);

		if ((mem & 0x80) != 0)
			CpuRegisters.getInstance().setNegative(true);
		else
			CpuRegisters.getInstance().setNegative(false);

		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "BIT instruction with " + mode;
	}

}
