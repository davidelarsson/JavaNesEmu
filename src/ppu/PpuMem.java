package ppu;

import other.NesFile;

public class PpuMem {

	private int[] ppuRam;
	private int[] chrRom;
	private int[] palette;

	private static PpuMem instance;
	int oldPpuAddress;

	public static PpuMem getInstance() {
		if (instance == null)
			instance = new PpuMem();
		return instance;
	}

	private PpuMem() {

		// This is the 2 KB of PPU RAM plus mirroring
		ppuRam = new int[0x1000];
		// The CHR ROM is 8 KB
		chrRom = new int[0x2000];
		// The palette is 32 bytes. Included mirrored addresses
		palette = new int[0x20];

		oldPpuAddress = 0x0000;
	}

	/**
	 * 
	 * @param address
	 *            - Must be between 0x00 and 0xFF
	 * @return an 6-bit value. What value depends on mirroring
	 * 
	 *         0x00 - Background color (color 0)
	 *         0x01 - 0x03 - Background palette 0, colors 1 - 3
	 *         0x04 - General purpose 6-bit data register
	 *         0x05 - 0x07 - Background palette 1, colors 1 - 3
	 *         0x08 - General purpose 6-bit data register
	 *         0x09 - 0x0B - Background Palette 2, colors 1 - 3
	 *         0x0C - General purpose 6-bit data register
	 *         0x0D - 0x0F - Background Palette 3, colors 1 - 3
	 * 
	 *         0x10 - Mirror 0x00
	 *         0x11 - 0x13 - Sprite palette 0, colors 1 - 3
	 *         0x14 - Mirror of 0x04
	 *         0x15 - 0x17 - Sprite palette 1, colors 1 - 3
	 *         0x18 - Mirror of 0x08
	 *         0x19 - 0x1B - Sprite palette 2, colors 1 - 3
	 *         0x1C - Mirror of 0x0C
	 *         0x1D - 0x1F - Sprite palette 3, colors 1 - 3
	 * 
	 *         0x20 - 0xFF - Mirrors of 0x00 - 0x1F
	 */
	private int readPalette(int address) {

		// 0x3F20 - 0x3FFF is mirrored as 0x3F00 - 0x3F1F
		address &= 0x001F;
		int value = 0x00;

		// If both bit 0 and bit 1 are not clear, we're safe to deliver a value
		// from the palette
		if ((address & 0x03) != 0x00)
			value = palette[address];
		// Actually, 0x00 is also a valid address
		else if (address == 0x00)
			value = palette[0x00];
		// 0x04 is a general purpose register
		else if (address == 0x04)
			value = palette[0x04];
		// 0x08 is a general purpose register
		else if (address == 0x08)
			value = palette[0x08];
		// 0x0C is a general purpose register
		else if (address == 0x0C)
			value = palette[0x0C];
		// 0x10 is a mirror of 0x00
		else if (address == 0x10)
			value = palette[0x00];
		// 0x14 is a mirror of 0x04
		else if (address == 0x14)
			value = palette[0x04];
		// 0x18 is a mirror of 0x08
		else if (address == 0x18)
			value = palette[0x08];
		// 0x1C is a mirror of 0x0C
		else if (address == 0x1C)
			value = palette[0x0C];

		// Only return a 6-bit value
		return (value & 0x3F);

	}

