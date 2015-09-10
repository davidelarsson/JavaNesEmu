package cpu;

import memory.CpuMem;

public class InstructionJSR extends InstructionHandler {
	private int cycles;
	public int read() {
		return 0;
	}

	public void write(int value) {

	}

	/**
	 * 
	 * @param mode
	 *            - not used
	 * @param length
	 *            - not used either
	 * @param cycles
	 *            - used. Always set to 6.
	 */
	public InstructionJSR(AddressingMode mode, int length, int cycles) {
		this.cycles = cycles;
	}

	/**
	 * JSR (Jump Sub Routine)
	 * 
	 * First, get the target address, which is the 16-bit argument of the
	 * instruction. Because it's 16 bits, we can not use mode.read(), which
	 * would only give us the first byte of the argument. Set PC to this address.
	 * 
	 * Then push the 16-bit (former) PC+2 to stack.
	 */
	@Override
	public void execute() {
		int pc = CpuRegisters.getInstance().getPC();
		int targetLowByte = CpuMem.getInstance().readMem(++pc);
		int targetHighByte = CpuMem.getInstance().readMem(++pc);
		int targetAddress = targetHighByte << 8;
		targetAddress |= targetLowByte;
		CpuRegisters.getInstance().setPC(targetAddress);

		// Yeah, Java is Big Endian, while 6502 is Little Endian. Confusing, eh?
		int returnHighByte = (pc & 0x00ff);
		int returnLowByte = (pc & 0xff00) >> 8;

		pushStack(returnLowByte);
		pushStack(returnHighByte);

		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "JSR instruction without addressing mode";
	}

}
