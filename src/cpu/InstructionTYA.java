package cpu;

public class InstructionTYA extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;

	public InstructionTYA(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
		this.addPageBoundaryCycle = false;
	}

	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegY();
		CpuRegisters.getInstance().setRegA(value);

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
		return "TYA instruction with " + mode;
	}

}
