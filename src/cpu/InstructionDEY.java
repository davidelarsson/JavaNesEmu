package cpu;

public class InstructionDEY extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionDEY(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Decrement Y
	 */
	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegY();
		value--;
		value &= 0xff;
		CpuRegisters.getInstance().setRegY(value);

		// Check for zero flag
		if(value == 0x00)
			CpuRegisters.getInstance().setZero(true);
		else
			CpuRegisters.getInstance().setZero(false);
		// Check for negative flag
		if((value & 0x80) != 0)
			CpuRegisters.getInstance().setNegative(true);
		else
			CpuRegisters.getInstance().setNegative(false);

		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "DEY instruction with " + mode;
	}

}
