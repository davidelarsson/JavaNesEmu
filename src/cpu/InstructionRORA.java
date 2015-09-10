package cpu;

public class InstructionRORA extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionRORA(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Rotate all bits in accumulator right. Shift all bits right. Carry is
	 * shifted into bit 7, bit 0 is shifted into carry.
	 */
	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegA();

		if (CpuRegisters.getInstance().isCarry())
			value |= 0x100;

		// Check for carry flag
		if ((value & 0x01) != 0)
			CpuRegisters.getInstance().setCarry(true);
		else
			CpuRegisters.getInstance().setCarry(false);

		value = value >> 1;

		CpuRegisters.getInstance().setRegA(value);

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
		return "ROR A instruction";
	}


}