	/**
	 * 
	 * @param address
	 *            - Must be between 0x00 and 0xFF
	 * @return an 6-bit value. What value depends on mirroring
	 * 
	 *         This method is used when the PPU is accessing the palette. The
	 *         reason that a different call exists, is that the mirroring works
	 *         a little different as opposed to when the CPU is accessing PPU
	 *         RAM.
	 *         0x00 - Background color (color 0)
	 *         0x01 - 0x03 - Background palette 0, colors 1 - 3
	 *         0x04 - Mirror of 0x00
	 *         0x05 - 0x07 - Background palette 1, colors 1 - 3
	 *         0x08 - Mirror of 0x00
	 *         0x09 - 0x0B - Background Palette 2, colors 1 - 3
	 *         0x0C - Mirror of 0x00
	 *         0x0D - 0x0F - Background Palette 3, colors 1 - 3
	 * 
	 *         0x10 - Mirror 0x00
	 *         0x11 - 0x13 - Sprite palette 0, colors 1 - 3
	 *         0x14 - Mirror of 0x00
	 *         0x15 - 0x17 - Sprite palette 1, colors 1 - 3
	 *         0x18 - Mirror of 0x00
	 *         0x19 - 0x1B - Sprite palette 2, colors 1 - 3
	 *         0x1C - Mirror of 0x00
	 *         0x1D - 0x1F - Sprite palette 3, colors 1 - 3
	 * 
	 *         0x20 - 0xFF - Mirrors of 0x00 - 0x1F
	 */
	private int readPalettePpu(int address) {

		// 0x3F20 - 0x3FFF is mirrored as 0x3F00 - 0x3F1F
		address &= 0x001F;
		int value = 0x00;

		// If both bit 0 and bit 1 are not clear, we're safe to deliver a value
		// from the palette
		if ((address & 0x03) != 0x00)
			value = palette[address];
		// If not, we should deliver the value from 0x00, whatever the values of
		// the other bits
		else
			value = palette[0x00];

		// Only return a 6-bit value
		return (value & 0x3F);

	}

	/**
	 * 
	 * @param value
	 *            - a 6-bit value (bit 6 and bit 7 are masked out)
	 * @param address
	 *            - Must be between 0x00 and 0xFF
	 * 
	 *            0x00 - Background color (color 0)
	 *            0x01 - 0x03 - Background palette 0, colors 1 - 3
	 *            0x04 - General purpose 6-bit data register
	 *            0x05 - 0x07 - Background palette 1, colors 1 - 3
	 *            0x08 - General purpose 6-bit data register
	 *            0x09 - 0x0B - Background Palette 2, colors 1 - 3
	 *            0x0C - General purpose 6-bit data register
	 *            0x0D - 0x0F - Background Palette 3, colors 1 - 3
	 * 
	 *            0x10 - Mirror 0x00
	 *            0x11 - 0x13 - Sprite palette 0, colors 1 - 3
	 *            0x14 - Mirror of 0x04
	 *            0x15 - 0x17 - Sprite palette 1, colors 1 - 3
	 *            0x18 - Mirror of 0x08
	 *            0x19 - 0x1B - Sprite palette 2, colors 1 - 3
	 *            0x1C - Mirror of 0x0C
	 *            0x1D - 0x1F - Sprite palette 3, colors 1 - 3
	 * 
	 *            0x20 - 0xFF - Mirrors of 0x00 - 0x1F
	 */
	private void writePalette(int address, int value) {

		// Bit 6 and bit 7 are out. We can only save a 6-bit value in the
		// palette
		value &= 0x3F;

		// 0x3F20 - 0x3FFF is mirrored as 0x3F00 - 0x3F1F
		address &= 0x001F;

		// If both bit 0 and bit 1 are not clear, we're safe to deliver a value
		// from the palette
		if ((address & 0x03) != 0x00)
			palette[address & 0x3F] = value;
		// Actually, 0x00 is also a valid address
		else if (address == 0x00)
			palette[0x00] = value;
		// 0x04 is a general purpose register
		else if (address == 0x04)
			palette[0x04] = value;
		// 0x08 is a general purpose register
		else if (address == 0x08)
			palette[0x08] = value;
		// 0x0C is a general purpose register
		else if (address == 0x0C)
			palette[0x0C] = value;
		// 0x10 is a mirror of 0x00
		else if (address == 0x10)
			palette[0x00] = value;
		// 0x14 is a mirror of 0x04
		else if (address == 0x14)
			palette[0x04] = value;
		// 0x18 is a mirror of 0x08
		else if (address == 0x18)
			palette[0x08] = value;
		// 0x1C is a mirror of 0x0C
		else if (address == 0x1C)
			palette[0x0C] = value;

	}

	/*
	 * Horizontal mirroring: 0x2000 == 0x2400 and 0x2800 == 0x2C00
	 * I.e. bit 10 is cleared.
	 * 
	 * Vertical mirroring: 0x2000 == 0x2800 and 0x2400 == 0x2C00
	 * I.e. bit 9 is cleared.
	 */

	private int readRam(int address) {
		if (NesFile.getInstance().getMirroring() == NesFile.HORIZONTAL)
			address &= 0xFBFF;
		else
			address &= 0xF7FF;
		return ppuRam[address];
	}

