package debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import other.Controller;

public class ControllerPane {
	private static ControllerPane instance;
	private JPanel controllerPanel;

	public static ControllerPane getInstance() {
		if (instance == null)
			instance = new ControllerPane();
		return instance;
	}

	public JPanel getPanel() {
		return controllerPanel;
	}

	private ControllerPane() {
		controllerPanel = new JPanel();
		controllerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JButton selectButton = new JButton("select");
		selectButton.addMouseListener(new SelectButtonListener());
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		controllerPanel.add(selectButton, c);

		JButton startButton = new JButton("start");
		startButton.addMouseListener(new StartButtonListener());
		c.gridx = 2;
		controllerPanel.add(startButton, c);

		JButton upButton = new JButton("up");
		upButton.addMouseListener(new UpButtonListener());
		c.gridy = 1;
		c.gridx = 1;
		controllerPanel.add(upButton, c);

		JButton leftButton = new JButton("left");
		leftButton.addMouseListener(new LeftButtonListener());
		c.gridx = 0;
		c.gridy = 2;
		controllerPanel.add(leftButton, c);

		JButton rightButton = new JButton("right");
		rightButton.addMouseListener(new RightButtonListener());
		c.gridx = 2;
		c.gridy = 2;
		controllerPanel.add(rightButton, c);

		JButton downButton = new JButton("down");
		downButton.addMouseListener(new DownButtonListener());
		c.gridx = 1;
		c.gridy = 3;
		controllerPanel.add(downButton, c);

		JButton aButton = new JButton("a");
		aButton.addMouseListener(new AButtonListener());
		c.gridx = 0;
		c.gridy = 4;
		controllerPanel.add(aButton, c);

		JButton bButton = new JButton("b");
		bButton.addMouseListener(new BButtonListener());
		c.gridx = 2;
		c.gridy = 4;
		controllerPanel.add(bButton, c);

	}

	/*
	 * Select is pressed
	 */
	private class SelectButtonListener extends ButtonListener {
		public void mousePressed(MouseEvent arg0) {
			Controller.getInstance().setSelectPressed(true);
		}

		public void mouseReleased(MouseEvent arg0) {
			Controller.getInstance().setSelectPressed(false);
		}

	}

	/*
	 * Start is pressed
	 */
	private class StartButtonListener extends ButtonListener {
		public void mousePressed(MouseEvent arg0) {
			Controller.getInstance().setStartPressed(true);
		}

		public void mouseReleased(MouseEvent arg0) {
			Controller.getInstance().setStartPressed(false);
		}

	}

	/*
	 * Up is pressed
	 */
	private class UpButtonListener extends ButtonListener {
		public void mousePressed(MouseEvent arg0) {
			Controller.getInstance().setUpPressed(true);
		}

		public void mouseReleased(MouseEvent arg0) {
			Controller.getInstance().setUpPressed(false);
		}

	}

	/*
	 * Left is pressed
	 */
	private class LeftButtonListener extends ButtonListener {
		public void mousePressed(MouseEvent arg0) {
			Controller.getInstance().setLeftPressed(true);
		}

		public void mouseReleased(MouseEvent arg0) {
			Controller.getInstance().setLeftPressed(false);
		}

	}

	/*
	 * Right is pressed
	 */
	private class RightButtonListener extends ButtonListener {
		public void mousePressed(MouseEvent arg0) {
			Controller.getInstance().setRightPressed(true);
		}

		public void mouseReleased(MouseEvent arg0) {
			Controller.getInstance().setRightPressed(false);
		}

	}

	/*
	 * Down is pressed
	 */
	private class DownButtonListener extends ButtonListener {
		public void mousePressed(MouseEvent arg0) {
			Controller.getInstance().setDownPressed(true);
		}

		public void mouseReleased(MouseEvent arg0) {
			Controller.getInstance().setDownPressed(false);
		}

	}

	/*
	 * A is pressed
	 */
	private class AButtonListener extends ButtonListener {
		public void mousePressed(MouseEvent arg0) {
			Controller.getInstance().setaPressed(true);
		}

		public void mouseReleased(MouseEvent arg0) {
			Controller.getInstance().setaPressed(false);
		}

	}

	/*
	 * B is pressed
	 */
	private class BButtonListener extends ButtonListener {
		public void mousePressed(MouseEvent arg0) {
			Controller.getInstance().setbPressed(true);
		}

		public void mouseReleased(MouseEvent arg0) {
			Controller.getInstance().setbPressed(false);
		}

	}

	/**
	 * Just a dummy class from which all ButtonListener in this class inherit
	 */
	private class ButtonListener implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
		}

	}
}
