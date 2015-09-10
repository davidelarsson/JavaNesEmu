package cpu;

public class InstructionJMP extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;
	boolean addPageBoundaryCycle;
	/**
	 */
	public InstructionJMP(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * 
	 */
	@Override
	public void execute() {
		int address = mode.read();
		CpuRegisters.getInstance().setPC(address);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "JMP instruction with " + mode;
	}


}
