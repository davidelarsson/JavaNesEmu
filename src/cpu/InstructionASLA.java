package cpu;

public class InstructionASLA extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionASLA(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Shift accumulator left
	 */
	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegA();
		value = value << 1;

		// Check for negative flag
		if((value & 0x100) != 0)
			CpuRegisters.getInstance().setCarry(true);
		else
			CpuRegisters.getInstance().setCarry(false);
		
		value &= 0xff;
		CpuRegisters.getInstance().setRegA(value);

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
		return "ASL A instruction";
	}

}
