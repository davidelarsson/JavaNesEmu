package cpu;

public class InstructionRTI extends InstructionHandler {

	private int cycles;

	/**
	 * 
	 * @param mode
	 *            - not used
	 * @param length
	 *            - not used
	 * @param cycles
	 *            - used, always set to 6.
	 */
	public InstructionRTI(AddressingMode mode, int length, int cycles) {
		this.cycles = cycles;
	}

	/**
	 * First, pull the status register from stack, then the 16-bit return
	 * address. Transfer address to PC.
	 */
	@Override
	public void execute() {
		int status = pullStack();
		int returnLowByte = pullStack();
		int returnHighByte = pullStack();
		CpuRegisters.getInstance().setFlagByte(status);

		int address = returnHighByte << 8;
		address |= returnLowByte;
		CpuRegisters.getInstance().setPC(address);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "RTS instruction without addressing mode.";
	}

}
