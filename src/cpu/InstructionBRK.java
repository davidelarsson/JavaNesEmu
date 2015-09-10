package cpu;

import memory.CpuMem;
import debugger.DebugPane;

public class InstructionBRK extends InstructionHandler {
	private AddressingMode mode;
	private int length;
	private int cycles;

	public InstructionBRK(AddressingMode mode, int length, int cycles) {
		this.mode = mode;
		this.length = length;
		this.cycles = cycles;
	}

	/**
	 * First, push flags on stack. Then return address. Then fetch the BRK
	 * vector at $FFFE and transfer it to PC.
	 */
	@Override
	public void execute() {
		CpuRegisters.getInstance().setBreakFlag(true);
		int pc = CpuRegisters.getInstance().getPC();
		pc += length;
		int flagByte = CpuRegisters.getInstance().getFlagByte();
		CpuRegisters.getInstance().setInterrupt(true);
		pushStack(pc >> 8);
		pushStack(pc & 0x00ff);
		pushStack(flagByte);
		int brkVector = CpuMem.getInstance().readMem(0xffff) << 8;
		brkVector |= CpuMem.getInstance().readMem(0xfffe);
		CpuRegisters.getInstance().setPC(brkVector);
		CpuRegisters.getInstance().addClockCycles(cycles);
	}

	@Override
	public String toString() {
		return "BRK instruction with " + mode;
	}

}
