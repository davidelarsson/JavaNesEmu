package other;

import ppu.Ppu;
import ppu.PpuRegisters;
import cpu.Cpu;
import cpu.CpuRegisters;
import debugger.ExecutionPane;
import debugger.GraphicsPane;

public class Emulator implements Runnable {
	static Emulator instance;
	private long cyclesLeftToExecute;
	private int cyclesPerFrame;
	private int cyclesPerVblank;

	private long oldTime;
	private long newTime;
	private long sleepTime;

	public static Emulator getInstance() {
		if (instance == null)
			instance = new Emulator();
		return instance;
	}

	public Emulator() {
		cyclesPerFrame = 29829;
		cyclesPerVblank = 2387;
		cyclesLeftToExecute = cyclesPerFrame;
		oldTime = 0;
		newTime = 0;
		sleepTime = 0;

	}

	public void run() {

		while (true) {
			oldTime = System.currentTimeMillis();

			// FIXME! This breaks the abstraction layer!
			if (ExecutionPane.getInstance().stopButtonPressed) {
//				System.out.println("BREAK!");
				break;

			}

			// Do 1/60th of a second's worth of emulation
			step();
			GraphicsPane.getInstance().updateImage();

			newTime = System.currentTimeMillis();
			sleepTime = oldTime - newTime + 16;

			try {
				if (sleepTime < 0)
					;
				else {
					Thread.sleep(sleepTime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Do one step of emulation. Right now it executes 1/60 second's worth of
	 * emulation
	 */
	public void step() {

		long totalCycles = CpuRegisters.getInstance().getClockCycles();

		cyclesLeftToExecute = cyclesPerFrame + totalCycles;

		// First, do a number of clock cycles
		while ((totalCycles + cyclesPerVblank) < cyclesLeftToExecute) {
			Cpu.getInstance().executeInstruction();
			totalCycles = CpuRegisters.getInstance().getClockCycles();
		}

		// We are now in a VBlank
		PpuRegisters.getInstance().setVblank(true);

		// If we are to execute NMI on VBlank, do so
		if (PpuRegisters.getInstance().isNMIOnVblank())
			Cpu.getInstance().executeNMI();

		// Do the rest of the cycles
		while (totalCycles < cyclesLeftToExecute) {
			Cpu.getInstance().executeInstruction();
			totalCycles = CpuRegisters.getInstance().getClockCycles();
		}

		// And we're out of the VBlank
		PpuRegisters.getInstance().setVblank(true);

		// Time to update the PPU
		Ppu.getInstance().update();

	}
}
