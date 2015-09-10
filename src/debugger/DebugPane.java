package debugger;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import ppu.PpuRegisters;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DebugPane {
	private static DebugPane instance;
	private JPanel debugPanel;
	private JPanel buttonPanel;
	private JTextArea debugArea;

	public static DebugPane getInstance() {
		if (instance == null)
			instance = new DebugPane();
		return instance;
	}

	public JPanel getPanel() {
		return debugPanel;
	}

	private void setupButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setBorder(new TitledBorder(new EtchedBorder(), "Buttons"));

		JButton debugButton = new JButton("De!");
		debugButton.addActionListener(new DebugButtonListener());
		buttonPanel.add(debugButton);

		JButton addButton = new JButton("Ad");
		addButton.addActionListener(new AddButtonListener());
		buttonPanel.add(addButton);

		JButton saveButton = new JButton("Sa");
		saveButton.addActionListener(new SaveButtonListener());
		buttonPanel.add(saveButton);
		/*
		 * FIXMEE!! THIS IS TO BE INCLUDED!!
		 * JButton clearButton = new JButton("CLR");
		 * clearButton.addActionListener(new ClearButtonListener());
		 * buttonPanel.add(clearButton);
		 */
		JButton sprButton = new JButton("SPR");
		sprButton.addActionListener(new SprButtonListener());
		buttonPanel.add(sprButton);

		JButton ppuButton = new JButton("PPU");
		ppuButton.addActionListener(new PpuButtonListener());
		buttonPanel.add(ppuButton);

	}

	private class ClearButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			debugArea.setText("");
		}

	}

	private class SaveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			String debugFileName = "debug";

			try {

				int index = 0;
				File file = new File(debugFileName + index);
				/*
				 * FIXME!! Does this open a whole bunch of files?!
				 */
				while (true) {
					file = new File(debugFileName + index + ".out");
					if (file.exists())
						index++;
					else
						break;
				}

				Formatter debugFile;
				debugFile = new Formatter(debugFileName + index + ".out");

				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy.MM.dd HH:mm:ss");
				Date now = new Date();
				String todayString = formatter.format(now);

				debugFile.format("Output saved ");
				// Insert a date stamp in the beginning of the log
				debugFile.format(todayString + "\n");
				debugFile.format(debugArea.getText());
				debugFile.close();
				outputDebugMessage("File saved as debug" + index + ".out");
			} catch (FileNotFoundException ex) {
				outputDebugMessage("Can't open file: " + debugFileName);
			}

		}

	}

	private class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = JOptionPane.showInternalInputDialog(debugPanel,
					"Enter message to add");
			outputDebugMessage(msg);
		}

	}

	private class DebugButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String str;
			str = "scrollX: " + PpuRegisters.getInstance().getScrollX();
			str += "scrollY: " + PpuRegisters.getInstance().getScrollY();
			
			outputDebugMessage(str);
		}

	}

	private class PpuButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String offsetString = JOptionPane.showInternalInputDialog(
					debugPanel, "Enter offset in hex");
			int offset = 0;
			try {
				offset = Integer.parseInt(offsetString, 0x10);
			} catch (Exception ex) {
				outputDebugMessage("Not a valid offset");
			}
			String str = "\nOffset: " + String.format("0x%2X", offset) + "\n";
			for (int i = 0; i < 0x100; i++) {
				if ((i % 0x10 == 0) && (i != 0x00))
					str += "\n";
				if ((offset + i) >= 0x4000)
					str += "0x-1 ";
				else
					str += String.format("0x%02X ", PpuRegisters.getInstance()
							.readPpuMemQuiet(offset + i));
			}
			outputDebugMessage(str);

		}

	}

	private class SprButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String str = "\nSPR RAM:\n";
			for (int i = 0; i < 0x100; i++) {
				if ((i % 0x10 == 0) && (i != 0x00))
					str += "\n";
				else
					str += String.format("0x%02X ", PpuRegisters.getInstance()
							.readSpriteRamQuiet(i));
			}
			outputDebugMessage(str);
		}

	}

	public void outputDebugMessage(String str) {
		debugArea.append(str);
		if (!str.endsWith("\n"))
			debugArea.append("\n");
	}

	private DebugPane() {
		setupButtonPanel();
		debugPanel = new JPanel();
		debugPanel.setLayout(new BorderLayout());

		debugArea = new JTextArea(10, 20);
		debugArea.setEditable(false);

		JScrollPane scrollpane = new JScrollPane(debugArea);
		scrollpane.setBorder(new TitledBorder(new EtchedBorder(), "Output"));
		scrollpane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		debugPanel.add(buttonPanel, BorderLayout.NORTH);
		debugPanel.add(scrollpane, BorderLayout.CENTER);

	}
}
