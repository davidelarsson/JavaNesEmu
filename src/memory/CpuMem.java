package memory;

import other.Controller;
import ppu.PpuRegisters;
import debugger.DebugPane;

public class CpuMem {

	private static CpuMem instance;

	/**
	 * Make sure there's only ever one instance of this class
	 */
	public static CpuMem getInstance() {
		if (instance == null) {
			instance = new CpuMem();
		}
		return instance;
	}

	// We really don't need anything done during construction of this one (and
	// always one) object
	private CpuMem() {
	}

	/*
	 * The NES CPU Memory Map:
	 * 
	 * $0000 - $07FF - RAM
	 * $0800 - $0FFF - Mirror of $0000 - $07FF
	 * $1000 - $17FF - Mirror of $0000 - $07FF
	 * $1800 - $1FFF - Mirror of $0000 - $07FF
	 * 
	 * $2000 - $2007 - I/O registers
	 * $2008 - $3FFF - Mirror of $2000 - $2007
	 * 
	 * et.c.
	 */

	/*
	 * Normal read from memory
	 */
	public int readMem(int address) {
		address &= 0xFFFF;

		// First, check whether the read is in RAM
		if ((address >= 0) && (address < 0x2000)) {
			address &= 0x07FF;
			return RAM.getInstance().read(address);
		}

		// In the $2000 - $2007 I/O registers?
		if ((address >= 0x2000) && (address < 0x4000)) {
			address &= 0x000F;
			if (address == 0x0002) // It's really $2002
				return PpuRegisters.getInstance().status();
			else if (address == 0x0004) // It's really $2004
				return PpuRegisters.getInstance().readSpriteData();
			else if (address == 0x0007) // It's really $2007
				return PpuRegisters.getInstance().readPpuMem();
			return 0;
		}

		// In the $4000 - $4017 I/O registers?
		if ((address >= 0x4000) && (address <= 0x4017)) {
			address &= 0x000F;
			if (address == 0x0006) // It's really $4006
				return Controller.getInstance().readController1();
			if (address == 0x0007) // It's really $4007
				return Controller.getInstance().readController2();
			else
				// Dummy
				return 0;
		}

		// In the PRG ROM area?
		else if ((address >= 0x8000) && (address < 0x10000)) {
			address &= 0x7FFF;
			return PRGROM.getInstance().read(address);
		} else

			return 0;
	}

	/*
	 * This is the emulated write to memory.
	 */
	public void writeMem(int address, int value) {
		address &= 0xFFFF;

		// First, check whether the read is in RAM
		if ((address >= 0) && (address < 0x2000)) {
			address &= 0x07FF;
			RAM.getInstance().write(address, value);
		}

		// In the $2000 - $2007 I/O registers?
		if ((address >= 0x2000) && (address < 0x4000)) {
			// In case the access occours in the mirrored part of memory
			address &= 0x000F;
			if (address == 0x0000) // Really, it's 0x2000
				PpuRegisters.getInstance().control1(value);
			else if (address == 0x0001) // Really, it's 0x2001
				PpuRegisters.getInstance().control2(value);
			else if (address == 0x0003) // It's really 0x2003
				PpuRegisters.getInstance().spriteAddress(value);
			else if (address == 0x0004) // It's really 0x2004
				PpuRegisters.getInstance().writeSpriteData(value);
			else if (address == 0x0005) // It's really 0x2005
				PpuRegisters.getInstance().writeScroll(value);
			else if (address == 0x0006) // It's really 0x2006
				PpuRegisters.getInstance().writePpuMemAddress(value);
			else if (address == 0x0007) // It's really 0x2006
				PpuRegisters.getInstance().writePpuMem(value);

		}

		// In the $4000 - $4017 range?
		if ((address >= 0x4000) && (address < 0x4017)) {
			address &= 0x00FF;
			if (address == 0x0014) // I mean $4014
				PpuRegisters.getInstance().writeSpriteDma(value);
			if(address == 0x0016) // I mean $4016
				Controller.getInstance().writeController1(value);
				// This is APU related
//			if(address == 0x0017) // I mean $4017
//				Controller.getInstance().writeController2(value);
			
		}

		// In the PRG ROM area?
		else if ((address >= 0x8000) && (address < 0x10000)) {
			address &= 0x7FFF;
			PRGROM.getInstance().write(address, value);
		}
	}

	/*
	 * This method is used when the debugger reads memory. It doesn't want to
	 * trigger any special behaviour that normal reading from memory might do.
	 */
	public int readMemQuiet(int address) {
		address &= 0xFFFF;
		// First, check whether the read is in RAM
		if ((address >= 0) && (address < 0x2000)) {
			address &= 0x07FF;
			return RAM.getInstance().read(address);
		}

		// In the $2000 - $2007 I/O registers?
		if ((address >= 0x2000) && (address < 0x4000)) {
			address &= 0x000F;
			return 0; // PPU!
		}

		// In the PRG ROM area?
		else if ((address >= 0x8000) && (address < 0x10000)) {
			address &= 0x7FFF;
			return PRGROM.getInstance().read(address);
		} else
			DebugPane.getInstance().outputDebugMessage(
					"readQuiet ERROR! " + address);
		return 0;
	}

	/*
	 * This method writes to memory without any special triggers.
	 */
	public void writeMemQuiet(int address, int value) {
		address &= 0xFFFF;

		// First, check whether the read is in RAM
		if ((address >= 0) && (address < 0x2000)) {
			address &= 0x07FF;
			RAM.getInstance().write(address, value);
		}

		// In the $2000 - $2007 I/O registers?
		else if ((address >= 0x2000) && (address < 0x4000)) {
			address &= 0x000F;
			// PPU!
		}
		// In the PRG ROM area?
		else if ((address >= 0x8000) && (address < 0x10000)) {
			address &= 0x7FFF;
			PRGROM.getInstance().write(address, value);
		} else
			DebugPane.getInstance().outputDebugMessage(
					"writeQuiet ERROR! " + address);

	}

	/**
	 * 
	 * @return The reset vector
	 * 
	 */
	public int getResetVector() {
		int low = readMemQuiet(0xFFFC);
		int high = readMemQuiet(0xFFFD);
		return (high << 8) | low;
	}

	public String toString() {
		return "Emulated memory access";
	}
}
