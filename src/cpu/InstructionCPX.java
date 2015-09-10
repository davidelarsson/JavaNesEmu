package cpu;

public class InstructionCPX extends InstructionHandler {

	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionCPX(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Get a memory value (mem) and compare it to the X register (x)
	 *    negative zero carry
	 * x < mem  * 0 0
	 * x == mem 0 1 1
	 * x > mem  * 0 1
	 * 
	 * where * is bit 8 of the accumulator
	 */
	@Override
	public void execute() {
		int mem = mode.read();
		int x = CpuRegisters.getInstance().getRegX();
		int res = x - mem;

		if (x < mem) {
			CpuRegisters.getInstance().setZero(false);
			CpuRegisters.getInstance().setCarry(false);
		} else if (x == mem) {
			CpuRegisters.getInstance().setZero(true);
			CpuRegisters.getInstance().setCarry(true);
			CpuRegisters.getInstance().setNegative(false);
		} else if (x > mem) {
			CpuRegisters.getInstance().setZero(false);
			CpuRegisters.getInstance().setCarry(true);
		}
		
		if((res & 0x80) != 0x00)
			CpuRegisters.getInstance().setNegative(true);
		else
			CpuRegisters.getInstance().setNegative(false);

		CpuRegisters.getInstance().setPC(
				CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "CPX instruction with " + mode;
	}

}
