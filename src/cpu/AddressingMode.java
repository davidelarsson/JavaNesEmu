package cpu;

import memory.CpuMem;

abstract class AddressingMode {

	public abstract int read();

	public abstract void write(int value);

	public abstract String toString();

	public int getFirstArg() {
		int address = CpuRegisters.getInstance().getPC();
		address++;
		return CpuMem.getInstance().readMem(address);
	}

	public int getSecondArg() {
		int address = CpuRegisters.getInstance().getPC();
		address += 2;
		return CpuMem.getInstance().readMem(address);
	}
}
