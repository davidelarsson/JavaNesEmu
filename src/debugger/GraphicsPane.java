package debugger;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.MemoryImageSource;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cpu.Cpu;

import ppu.Ppu;
import ppu.PpuRegisters;

/**
 * FIXME FIXME FIXME!!! Why, oh why, do I need to extend JPanel() only to be
 * able to use CreateImage() ?!?
 * Oh, and remove the stupid serial number stuff when the JPanel() inheritance
 * is no longer needed
 * 
 */
public class GraphicsPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private static GraphicsPane instance;
	private JPanel graphicsPanel;
	private JPanel buttonsPanel;
	private JPanel outputPanel;
	private Image outputImage;
	private Image totalImage;

	JCheckBox vblankCheckbox;

	public static GraphicsPane getInstance() {
		if (instance == null)
			instance = new GraphicsPane();
		return instance;
	}

	public JPanel getPanel() {
		return graphicsPanel;
	}

	/*
	 * This JPanel contains in itself two other JPanels:
	 * To the NORTH, there's a buttonsPanel, that contains the buttons
	 * controlling the output window.
	 * 
	 * To the SOUTH, there's a GraphicsWindow(), which is the actual output of
	 * the PPU.
	 */
	private GraphicsPane() {
		graphicsPanel = new JPanel();

		graphicsPanel.setLayout(new BorderLayout());
		setupButtonsPanel();
		graphicsPanel.add(buttonsPanel, BorderLayout.NORTH);
		outputPanel = new GraphicsWindow();
		graphicsPanel.add(outputPanel, BorderLayout.CENTER);
		updateImage();
	}

	private void setupButtonsPanel() {
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Control"));

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new updateButtonListener());
		buttonsPanel.add(updateButton);

		JButton nmiButton = new JButton("Do NMI");
		nmiButton.addActionListener(new ExecuteNMI());
		buttonsPanel.add(nmiButton);

		vblankCheckbox = new JCheckBox("VBlank");
		vblankCheckbox.addActionListener(new VBlankToggle());
		buttonsPanel.add(vblankCheckbox);
	}

	private class ExecuteNMI implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Cpu.getInstance().executeNMI();
			CpuPane.getInstance().fireStateChanged();
			StackPane.getInstance().fireStateChanged();
			MemoryPane.getInstance().fireStateChanged();
			ExecutionPane.getInstance().fireStateChanged();
		}

	}

	private class VBlankToggle implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (vblankCheckbox.isSelected())
				PpuRegisters.getInstance().setVblank(true);
			else
				PpuRegisters.getInstance().setVblank(false);
		}

	}

	/**
	 * Updates the PPU window
	 */
	private class updateButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			updateImage();
		}

	}
	
	public void fireStateChanged() {
		//FIXME!! Should also update the state of the buttons!
		updateImage();
	}

	public void updateImage() {

		// Fetch the visible screen
		int screenWidth = Ppu.getInstance().getScreenWidth();
		int screenHeight = Ppu.getInstance().getScreenHeight();
		int pixelArray[] = Ppu.getInstance().getPixelArray();

		outputImage = createImage(new MemoryImageSource(screenWidth,
				screenHeight, pixelArray, 0, screenWidth));
		
		// Also fetch the total screen
		int totalWidth = Ppu.getInstance().getTotalWidth();
		int totalHeight = Ppu.getInstance().getTotalHeight();
		int totalArray[] = Ppu.getInstance().getTotalArray();

		totalImage = createImage(new MemoryImageSource(totalWidth, totalHeight,
				totalArray, 0, totalWidth));
		outputPanel.repaint();
	}

	/*
	 * We use a JPanel whose paintComponent() we override to do our painting.
	 */
	private class GraphicsWindow extends JPanel {

		private static final long serialVersionUID = 1L;

		public GraphicsWindow() {
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// Move out the pained image a little from the upper-left corner
			g.drawImage(outputImage, 8, 8, this);
			g.drawImage(totalImage, 8, 250, this);

		}

	}
}
