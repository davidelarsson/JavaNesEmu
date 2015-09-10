package cpu;

public class InstructionASL extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionASL(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Shift memory location left
	 */
	@Override
	public void execute() {
		int value = mode.read();
		value = value << 1;

		// Check for negative flag
		if((value & 0x100) != 0)
			CpuRegisters.getInstance().setCarry(true);
		else
			CpuRegisters.getInstance().setCarry(false);
		
		value &= 0xff;
		mode.write(value);

		// Check for negative flag
		if((value & 0x80) != 0x00)
			CpuRegisters.getInstance().setNegative(true);
		else
			CpuRegisters.getInstance().setNegative(false);

		// Check for zero flag
		if(value == 0x00)
			CpuRegisters.getInstance().setZero(true);
		else
			CpuRegisters.getInstance().setZero(false);
		

		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "ASL instruction with " + mode;
	}

}
