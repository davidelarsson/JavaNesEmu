package cpu;

public class InstructionLSRA extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionLSRA(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Shift memory location right
	 */
	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegA();

		// Check for carry flag
		if((value & 0x01) != 0)
			CpuRegisters.getInstance().setCarry(true);
		else
			CpuRegisters.getInstance().setCarry(false);

		value = value >> 1;
		value &= 0xff;
		CpuRegisters.getInstance().setRegA(value);

		// Check for zero flag
		if(value == 0x00)
			CpuRegisters.getInstance().setZero(true);
		else
			CpuRegisters.getInstance().setZero(false);
		
		// Negative flag is always clear
		CpuRegisters.getInstance().setNegative(false);

		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "LSR A instruction";
	}

}
