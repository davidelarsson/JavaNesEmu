package ppu;

import cpu.CpuRegisters;
import memory.CpuMem;

public class PpuRegisters {
	private static PpuRegisters instance;
	// I didn't bother to make a separate class for SPR RAM, it's fairly simple
	private int spriteRam[];

	// Indicates whether NMI is executed on each VBlank
	private boolean NMIOnVblank;

	// Really, this is never used, even on a real NES
	private enum PpuType {
		MASTER, SLAVE
	};

	private PpuType ppuType;

	// SMALL = 8x8, LARGE = 16x8
	private enum SpriteSize {
		SMALL, LARGE
	};

	private SpriteSize spriteSize;

	private int backgroundPatternTableAddress;
	private int spritePatternTableAddress;
	/**
	 * Indicates how much the ppuRamAddress should be incremented after each
	 * access
	 * It's either 1 or 32, according to bit 2 of control1 ($2000)
	 */
	private int ppuRamAddressIncrement;

	private int scrollBaseAddress;
	private boolean spritesVisible;
	private boolean backgroundVisible;
	private int spriteAddress;
	private int scrollX;
	private int scrollY;
	private int ppuRamAddress;

	/*
	 * Two registers, $2005 (ppuBackroundScrollingOffset()) and $2006
	 * (ppuRamAddress()) need to be accessed (written to) twice in order to form
	 * a 16-bit (or actually 14-bit) address in PPU memory. The memory of
	 * whether the current access is the first or second is shared between these
	 * two ports.
	 */
	private boolean firstAccess;

	// This indicates whether we are in a vblank
	private boolean vblank;

	public static PpuRegisters getInstance() {
		if (instance == null)
			instance = new PpuRegisters();
		return instance;
	}

	/**
	 * These are the methods and corresponding registers of this Class:
	 * 
	 * $2000 - control1()
	 * $2001 - control2()
	 * $2002 - status()
	 * $2003 - spriteAddress()
	 * $2004 - readSpriteData()
	 * $2004 - writeSpriteData()
	 * $2005 - writeScroll()
	 * $2006 - ppuRamAddress()
	 * $2007 - readPpuRam()
	 * $2007 - writePpuRam()
	 * $4014 - SpriteDma()
	 */
	private PpuRegisters() {
		spriteRam = new int[0x100]; // SPR RAM is only 256 bytes
		NMIOnVblank = false;
		ppuType = PpuType.MASTER;
		vblank = false;
		backgroundPatternTableAddress = 0x0000;
		spritePatternTableAddress = 0x0000;
		ppuRamAddressIncrement = 0x01;
		scrollBaseAddress = 0x2000;
		spritesVisible = false;
		backgroundVisible = false;
		spriteAddress = 0x00;
		firstAccess = true;
		ppuRamAddress = 0x0000;
	}

