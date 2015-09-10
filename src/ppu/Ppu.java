package ppu;

import javax.swing.JPanel;

import debugger.DebugPane;

/*
 * FIXME FIXEM FIXME! Here we go again, with the imported JPanel crap!
 */
public class Ppu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Ppu instance;

	int screenWidth;
	int screenHeight;
	int screenSize;
	int totalScreenWidth;
	int totalScreenHeight;
	int totalScreenSize;
	// This is the currently viewable screen, sent to output
	private int[] screenPixelArray;
	// This is the total screen, with all four Name Tables, a 512*480 pixel
	// frame buffer
	private int[] totalPixelArray;
	private int[] rgbArray = { 0x7C7C7C, 0x0000FC, 0x0000BC, 0x4428BC,
			0x940084, 0xA80020, 0xA81000, 0x881400, 0x503000, 0x007800,
			0x006800, 0x005800, 0x004058, 0x000000, 0x000000, 0x000000,
			0xBCBCBC, 0x0078F8, 0x0058F8, 0x6844FC, 0xD800CC, 0xE40058,
			0xF83800, 0xE45C10, 0xAC7C00, 0x00B800, 0x00A800, 0x00A844,
			0x008888, 0x000000, 0x000000, 0x000000, 0xF8F8F8, 0x3CBCFC,
			0x6888FC, 0x9878F8, 0xF878F8, 0xF85898, 0xF87858, 0xFCA044,
			0xF8B800, 0xB8F818, 0x58D854, 0x58F898, 0x00E8D8, 0x787878,
			0x000000, 0x000000, 0xFCFCFC, 0xA4E4FC, 0xB8B8F8, 0xD8B8F8,
			0xF8B8F8, 0xF8A4C0, 0xF0D0B0, 0xFCE0A8, 0xF8D878, 0xD8F878,
			0xB8F8B8, 0xB8F8D8, 0x00FCFC, 0xF8D8F8, 0x000000, 0x000000 };

	/*
	 * See it as:
	 * 00001111222233334444555566667777
	 * 88889999aaaabbbbccccddddeeeeffff
	 * and it might look a little clearer.
	 */
	private int[] attributeByteLoopkup = { 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2,
			3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 0, 0,
			0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5,
			6, 6, 6, 6, 7, 7, 7, 7, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3,
			3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 0, 0, 0, 0,
			1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6,
			6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 11, 11,
			11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15,
			15, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12,
			12, 12, 13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 8, 8, 8, 8,
			9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13,
			13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 8, 8, 8, 8, 9, 9, 9, 9, 10,
			10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14,
			14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17, 17, 18, 18, 18,
			18, 19, 19, 19, 19, 20, 20, 20, 20, 21, 21, 21, 21, 22, 22, 22, 22,
			23, 23, 23, 23, 16, 16, 16, 16, 17, 17, 17, 17, 18, 18, 18, 18, 19,
			19, 19, 19, 20, 20, 20, 20, 21, 21, 21, 21, 22, 22, 22, 22, 23, 23,
			23, 23, 16, 16, 16, 16, 17, 17, 17, 17, 18, 18, 18, 18, 19, 19, 19,
			19, 20, 20, 20, 20, 21, 21, 21, 21, 22, 22, 22, 22, 23, 23, 23, 23,
			16, 16, 16, 16, 17, 17, 17, 17, 18, 18, 18, 18, 19, 19, 19, 19, 20,
			20, 20, 20, 21, 21, 21, 21, 22, 22, 22, 22, 23, 23, 23, 23, 24, 24,
			24, 24, 25, 25, 25, 25, 26, 26, 26, 26, 27, 27, 27, 27, 28, 28, 28,
			28, 29, 29, 29, 29, 30, 30, 30, 30, 31, 31, 31, 31, 24, 24, 24, 24,
			25, 25, 25, 25, 26, 26, 26, 26, 27, 27, 27, 27, 28, 28, 28, 28, 29,
			29, 29, 29, 30, 30, 30, 30, 31, 31, 31, 31, 24, 24, 24, 24, 25, 25,
			25, 25, 26, 26, 26, 26, 27, 27, 27, 27, 28, 28, 28, 28, 29, 29, 29,
			29, 30, 30, 30, 30, 31, 31, 31, 31, 24, 24, 24, 24, 25, 25, 25, 25,
			26, 26, 26, 26, 27, 27, 27, 27, 28, 28, 28, 28, 29, 29, 29, 29, 30,
			30, 30, 30, 31, 31, 31, 31, 32, 32, 32, 32, 33, 33, 33, 33, 34, 34,
			34, 34, 35, 35, 35, 35, 36, 36, 36, 36, 37, 37, 37, 37, 38, 38, 38,
			38, 39, 39, 39, 39, 32, 32, 32, 32, 33, 33, 33, 33, 34, 34, 34, 34,
			35, 35, 35, 35, 36, 36, 36, 36, 37, 37, 37, 37, 38, 38, 38, 38, 39,
			39, 39, 39, 32, 32, 32, 32, 33, 33, 33, 33, 34, 34, 34, 34, 35, 35,
			35, 35, 36, 36, 36, 36, 37, 37, 37, 37, 38, 38, 38, 38, 39, 39, 39,
			39, 32, 32, 32, 32, 33, 33, 33, 33, 34, 34, 34, 34, 35, 35, 35, 35,
			36, 36, 36, 36, 37, 37, 37, 37, 38, 38, 38, 38, 39, 39, 39, 39, 40,
			40, 40, 40, 41, 41, 41, 41, 42, 42, 42, 42, 43, 43, 43, 43, 44, 44,
			44, 44, 45, 45, 45, 45, 46, 46, 46, 46, 47, 47, 47, 47, 40, 40, 40,
			40, 41, 41, 41, 41, 42, 42, 42, 42, 43, 43, 43, 43, 44, 44, 44, 44,
			45, 45, 45, 45, 46, 46, 46, 46, 47, 47, 47, 47, 40, 40, 40, 40, 41,
			41, 41, 41, 42, 42, 42, 42, 43, 43, 43, 43, 44, 44, 44, 44, 45, 45,
			45, 45, 46, 46, 46, 46, 47, 47, 47, 47, 40, 40, 40, 40, 41, 41, 41,
			41, 42, 42, 42, 42, 43, 43, 43, 43, 44, 44, 44, 44, 45, 45, 45, 45,
			46, 46, 46, 46, 47, 47, 47, 47, 48, 48, 48, 48, 49, 49, 49, 49, 50,
			50, 50, 50, 51, 51, 51, 51, 52, 52, 52, 52, 53, 53, 53, 53, 54, 54,
			54, 54, 55, 55, 55, 55, 48, 48, 48, 48, 49, 49, 49, 49, 50, 50, 50,
			50, 51, 51, 51, 51, 52, 52, 52, 52, 53, 53, 53, 53, 54, 54, 54, 54,
			55, 55, 55, 55, 48, 48, 48, 48, 49, 49, 49, 49, 50, 50, 50, 50, 51,
			51, 51, 51, 52, 52, 52, 52, 53, 53, 53, 53, 54, 54, 54, 54, 55, 55,
			55, 55, 48, 48, 48, 48, 49, 49, 49, 49, 50, 50, 50, 50, 51, 51, 51,
			51, 52, 52, 52, 52, 53, 53, 53, 53, 54, 54, 54, 54, 55, 55, 55, 55,
			56, 56, 56, 56, 57, 57, 57, 57, 58, 58, 58, 58, 59, 59, 59, 59, 60,
			60, 60, 60, 61, 61, 61, 61, 62, 62, 62, 62, 63, 63, 63, 63, 56, 56,
			56, 56, 57, 57, 57, 57, 58, 58, 58, 58, 59, 59, 59, 59, 60, 60, 60,
			60, 61, 61, 61, 61, 62, 62, 62, 62, 63, 63, 63, 63 };

	/*
	 * The screen is divided according to:
	 * 0 0 2 2
	 * 0 0 2 2
	 * 4 4 6 6
	 * 4 4 6 6
	 */
	private int[] attributeByteShiftLookup = { 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2,
			2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0,
			0, 2, 2, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6,
			6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4,
			4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6, 4, 4, 6, 6

	};

	public static Ppu getInstance() {
		if (instance == null)
			instance = new Ppu();
		return instance;
	}

	/**
	 * When calling update(), the pixel array is updated, and can be fetched
	 * through getPixelArray(). This is a linear frame buffer that can be used
	 * to output pixels on a screen.
	 */
	private Ppu() {
		screenWidth = 256;
		screenHeight = 240;
		screenSize = screenWidth * screenHeight;
		screenPixelArray = new int[screenSize];

		totalScreenWidth = screenWidth * 2;
		totalScreenHeight = screenHeight * 2;
		totalScreenSize = totalScreenWidth * totalScreenHeight;
		totalPixelArray = new int[totalScreenSize];
		update();

	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenSize() {
		return screenSize;
	}

	public int[] getPixelArray() {
		return screenPixelArray;
	}

	public void update() {
		// First, black out the screens
		for (int i = 0; i < screenSize; i++)
			screenPixelArray[i] = 0xFF000000;
		for (int i = 0; i < totalScreenSize; i++)
			totalPixelArray[i] = 0xFF000000;

		updateTotalBackground();
		updateBackground();
		updateSprites();
		updateScrollGrid();

	}

	/**
	 * Paints a black rectangle in the background screen to show the current
	 * viewable scroll
	 */
	private void updateScrollGrid() {
		int x = PpuRegisters.getInstance().getScrollX();
		int y = PpuRegisters.getInstance().getScrollY();

		// See where from where the scroll should start
		int scrollBase = PpuRegisters.getInstance().getScrollBaseAddress();
		switch (scrollBase) {
		case 0x2000:
			x += 0;
			y += 0;
			break;
		case 0x2400:
			x += screenWidth;
			y += 0;
			break;
		case 0x2800:
			x += 0;
			y += screenHeight;
			break;
		case 0x2C00:
			x += screenWidth;
			y += screenHeight;
		}

		// FIXME!! These lines ain't working perfectly...

		// Left vertical line
		for (int i = 0; i < screenHeight; i++) {
			int pixel = y * totalScreenWidth + i * totalScreenWidth + x;
			if (x + i >= totalScreenWidth)
				pixel -= totalScreenWidth;
			if (pixel >= totalScreenSize)
				pixel -= totalScreenSize;
			totalPixelArray[pixel] = 0xFF000000;

		}

		// Right vertical line
		for (int i = 0; i < screenHeight; i++) {
			int pixel = y * totalScreenWidth + i * totalScreenWidth + x
					+ screenWidth;
			if (x + i >= totalScreenWidth)
				pixel -= totalScreenWidth;
			if (pixel >= totalScreenSize)
				pixel -= totalScreenSize;
			totalPixelArray[pixel] = 0xFF000000;
		}

		// Upper horizontal line
		for (int i = 0; i < screenWidth; i++) {
			int pixel = y * totalScreenWidth + i + x;
			if (x + i >= totalScreenWidth)
				pixel -= totalScreenWidth;
			if (pixel >= totalScreenSize)
				pixel -= totalScreenSize;
			totalPixelArray[pixel] = 0xFF000000;
		}

		// Lower horizontal line
		for (int i = 0; i < screenWidth; i++) {
			int pixel = y * totalScreenWidth + totalScreenWidth * screenHeight
					+ x + i;
			if (x + i >= totalScreenWidth)
				pixel -= totalScreenWidth;
			if (pixel >= totalScreenSize)
				pixel -= totalScreenSize;
			totalPixelArray[pixel] = 0xFF000000;

		}
	}

	/*
	 * This copies a section of the total screen into the visible screen,
	 * according to the scroll registers
	 */
	private void updateBackground() {
		int scrollX = PpuRegisters.getInstance().getScrollX();
		int scrollY = PpuRegisters.getInstance().getScrollY();
		if (scrollY > 240) {
			scrollY -= 240;
		}

		// See from where the scroll should start
		int scrollBase = PpuRegisters.getInstance().getScrollBaseAddress();
		switch (scrollBase) {
		case 0x2000:
			scrollBase = 0;
			break;
		case 0x2400:
			scrollBase = screenWidth;
			break;
		case 0x2800:
			scrollBase = totalScreenWidth * screenHeight;
			break;
		case 0x2C00:
			scrollBase = totalScreenWidth * screenHeight + screenWidth;
		}

		int start = scrollBase + scrollY * totalScreenWidth + scrollX;

		for (int writeY = 0; writeY < screenHeight; writeY++) {

			for (int writeX = 0; writeX < screenWidth; writeX++) {
				int readX = writeX;
				int readY = writeY;

				/*
				 * If the read from the background screen wraps around the
				 * screen, make sure we read from the same row
				 */
				if ((start % totalScreenWidth + readX) >= totalScreenWidth) {
					readY--;
				}

				/*
				 * Wrap around the background screen
				 */
				if ((start + readY * totalScreenWidth + readX) >= totalScreenSize) {
					start -= totalScreenSize;
				}
				screenPixelArray[writeY * screenWidth + writeX] = totalPixelArray[start
						+ readY * totalScreenWidth + readX];
			}
		}

	}

	private void updateTotalBackground() {

		// The total number of tiles per Name Table
		final int tilesX = 64;
		final int tilesY = 60;
		final int totalTiles = tilesX * tilesY;
		final int totalPixelSize = tilesX * 8 * tilesY * 8;

		// The coordinates of the tile in the total grid
		int tileTotalCoordY;
		int tileTotalCoordX;
		// The coordinates of the tile within its Name Table
		int tileNameTableCoordY;
		int tileNameTableCoordX;
		int[] tileArray;
		int start;
		int nameTableIndex = 0;

		for (int tile = 0; tile < totalTiles; tile++) {

			// The coordinates of the tile in the total grid
			tileTotalCoordY = (tile / tilesX);
			tileTotalCoordX = (tile % tilesX);

			// Check which Name Table we're in, and set the coordinates of the
			// tile within this Name Table
			if ((tileTotalCoordX < 32) && (tileTotalCoordY < 30)) {
				nameTableIndex = 0x2000;
				// We're in Name Table 0, total grid and Name Table grid
				// coordinates are equal
				tileNameTableCoordX = tileTotalCoordX;
				tileNameTableCoordY = tileTotalCoordY;
			} else if ((tileTotalCoordX >= 32) && (tileTotalCoordY < 30)) {
				// We're in Name Table 1. X needs to be decreased to stay within
				// its Name Table
				nameTableIndex = 0x2400;
				tileNameTableCoordX = tileTotalCoordX - 32;
				tileNameTableCoordY = tileTotalCoordY;
			} else if ((tileTotalCoordX < 32) && (tileTotalCoordY >= 30)) {
				// We're in Name Table 2. Y needs to be decreased
				nameTableIndex = 0x2800;
				tileNameTableCoordX = tileTotalCoordX;
				tileNameTableCoordY = tileTotalCoordY - 30;
			} else {
				// We're in Name Table 3. Both coordinates need to be decreased
				nameTableIndex = 0x2C00;
				tileNameTableCoordX = tileTotalCoordX - 32;
				tileNameTableCoordY = tileTotalCoordY - 30;
			}

			/*
			 * Create the tile. createBackgroundTile() needs to know which
			 * tile in which Name Table to create
			 */
			tileArray = createBackgroundTile(tileNameTableCoordY * 32
					+ tileNameTableCoordX, nameTableIndex);

			// The pixel coordinates of where to start writing the tile
			start = (tileTotalCoordY * 64) * tilesX + (tileTotalCoordX * 8);

			// Put the returned tile at the position as stated by
			// (tileY * 8) and (tileX * 8)
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int color = tileArray[y * 8 + x];
					/*
					 * Only draw pixel if it's within the screen area
					 */
//					if (((start + y * tilesX * 8 + x) > totalPixelSize)
//							|| ((color & 0xff000000) != 0xff000000))
					if ((start + y * tilesX * 8 + x) > totalPixelSize)
						;
					else
						totalPixelArray[start + y * tilesX * 8 + x] = color;
				}
			}
		}
	}

	public int getTotalWidth() {
		return totalScreenWidth;
	}

	public int getTotalHeight() {
		return totalScreenHeight;
	}

	public int getTotalSize() {
		return totalScreenSize;
	}

	public int[] getTotalArray() {
		return totalPixelArray;
	}

	private void updateSprites() {

		// Create all 64 sprites
		int spriteY;
		int spriteX;
		int[] tileArray;
		int start;
		for (int sprite = 0; sprite < 0x40; sprite++) {
			spriteY = PpuRegisters.getInstance().readSpriteRamQuiet(
					sprite * 4 + 0);
			spriteX = PpuRegisters.getInstance().readSpriteRamQuiet(
					sprite * 4 + 3);
			tileArray = createSpriteTile(sprite);

			start = spriteY * screenWidth + spriteX;

			/*
			 * tileArray consists of ints that represent colors in rgbArray.
			 * Analyze every pixel, and if it has bit 8 set, it's supposed to go
			 * behind the background. Then only paint it if the pixel it would
			 * be painted over is of the background color (i.e. 0x00 in the
			 * palette)
			 */
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int sourcePosition = y * 8 + x;
					int destinationPosition = start + y * screenWidth + x;

					/*
					 * Only draw pixel if it's not transparent, and if it's
					 * within the screen area. FIXME! Should this really be
					 * neccessary?
					 */
					if ((destinationPosition + 8) > screenSize)
						;
					// If the pixel color is set to 0xFF, make the pixel
					// transparent, and don't care about the color.
					else if ((tileArray[sourcePosition] & 0xff) == 0xff) {
						tileArray[sourcePosition] = 0x00ffffff;
					}

					// Check whether the sprite is to be behind the background
					else if ((tileArray[sourcePosition] & 0x100) != 0x00) {

						int rgbIndex = tileArray[sourcePosition];
						int color = rgbArray[rgbIndex & 0xFF];
						int bgColor = (rgbArray[PpuRegisters.getInstance()
								.readPpuMemPpu(0x3F00)] | 0xFF000000);
						int currentBg = screenPixelArray[destinationPosition];
						/*
						 * Only paint pixel if the pixel we're writing over in
						 * the background is of the background color!
						 */
						if (currentBg == bgColor)
							screenPixelArray[destinationPosition] = 0xFF000000 | color;
					} else {
						// Sprites are written directly to the visible screen
						// Remove flags before looking up the RGB color
						int rgbIndex = tileArray[sourcePosition];
						int color = rgbArray[rgbIndex];
						screenPixelArray[destinationPosition] = 0xFF000000 | color;
					}
				}
			}
		}
	}

	private void updateSpritesOld() {

		// Create all 64 sprites
		int spriteY;
		int spriteX;
		int[] tileArray;
		int start;
		for (int sprite = 0; sprite < 0x40; sprite++) {
			spriteY = PpuRegisters.getInstance().readSpriteRamQuiet(
					sprite * 4 + 0);
			spriteX = PpuRegisters.getInstance().readSpriteRamQuiet(
					sprite * 4 + 3);
			tileArray = createSpriteTile(sprite);

			/*
			 * tileArray consist of a color in the palette, convert these colors
			 * to RGB values. This is done by using rgbArray[]. If the pixel
			 * color
			 * is set to 0xFF, make the pixel transparent, and don't care about
			 * the
			 * color.
			 */
			for (int i = 0; i < 0x40; i++) {
				if (tileArray[i] == 0xff)
					tileArray[i] = 0x00ffffff;
				else
					// Make sure the first bits 24-31 are set, to make the pixel
					// opaque.
					tileArray[i] = (0xff000000 | rgbArray[tileArray[i]]);
			}

			start = spriteY * screenWidth + spriteX;
			// Put tile the returned tile at the position as stated by spriteX
			// and spriteY
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int color = tileArray[y * 8 + x];
					/*
					 * Only draw pixel if it's not transparent, and if it's
					 * within the screen area. Should this really be neccessary?
					 */
					if (((start + y * screenWidth + x + 8) > screenSize)
							|| ((color & 0xff000000) != 0xff000000))
						;
					// Sprites are written directly to the visible screen
					else
						screenPixelArray[start + y * screenWidth + x] = color;
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 *         An int[] that represents an 8x8 framebuffer consisting of a tile.
	 */
	private int[] createSpriteTile(int sprite) {

		// There are 64 pixels in one tile
		int pixelsPerTile = 0x40;

		int[] pixelBytes = new int[pixelsPerTile];

		int tileIndex = PpuRegisters.getInstance().readSpriteRamQuiet(
				sprite * 4 + 1);
		int attribute = PpuRegisters.getInstance().readSpriteRamQuiet(
				sprite * 4 + 2);
		int patternTableoffset = PpuRegisters.getInstance()
				.getSpritePatternTableAddress();

		/*
		 * First, get bit 0 from the Pattern Table entry.
		 * Which Pattern Table is determined by tableOffset.
		 */
		int tilePart;
		for (int i = 0; i < 0x8; i++) {
			tilePart = PpuRegisters.getInstance().readPpuMemQuiet(
					patternTableoffset + (tileIndex * 0x10) + i);

			pixelBytes[8 * i + 0] = ((tilePart >> 7) & (0x01));
			pixelBytes[8 * i + 1] = ((tilePart >> 6) & (0x01));
			pixelBytes[8 * i + 2] = ((tilePart >> 5) & (0x01));
			pixelBytes[8 * i + 3] = ((tilePart >> 4) & (0x01));
			pixelBytes[8 * i + 4] = ((tilePart >> 3) & (0x01));
			pixelBytes[8 * i + 5] = ((tilePart >> 2) & (0x01));
			pixelBytes[8 * i + 6] = ((tilePart >> 1) & (0x01));
			pixelBytes[8 * i + 7] = ((tilePart) & (0x01));
		}

		/*
		 * Get bit 1 from the Pattern Table entry.
		 */
		for (int i = 0; i < 0x8; i++) {
			tilePart = PpuRegisters.getInstance().readPpuMemQuiet(
					patternTableoffset + (tileIndex * 0x10) + i + 8);
			pixelBytes[8 * i + 0] |= ((tilePart >> 6) & (0x02));
			pixelBytes[8 * i + 1] |= ((tilePart >> 5) & (0x02));
			pixelBytes[8 * i + 2] |= ((tilePart >> 4) & (0x02));
			pixelBytes[8 * i + 3] |= ((tilePart >> 3) & (0x02));
			pixelBytes[8 * i + 4] |= ((tilePart >> 2) & (0x02));
			pixelBytes[8 * i + 5] |= ((tilePart >> 1) & (0x02));
			pixelBytes[8 * i + 6] |= ((tilePart) & (0x02));
			pixelBytes[8 * i + 7] |= ((tilePart << 1) & (0x02));
		}

		/*
		 * Now, get bit 2 and bit 3 from the attribute byte
		 */
		for (int i = 0; i < 0x40; i++) {
			pixelBytes[i] |= ((attribute & 0x03) << 2);
		}

		/*
		 * Check for horizontal flipping!
		 */
		if ((attribute & 0x40) != 0) {
			int[] mirrorBytes = new int[pixelsPerTile];
			// Turn it around...
			for (int col = 0; col < 8; col++)
				for (int row = 0; row < 8; row++)
					mirrorBytes[col * 8 + 7 - row] = pixelBytes[col * 8 + row];
			// And put it back!
			for (int i = 0; i < pixelsPerTile; i++)
				pixelBytes[i] = mirrorBytes[i];

		}

		/*
		 * Check for vertical flipping!
		 */
		if ((attribute & 0x80) != 0) {
			int[] mirrorBytes = new int[pixelsPerTile];
			// Twist and turn...
			for (int col = 0; col < 8; col++)
				for (int row = 0; row < 8; row++)
					mirrorBytes[(7 - col) * 8 + row] = pixelBytes[col * 8 + row];
			// And put it back.
			for (int i = 0; i < pixelsPerTile; i++)
				pixelBytes[i] = mirrorBytes[i];

		}

		/*
		 * From here, each byte in the array contains an index that points to a
		 * color in the palette.
		 */
		for (int i = 0; i < 0x40; i++) {
			/*
			 * If the bit 0 and bit 1 are both clear, it means the pixel is not
			 * visible, and no color should be fetched from the palette. We
			 * indicate this by setting the pixel byte to 0xFF.
			 */
			if ((pixelBytes[i] & 0x03) == 0)
				pixelBytes[i] = 0xFF;
			else
			/*
			 * Get the color from the palette. The address of the sprite palette
			 * in PPU memory is 0x3F10
			 */
			{
				pixelBytes[i] = PpuRegisters.getInstance().readPpuMemPpu(
						0x3F10 + pixelBytes[i]);
			}

		}

		/*
		 * Check Background priority.
		 */
		if ((attribute & 0x20) != 0) {
			/*
			 * This is one hell of an ugly solution. But it works for now. If
			 * the tile is supposed to be behind the background, indicate this
			 * with 0x100.
			 */
			for (int i = 0; i < 0x40; i++)
				pixelBytes[i] |= 0x100;

		}

		// Now, the pixelBytes array consists of a 8x8 pixel area represented
		// by a linear int array
		return pixelBytes;

	}

	/**
	 * 
	 * @param tile
	 *            - Which tile
	 * @param nameTableIndex
	 *            - in which Name table to create
	 * @return
	 *         An int[] that represents an 8x8 framebuffer consisting of a tile.
	 */
	private int[] createBackgroundTile(int tile, int nameTableIndex) {

		// There are 64 pixels in one tile
		int[] pixelBytes = new int[0x40];
		// Which Pattern Table to use for backgrounds
		int patternTableIndex = PpuRegisters.getInstance()
				.getBackgroundPatternTableAddress();

		// This is the tile number in the Pattern Table
		int tileIndex = PpuRegisters.getInstance().readPpuMemQuiet(
				nameTableIndex + tile);

		/*
		 * First, get bit 0 from the Pattern Table entry.
		 * Which Pattern Table is determined by tableOffset.
		 */
		int tilePart;
		for (int i = 0; i < 0x8; i++) {
			tilePart = PpuRegisters.getInstance().readPpuMemQuiet(
					patternTableIndex + (tileIndex * 0x10) + i);

			pixelBytes[8 * i + 0] = ((tilePart >> 7) & (0x01));
			pixelBytes[8 * i + 1] = ((tilePart >> 6) & (0x01));
			pixelBytes[8 * i + 2] = ((tilePart >> 5) & (0x01));
			pixelBytes[8 * i + 3] = ((tilePart >> 4) & (0x01));
			pixelBytes[8 * i + 4] = ((tilePart >> 3) & (0x01));
			pixelBytes[8 * i + 5] = ((tilePart >> 2) & (0x01));
			pixelBytes[8 * i + 6] = ((tilePart >> 1) & (0x01));
			pixelBytes[8 * i + 7] = ((tilePart) & (0x01));
		}

		/*
		 * Get bit 1 from the Pattern Table entry.
		 */
		for (int i = 0; i < 0x8; i++) {
			tilePart = PpuRegisters.getInstance().readPpuMemQuiet(
					patternTableIndex + (tileIndex * 0x10) + i + 8);

			pixelBytes[8 * i + 0] |= ((tilePart >> 6) & (0x02));
			pixelBytes[8 * i + 1] |= ((tilePart >> 5) & (0x02));
			pixelBytes[8 * i + 2] |= ((tilePart >> 4) & (0x02));
			pixelBytes[8 * i + 3] |= ((tilePart >> 3) & (0x02));
			pixelBytes[8 * i + 4] |= ((tilePart >> 2) & (0x02));
			pixelBytes[8 * i + 5] |= ((tilePart >> 1) & (0x02));
			pixelBytes[8 * i + 6] |= ((tilePart) & (0x02));
			pixelBytes[8 * i + 7] |= ((tilePart << 1) & (0x02));
		}
		/*
		 * Now, get the attribute byte from the Attribute Table
		 * 
		 * The screen is divided into 4x4 tile (that is 32x32 pixel) squares
		 * Each byte in the Attribute Table determines bit 2 and bit 3 of the
		 * color of the tiles within each square.
		 * 
		 * So, first, get the attribute byte from the Attribute Table
		 * In order to make it easy (and fast) for us, we use a lookup table
		 */

		int attributeByteIndex = attributeByteLoopkup[tile];
		// The Attribute Table is 960 bytes after the start of the Name Table
		int attributeByteAddress = nameTableIndex + 960 + attributeByteIndex;
		int attributeByte = PpuRegisters.getInstance().readPpuMemQuiet(
				attributeByteAddress);

		/*
		 * Now check how many bits to shift. Again, use a lookup table
		 */
		int attributeShift = attributeByteShiftLookup[tile];
		attributeByte = ((attributeByte >> attributeShift) & (0x03));

		/*
		 * Shift in these two bits to bit 2 and bit 3 of pixelBytes[]
		 */
		for (int i = 0; i < 0x40; i++) {
			pixelBytes[i] |= ((attributeByte & 0x03) << 2);
		}

		/*
		 * From here, each byte in the array contains an index that points to a
		 * color in the palette.
		 */
		for (int i = 0; i < 0x40; i++) {
			/*
			 * Get the color from the palette. The address of the background
			 * palette in PPU memory is 0x3F00
			 */
			{
				pixelBytes[i] = PpuRegisters.getInstance().readPpuMemPpu(
						0x3F00 + pixelBytes[i]);
			}

		}

		/*
		 * Now the bytes consist of a color in the palette, convert these colors
		 * to RGB values. This is done using yet another lookup table,
		 * rgbArray[].
		 */
		for (int i = 0; i < 0x40; i++) {
			// Make sure the first bits 24-31 are set, to make the pixel
			// opaque.
			pixelBytes[i] = (0xff000000 | rgbArray[pixelBytes[i]]);
		}

		// Now, the pixelBytes array consists of a 8x8 pixel area represented
		// by a linear int array
		return pixelBytes;

	}

}