	private void writeRam(int address, int value) {
		if (NesFile.getInstance().getMirroring() == NesFile.HORIZONTAL)
			address &= 0xFBFF;
		else
			address &= 0xF7FF;
		ppuRam[address] = value;
	}

	private int readRamQuiet(int address) {
		if (NesFile.getInstance().getMirroring() == NesFile.HORIZONTAL)
			address &= 0xFBFF;
		else
			address &= 0xF7FF;
		return ppuRam[address];
	}

	private int readChrRom(int address) {
		return chrRom[address];
	}

	private void writeChrRom(int address, int value) {
		chrRom[address] = value;
	}

	private void writeRamQuiet(int address, int value) {
		if (NesFile.getInstance().getMirroring() == NesFile.HORIZONTAL)
			address &= 0xFBFF;
		else
			address &= 0xF7FF;
		ppuRam[address] = value;
	}

	/**
	 * 
	 * @param address
	 *            - Must be less than 0x4000
	 * @return - a byte in memory is always between 0x00 and 0xFF
	 */
	public int readPpuMem(int address) {

		// If we try to read from a PPU memory location less than 0x3FE0, we use
		// the old value in the latch!
		if (address < 0x3FE0) {
			int savedAddress = oldPpuAddress;
			oldPpuAddress = address;
			address = savedAddress;
		}

		// Within the CHR ROM area?
		if (address < 0x2000)
			return readChrRom(address);
		// Within the Attribute / Name Table RAM? (0x2000-0x2FFF)
		else if (address < 0x3000)
			return readRam((address - 0x3000) & 0x0FFF);
		// The palette is being accessed!
		else
			return readPalette(address - 0x3F00);
	}

	/**
	 * For screen output
	 * 
	 * @param address
	 *            - Must be less than 0x4000
	 * @return - a byte in memory is always between 0x00 and 0xFF
	 */
	public int readPpuMemQuiet(int address) {
		// Within the CHR ROM area?
		if (address < 0x2000)
			return chrRom[address];
		// Within the Attribute / Name Table RAM? (0x2000-0x2FFF)
		else if (address < 0x3000)
			return readRamQuiet(address - 0x2000);
		// The palette is being accessed!
		else
			return readPalette(address - 0x3F00);
	}

	/**
	 * 
	 * @param address
	 *            - position in PPU memory
	 * @return a value from PPU memory
	 * 
	 *         The reason this method exists is that the palette mirroring works
	 *         a little differently when the PPU is accessing PPU RAM, as
	 *         opposed to when the CPU is accessing PPU RAM.
	 */
	public int readPpuMemPpu(int address) {
		// Within the CHR ROM area?
		if (address < 0x2000)
			return chrRom[address];
		// Within the Attribute / Name Table RAM? (0x2000-0x2FFF)
		else if (address < 0x3000)
			return readRamQuiet(address - 0x2000);
		// The palette is being accessed!
		else
			return readPalettePpu(address - 0x3F00);
	}

	/**
	 * 
	 * @param address
	 *            - Must be less than 0x4000
	 * @param value
	 *            - Must be between 0x00 and 0xFF
	 */
	public void writePpuMem(int address, int value) {
		// Within the CHR ROM area?
		if (address < 0x2000)
			writeChrRom(address, value);
		// Within the Attribute / Name Table RAM? (0x2000-0x2FFF)
		else if (address < 0x3000)
			writeRam(((address - 0x3000) & 0x0FFF), value);
		// The palette is being accessed!
		else
			writePalette(address, value);
	}

	/**
	 * It's the same as writePpuMem()
	 * 
	 * @param address
	 *            - Must be less than 0x4000
	 * @param value
	 *            - Must be between 0x00 and 0xFF
	 */
	public void writePpuMemQuiet(int address, int value) {
		// Within the CHR ROM area?
		if (address < 0x2000)
			chrRom[address] = value;
		// Within the Attribute / Name Table RAM? (0x2000-0x2FFF)
		else if (address < 0x3000)
			writeRamQuiet(((address - 0x3000) & 0x0FFF), value);
		// The palette is being accessed!
		else
			writePalette(address, value);
	}
}