	/**
	 * Controls the PPU according to the following bit values:
	 * bit 7 - Execute NMI on VBlank
	 * bit 6 - PPU Master / Slave (0 = master, 1 = slave) Not used.
	 * bit 5 - Sprite size (0 = 8x8, 1 16x8)
	 * bit 4 - Background Pattern Table Address in PPU RAM (0 = 0x0000, 1 =
	 * 0x1000)
	 * bit 3 - Sprite Pattern Table Address in PPU RAM(0 = 0x0000, 1 = 0x1000)
	 * bit 2 - Increment port 0x2007 by (0) = 1 byte, (1) = 16 bytes
	 * bit 1,0 - Name Table Scroll Base address:
	 * 00 - 0x2000
	 * 01 - 0x2400
	 * 10 - 0x2800
	 * 11 - 0x2C00
	 * (That is, bit 0, scroll horizontally by 256; bit 1, scroll vertically by
	 * 240)
	 */
	public void control1(int arg) {

		if ((arg & 0x80) != 0x00)
			NMIOnVblank = true;
		else
			NMIOnVblank = false;

		if ((arg & 0x40) != 0x00)
			ppuType = PpuType.SLAVE;
		else
			ppuType = PpuType.MASTER;

		if ((arg & 0x20) != 0x00) {
			spriteSize = SpriteSize.LARGE;
			// FIXME!!! This is juuuust debug code, and shan't be used!
			System.err.println("Uh-oh! Large sprites!");
		}

		else
			spriteSize = SpriteSize.SMALL;

		if ((arg & 0x10) != 0x00)
			backgroundPatternTableAddress = 0x1000;
		else
			backgroundPatternTableAddress = 0x0000;

		if ((arg & 0x08) != 0x00)
			spritePatternTableAddress = 0x1000;
		else
			spritePatternTableAddress = 0x0000;

		if ((arg & 0x04) != 0x00)
			ppuRamAddressIncrement = 0x20;
		else
			ppuRamAddressIncrement = 1;

		// Now check bit 0 and bit 1
		arg &= 0x03;

		switch (arg) {
		case 0:
			scrollBaseAddress = 0x2000;
			break;
		case 1:
			scrollBaseAddress = 0x2400;
			break;
		case 2:
			scrollBaseAddress = 0x2800;
			break;
		case 3:
			scrollBaseAddress = 0x2C00;
			break;

		}

	}

	/**
	 * Controls the PPU according to the following bit values:
	 * bit 7-5: Color Emphasis
	 * bit 4: Sprite visibility (0 = not displayed, 1 = displayed)
	 * bit 3: Background visibility (0 = not displayed, 1 = displayed)
	 * bit 2: Sprite Clipping
	 * bit 1: Background Clipping
	 * bit 0: Monochrome Mode
	 */
	public void control2(int arg) {
		if ((arg & 0x10) != 0x00)
			spritesVisible = true;
		else
			spritesVisible = false;

		if ((arg & 0x08) != 0x00)
			backgroundVisible = true;
		else
			backgroundVisible = false;

	}

	/**
	 * Returns a status byte, with the following information:
	 * bit 7: VBlank Flag (0 = no VBlank, 1 = we are in VBlank)
	 * bit 6: Sprite 0 hit
	 * bit 5: Lost Sprites
	 * bit 4 - 0: Not used.
	 * 
	 * We also make sure to set firstAccess to true (see above for an
	 * explaination)
	 */
	public int status() {

		firstAccess = true;

		int returnByte = 0x00;
		if (vblank)
			returnByte |= 0x80;
		else
			returnByte &= 0x7F;
		return returnByte;
	}

	/**
	 * Set the address in SPR RAM that is used when using SPR RAM Data register
	 * ($2004) or SPR RAM DMA ($4014)
	 * 
	 */
	public void spriteAddress(int address) {
		address &= 0x00FF;
		spriteAddress = address;

	}

	/**
	 * This method assigns the byte pointed at by spriteAddress in Sprite RAM.
	 * The pointer spriteAddres is incremented for each write
	 */
	public void writeSpriteData(int value) {
		spriteRam[spriteAddress] = value;
		spriteAddress += 0x01;
		// Make sure the address does not point outside of Sprite memory
		spriteAddress &= 0x00FF;
	}

	/**
	 * This reads the byte pointed at by spriteAddress in Sprite RAM.
	 * The pointer spriteAddress is _not_ incremented by reads.
	 * 
	 * @param value
	 */
	public int readSpriteData() {
		return spriteRam[spriteAddress];
	}

	public boolean isNMIOnVblank() {
		return NMIOnVblank;
	}

	/**
	 * This register keeps track of the scrolling offset. First write is X
	 * offset, second write is Y offset
	 */
	public void writeScroll(int address) {
		if (firstAccess) {
			firstAccess = false;
			// Make sure that the address is never larger than 0xFF
			address &= 0xFF;
			scrollX = address;
		} else {
			firstAccess = true;
			address &= 0xFF;
			scrollY = address;
		}

	}

	public int getScrollX() {
		return scrollX;
	}

	public int getScrollY() {
		return scrollY;
	}

