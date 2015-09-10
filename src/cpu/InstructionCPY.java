package cpu;

public class InstructionCPY extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionCPY(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Get a memory value (mem) and compare it to the Y register (y) 
	 * 				negative		zero 	carry 
	 * y < mem		 *				 0		 0 
	 * y == mem 	 0				 1		 1
	 * y > mem		 *				 0		 1
	 * 
	 * where * is bit 8 of the accumulator
	 */
	@Override
	public void execute() {
		int mem = mode.read();
		int y = CpuRegisters.getInstance().getRegY();
		int res = y - mem;

		if (y < mem) {
			CpuRegisters.getInstance().setZero(false);
			CpuRegisters.getInstance().setCarry(false);
		} else if (y == mem) {
			CpuRegisters.getInstance().setZero(true);
			CpuRegisters.getInstance().setCarry(true);
			CpuRegisters.getInstance().setNegative(false);
		} else if (y > mem) {
			CpuRegisters.getInstance().setZero(false);
			CpuRegisters.getInstance().setCarry(true);
		}

		if((res & 0x80) != 0x00)
			CpuRegisters.getInstance().setNegative(true);
		else
			CpuRegisters.getInstance().setNegative(false);

		CpuRegisters.getInstance().setPC(CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "CPY instruction with " + mode;
	}

}
