package other;

import debugger.DebugPane;

public class Controller {
	private static Controller instance;
	private int readTimes;

	public static Controller getInstance() {
		if (instance == null)
			instance = new Controller();
		return instance;
	}

	private boolean selectPressed;
	private boolean startPressed;
	private boolean upPressed;
	private boolean downPressed;
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean aPressed;
	private boolean bPressed;

	public boolean isSelectPressed() {
		return selectPressed;
	}

	public void setSelectPressed(boolean selectPressed) {
		if (selectPressed)
			DebugPane.getInstance().outputDebugMessage("select pressed");
		else
			DebugPane.getInstance().outputDebugMessage("select released");

		this.selectPressed = selectPressed;
	}

	public boolean isStartPressed() {
		return startPressed;
	}

	public void setStartPressed(boolean startPressed) {
		if (startPressed)
			DebugPane.getInstance().outputDebugMessage("start pressed");
		else
			DebugPane.getInstance().outputDebugMessage("start released");
		this.startPressed = startPressed;
	}

	public boolean isUpPressed() {
		return upPressed;
	}

	public void setUpPressed(boolean upPressed) {
		if (upPressed)
			DebugPane.getInstance().outputDebugMessage("up pressed");
		else
			DebugPane.getInstance().outputDebugMessage("up released");
		this.upPressed = upPressed;
	}

	public boolean isDownPressed() {
		return downPressed;
	}

	public void setDownPressed(boolean downPressed) {
		if (downPressed)
			DebugPane.getInstance().outputDebugMessage("down pressed");
		else
			DebugPane.getInstance().outputDebugMessage("down released");
		this.downPressed = downPressed;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public void setLeftPressed(boolean leftPressed) {
		if (leftPressed)
			DebugPane.getInstance().outputDebugMessage("left pressed");
		else
			DebugPane.getInstance().outputDebugMessage("left released");
		this.leftPressed = leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public void setRightPressed(boolean rightPressed) {
		if (rightPressed)
			DebugPane.getInstance().outputDebugMessage("right pressed");
		else
			DebugPane.getInstance().outputDebugMessage("right released");
		this.rightPressed = rightPressed;
	}

	public boolean isaPressed() {
		return aPressed;
	}

	public void setaPressed(boolean aPressed) {
		if (aPressed)
			DebugPane.getInstance().outputDebugMessage("a pressed");
		else
			DebugPane.getInstance().outputDebugMessage("a released");
		this.aPressed = aPressed;
	}

	public boolean isbPressed() {
		return bPressed;
	}

	public void setbPressed(boolean bPressed) {
		if (bPressed)
			DebugPane.getInstance().outputDebugMessage("b pressed");
		else
			DebugPane.getInstance().outputDebugMessage("b released");
		this.bPressed = bPressed;
	}

	/**
	 * 
	 * @return
	 *         A value depending on the number of reads from the port and the
	 *         buttons currently pressed on the first controller
	 * 
	 *         Bit 0 is according to the following:
	 *         read Button
	 *         1st a
	 *         2nd b
	 *         3rd select
	 *         4th start
	 *         5th up
	 *         6th down
	 *         7th left
	 *         8th right
	 */
	public int readController1() {
		int returnByte = 0;

		switch (readTimes) {
		case 0:
			if (aPressed)
				returnByte |= 0x01;
			break;
		case 1:
			if (bPressed)
				returnByte |= 0x01;
			break;
		case 2:
			if (selectPressed)
				returnByte |= 0x01;
			break;
		case 3:
			if (startPressed)
				returnByte |= 0x01;
			break;
		case 4:
			if (upPressed)
				returnByte |= 0x01;
			break;
		case 5:
			if (downPressed)
				returnByte |= 0x01;
			break;
		case 6:
			if (leftPressed)
				returnByte |= 0x01;
			break;
		case 7:
			if (rightPressed)
				returnByte |= 0x01;
			readTimes = 0;
		}

		if (readTimes < 7)
			readTimes += 1;
		else
			readTimes = 0;

		return returnByte;
	}

	/**
	 * 
	 * @return
	 */
	public int readController2() {
		return 0;
	}

	public void writeController1(int value) {
		readTimes = 0;
	}

}