	/**
	 * Does a 256-byte DMA transfer from CPU RAM to SPR RAM.
	 * It starts in SPR RAM at the byte pointed at by spriteAddress, which
	 * should usually be set to zero before the start of the transfer.
	 * 
	 * The 8-bit address written to this register represents the high byte of
	 * the start of the 256-byte block in CPU RAM. The lower byte is always
	 * zero.
	 * 
	 * This operation takes 512 CPU clock cycles.
	 */
	public void writeSpriteDma(int address) {
		int value;
		for (int i = 0; i < 0x100; i++) {
			value = CpuMem.getInstance().readMem(address * 0x100 + i);
			// Make sure we don't try to write anything outside of SPR RAM!
			spriteRam[(spriteAddress + i) & 0x00ff] = value;
		}
		// Add 512 clock cycles
		CpuRegisters.getInstance().addClockCycles(0x200);
	}

	/**
	 * For debugging and the emulated screen
	 */
	public int readSpriteRamQuiet(int position) {
		return spriteRam[position];
	}

	/**
	 * First write to this register is the MSB, second is LSB.
	 */
	public void writePpuMemAddress(int address) {
		if (firstAccess) {
			firstAccess = false;
			// Make sure that the address is never larger than 0x3FFF
			address &= 0x3F;
			ppuRamAddress = (address << 8);
		} else {
			firstAccess = true;
			ppuRamAddress |= address;
		}
	}

	/**
	 * Reads a byte from PPU RAM
	 * ppuRamAddress is incremented after each read.
	 */
	public int readPpuMem() {
		int returnVal = PpuMem.getInstance().readPpuMem(ppuRamAddress);
		ppuRamAddress += ppuRamAddressIncrement;
		// Make sure that the address is never larger than 0x3FFF
		ppuRamAddress &= 0x3FFF;
		return returnVal;
	}

	/**
	 * Writes a byte from PPU RAM
	 * ppuRamAddress is incremented after each write.
	 */
	public void writePpuMem(int value) {
		PpuMem.getInstance().writePpuMem(ppuRamAddress, value);
		ppuRamAddress += ppuRamAddressIncrement;
		// Make sure that the address is never larger than 0x3FFF
		ppuRamAddress &= 0x3FFF;
	}

	/**
	 * For debugging and the emulated screen
	 */
	public int readPpuMemQuiet(int position) {
		return PpuMem.getInstance().readPpuMemQuiet(position);
	}

	/**
	 * This call should only be used by the PPU. The reason it needs a special
	 * call is that the mirroring in the palette works differently when the PPU
	 * is reading from PPU RAM, as opposed to when the CPU is reading
	 */
	public int readPpuMemPpu(int position) {
		return PpuMem.getInstance().readPpuMemPpu(position);
	}

	/**
	 * For debugging purposes, file loading and bank switching
	 */
	public void writePpuMemQuiet(int position, int value) {
		// Make sure that the address is never larger than 0x3FFF
		position &= 0x3FFF;
		PpuMem.getInstance().writePpuMemQuiet(position, value);
	}

	/*
	 * Return the address to the start of the Sprite Pattern Table
	 */
	public int getSpritePatternTableAddress() {
		return spritePatternTableAddress;
	}

	/*
	 * Return the address to the start of the Background Pattern Table
	 */
	public int getBackgroundPatternTableAddress() {
		return backgroundPatternTableAddress;
	}

	/*
	 * Return the address to the start of the Background Pattern Table
	 */
	public int getScrollBaseAddress() {
		return scrollBaseAddress;
	}

	/*
	 * Tells whether we are in a VBlank
	 */
	public boolean isVblank() {
		return vblank;
	}

	/*
	 * Toggles wheter we are in a VBlank
	 */
	public void setVblank(boolean vblank) {
		/*
		 * If we enter VBlank, set spriteAddress to zero, and
		 * clear the first/second access
		 */
		if (vblank == true) {
			spriteAddress = 0x00;
			firstAccess = true;
		}

		this.vblank = vblank;
	}

}
