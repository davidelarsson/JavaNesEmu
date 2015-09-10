package cpu;

public class CpuRegisters {

	private static CpuRegisters instance;

	// Registers
	private int regA = 0x00;
	private int regX = 0x00;
	private int regY = 0x00;

	// Program Counter
	private int regPC = 0x0000;

	// Stack Pointer
	private int stack = 0x000000FF;

	// Clock Cycle Counter
	private long clockCycles = 0;

	// Flags
	private boolean zero = false;
	private boolean carry = false;
	private boolean overflow = false;
	private boolean negative = false;
	private boolean interrupt = false;
	private boolean decimal = false;
	private boolean breakFlag = false;

	// Make sure only one instance is ever created by this class
	public static CpuRegisters getInstance() {
		if (instance == null) {
			instance = new CpuRegisters();
		}
		return instance;
	}

	// Empty consructor, only called once, locally
	private CpuRegisters() {
	}

	public int getRegA() {
		return regA;
	}

	public void setRegA(int regA) {
		this.regA = regA & 0x000000FF;
	}

	public int getRegX() {
		return regX;
	}

	public void setRegX(int regX) {
		this.regX = regX & 0x000000FF;
	}

	public int getRegY() {
		return regY;
	}

	public void setRegY(int regY) {
		this.regY = regY & 0x000000FF;
	}

	public int getPC() {
		return regPC;
	}

	public void setPC(int PC) {
		this.regPC = PC & 0x0000FFFF;
	}

	public long getClockCycles() {
		return clockCycles;
	}

	// We don't really need to be able to set this variable to a specific value,
	// only add a handful of cycles to it every once in a while
	public void addClockCycles(int clockCycles) {
		this.clockCycles += clockCycles;
	}

	public int getStack() {
		return stack;
	}

	public void setStack(int stack) {
		this.stack = stack & 0x000000FF;
	}

	public boolean isZero() {
		return zero;
	}

	public void setZero(boolean zero) {
		this.zero = zero;
	}

	public boolean isCarry() {
		return carry;
	}

	public void setCarry(boolean carry) {
		this.carry = carry;
	}

	public boolean isOverflow() {
		return overflow;
	}

	public void setOverflow(boolean overflow) {
		this.overflow = overflow;
	}

	public boolean isNegative() {
		return negative;
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
	}

	public boolean isInterrupt() {
		return interrupt;
	}

	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}

	public boolean isDecimal() {
		return decimal;
	}

	public void setDecimal(boolean decimal) {
		this.decimal = decimal;
	}

	public boolean isBreakFlag() {
		return breakFlag;
	}

	public void setBreakFlag(boolean breakFlag) {
		this.breakFlag = breakFlag;
	}

	public int getFlagByte() {

		// Bit 5 is always set!
		int flagByte = 0x0020;

		if (carry)
			flagByte += 0x0001;
		if (zero)
			flagByte += 0x0002;
		if (interrupt)
			flagByte += 0x0004;
		if (breakFlag)
			flagByte += 0x0008;
		if (decimal)
			flagByte += 0x0010;
		if (overflow)
			flagByte += 0x0040;
		if (negative)
			flagByte += 0x0080;
		return flagByte;
	}

	public void setFlagByte(int val) {

		/*
		 * Yeah, I'm exaggerating a little, but just to make sure we're not
		 * &:ing with a negative value! Stupid Java not to have non-signed
		 * variables...
		 */
		if ((val & 0x0001) != 0)
			carry = true;
		else
			carry = false;

		if ((val & 0x0002) != 0)
			zero = true;
		else
			zero = false;

		if ((val & 0x0004) != 0)
			interrupt = true;
		else
			interrupt = false;

		if ((val & 0x0008) != 0)
			breakFlag = true;
		else
			breakFlag = false;

		if ((val & 0x0010) != 0)
			decimal = true;
		else
			decimal = false;

		if ((val & 0x0040) != 0)
			overflow = true;
		else
			overflow = false;

		if ((val & 0x0080) != 0)
			negative = true;
		else
			negative = false;

	}

	public String toString() {
		return "This class contains all the registers.";
	}

}
