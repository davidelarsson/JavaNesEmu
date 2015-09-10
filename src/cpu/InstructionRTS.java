package cpu;

public class InstructionRTS extends InstructionHandler {

	private int cycles;

	/**
	 * Pull a 16-bit address from stack, increment by one and transfer it to PC.
	 * 
	 * @param mode
	 *            - not used
	 * @param length
	 *            - not used
	 * @param cycles
	 *            - used, always set to 6.
	 */
	public InstructionRTS(AddressingMode mode, int length, int cycles) {
		this.cycles = cycles;
	}

	/**
	 * 
	 */
	@Override
	public void execute() {
		int returnLowByte = pullStack();
		int returnHighByte = pullStack();

		int address = returnHighByte << 8;
		address |= returnLowByte;
		address++;
		CpuRegisters.getInstance().setPC(address);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "RTS instruction without addressing mode.";
	}

}
