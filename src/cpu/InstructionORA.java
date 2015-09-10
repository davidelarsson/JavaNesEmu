package cpu;

public class InstructionORA extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionORA(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	@Override
	public void execute() {
		int value = mode.read();
		int acc = CpuRegisters.getInstance().getRegA();
		value |= acc;

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
		CpuRegisters.getInstance().setRegA(value);

		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "ORA instruction with " + mode;
	}



}
