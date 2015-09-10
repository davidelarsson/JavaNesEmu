package cpu;

public class InstructionLDX extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionLDX(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Read a value from memory according to the current address mode
	 * and store it in the X register
	 */
	@Override
	public void execute() {
		int value = mode.read();
		CpuRegisters.getInstance().setRegX(value);
		
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
		return "LDX instruction with " + mode;
	}

}
