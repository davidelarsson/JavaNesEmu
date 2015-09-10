package cpu;

import memory.CpuMem;

/**
 * An interface describing an instruction handler.
 */
public abstract class InstructionHandler {
	/**
	 * Handles the instruction.
	 */

	
	public abstract void execute();

	public abstract String toString();
	
	
	/**
	 * Pulls a value from stack
	 * @return
	 */
	public int pullStack() {
		int stack = CpuRegisters.getInstance().getStack();
		stack++;
		//Make sure we stay in Stack Page
		stack &= 0xff;
		int value = CpuMem.getInstance().readMem(0x100 + stack);
		CpuRegisters.getInstance().setStack(stack);
		return value;
	}
	
	/**
	 * Pushes a value on stack
	 * @param val
	 */
	public void pushStack(int val) {
		int stackAddress = 0x100 + CpuRegisters.getInstance().getStack();
		CpuMem.getInstance().writeMem(stackAddress, val);
		stackAddress--;
		//Make sure we stay in Stack Page
		stackAddress &= 0xff;
		CpuRegisters.getInstance().setStack(stackAddress);
	}
}
