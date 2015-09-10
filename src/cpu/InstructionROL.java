package cpu;

public class InstructionROL extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionROL(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Rotate all bits left. Shift all bits left. Carry is shifted into bit 0,
	 * bit 7 is shifted into carry.
	 */
	@Override
	public void execute() {
		int value = mode.read();

		value = value << 1;

		if (CpuRegisters.getInstance().isCarry())
			value |= 0x01;

		// Check for carry flag
		if ((value & 0x100) != 0)
			CpuRegisters.getInstance().setCarry(true);
		else
			CpuRegisters.getInstance().setCarry(false);
		
		value &= 0xff;
		mode.write(value);

		// Check for negative flag
		if ((value & 0x80) != 0)
			CpuRegisters.getInstance().setNegative(true);
		else
			CpuRegisters.getInstance().setNegative(false);


		// Check for zero flag
		if (value == 0x00)
			CpuRegisters.getInstance().setZero(true);
		else
			CpuRegisters.getInstance().setZero(false);

		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "ROL instruction with " + mode;
	}

}
