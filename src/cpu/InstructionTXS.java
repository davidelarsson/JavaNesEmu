package cpu;

public class InstructionTXS extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;

	public InstructionTXS(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
		this.addPageBoundaryCycle = false;
	}

	@Override
	public void execute() {
		int value = CpuRegisters.getInstance().getRegX();
		CpuRegisters.getInstance().setStack(value);
		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "TXS instruction with " + mode;
	}

}
