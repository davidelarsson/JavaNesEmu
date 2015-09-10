package cpu;

public class InstructionSBC extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionSBC(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * Subtract the read memory value from the accumulator. Subtract one. If
	 * carry is set, add one.
	 */
	@Override
	public void execute() {
		int mem = mode.read();
		int acc = CpuRegisters.getInstance().getRegA();
		int res = acc - mem;

		// If carry is zero, decrement result
		if (!CpuRegisters.getInstance().isCarry())
			res--;
		
		// If the result overflows a byte (that is, if bit 8 is set), set carry
		if ((res & 0x100) == 0x100)
			CpuRegisters.getInstance().setCarry(false);
		else
			CpuRegisters.getInstance().setCarry(true);
			    
		// Check for overflow flag
		// FIXME! Stolen code... I dun' get it...
		boolean l = ((CpuRegisters.getInstance().getRegA() ^ res) & (0x80)) != 0;
		boolean r = ((CpuRegisters.getInstance().getRegA() ^ mem) & (0x80)) != 0;
		
		if (l && r)
			CpuRegisters.getInstance().setOverflow(true);
		else
			CpuRegisters.getInstance().setOverflow(false);

		// Check for zero flag
		if (res == 0x00)
			CpuRegisters.getInstance().setZero(true);
		else
			CpuRegisters.getInstance().setZero(false);

		// Check for negative flag
		if ((res & 0x80) != 0)
			CpuRegisters.getInstance().setNegative(true);
		else
			CpuRegisters.getInstance().setNegative(false);

		// Make sure the accumulator only receives a 8-bit value
		CpuRegisters.getInstance().setRegA(res & 0xFF);

		CpuRegisters.getInstance().setPC(
				CpuRegisters.getInstance().getPC() + length);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "SBC instruction with " + mode;
	}

}
