package cpu;

public class InstructionADC extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionADC(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Add the accumulator with the read value. Then add 1 if carry is set.
	 * Store the result in the accumulator. Then set the flags accordingly.
	 */
	@Override
	public void execute() {
		int mem = mode.read();
		int acc = CpuRegisters.getInstance().getRegA();

		int res = mem + acc;

		if (CpuRegisters.getInstance().isCarry())
			res++;

		// Check for carry flag
		if ((res & 0x100) != 0)
			CpuRegisters.getInstance().setCarry(true);
		else
			CpuRegisters.getInstance().setCarry(false);

		res &= 0xFF;

		CpuRegisters.getInstance().setRegA(res);

		/*
		 * Check for overflow flag
		 * 
		 * If both operands have different signs from the result, set overflow,
		 * otherwise clear overflow.
		 */

		if (((acc & 0x80) == 0) && ((mem & 0x80) == 0) && ((res & 0x80) != 0))
			CpuRegisters.getInstance().setOverflow(true);
		else if (((acc & 0x80) != 0) && ((mem & 0x80) != 0)
				&& ((res & 0x80) == 0))
			CpuRegisters.getInstance().setOverflow(true);
		else
			CpuRegisters.getInstance().setOverflow(false);

		if (res == 0x00)
			CpuRegisters.getInstance().setZero(true);
		else
			CpuRegisters.getInstance().setZero(false);

		// Check for negative flag
		if ((res & 0x80) != 0)
			CpuRegisters.getInstance().setNegative(true);
		else
			CpuRegisters.getInstance().setNegative(false);

		CpuRegisters.getInstance().setPC(
				CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "ADC instruction with " + mode;
	}

}
