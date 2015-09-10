package debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cpu.CpuRegisters;


public class CpuPane {

	private JPanel cpuPanel;
	private JPanel registersPanel;
	private JPanel flagsPanel;
	private static CpuPane instance;

	private JCheckBox nRegisterCheckBox;
	private JCheckBox vRegisterCheckBox;
	private JCheckBox dRegisterCheckBox;
	private JCheckBox bRegisterCheckBox;
	private JCheckBox iRegisterCheckBox;
	private JCheckBox zRegisterCheckBox;
	private JCheckBox cRegisterCheckBox;
	private JLabel valuePaneS;
	private JLabel cyclesPanel;

	private JTextField regAvalue;
	private JTextField regXvalue;
	private JTextField regYvalue;
	private JTextField regSP;
	private JTextField regPCvalue;

	public static CpuPane getInstance() {
		if (instance == null)
			instance = new CpuPane();
		return instance;
	}

	public class ToggleN implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (CpuRegisters.getInstance().isNegative())
				CpuRegisters.getInstance().setNegative(false);
			else
				CpuRegisters.getInstance().setNegative(true);
			valuePaneS.setText(String.format("%02X", CpuRegisters.getInstance()
					.getFlagByte()));
		}

	}

	public class ToggleV implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (CpuRegisters.getInstance().isOverflow())
				CpuRegisters.getInstance().setOverflow(false);
			else
				CpuRegisters.getInstance().setOverflow(true);
			valuePaneS.setText(String.format("%02X", CpuRegisters.getInstance()
					.getFlagByte()));
		}

	}

	public class ToggleD implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (CpuRegisters.getInstance().isDecimal())
				CpuRegisters.getInstance().setDecimal(false);
			else
				CpuRegisters.getInstance().setDecimal(true);
			valuePaneS.setText(String.format("%02X", CpuRegisters.getInstance()
					.getFlagByte()));
		}

	}

	public class ToggleB implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (CpuRegisters.getInstance().isBreakFlag())
				CpuRegisters.getInstance().setBreakFlag(false);
			else
				CpuRegisters.getInstance().setBreakFlag(true);
			valuePaneS.setText(String.format("%02X", CpuRegisters.getInstance()
					.getFlagByte()));
		}

	}

	public class ToggleI implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (CpuRegisters.getInstance().isInterrupt())
				CpuRegisters.getInstance().setInterrupt(false);
			else
				CpuRegisters.getInstance().setInterrupt(true);
			valuePaneS.setText(String.format("%02X", CpuRegisters.getInstance()
					.getFlagByte()));
		}

	}

	public class ToggleZ implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (CpuRegisters.getInstance().isZero())
				CpuRegisters.getInstance().setZero(false);
			else
				CpuRegisters.getInstance().setZero(true);
			valuePaneS.setText(String.format("%02X", CpuRegisters.getInstance()
					.getFlagByte()));
		}

	}

	public class ToggleC implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (CpuRegisters.getInstance().isCarry()) {
				CpuRegisters.getInstance().setCarry(false);
			} else {
				CpuRegisters.getInstance().setCarry(true);
			}
			valuePaneS.setText(String.format("%02X", CpuRegisters.getInstance()
					.getFlagByte()));

		}

	}

	public JPanel getPanel() {
		return cpuPanel;
	}

	private void setupFlagsPanel() {
		flagsPanel = new JPanel();
		flagsPanel.setLayout(new GridLayout(7, 1));
		flagsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Flags"));

		nRegisterCheckBox = new JCheckBox("N - negative");
		nRegisterCheckBox.addActionListener(new ToggleN());
		flagsPanel.add(nRegisterCheckBox);
		vRegisterCheckBox = new JCheckBox("V - overflow");
		vRegisterCheckBox.addActionListener(new ToggleV());
		flagsPanel.add(vRegisterCheckBox);
		dRegisterCheckBox = new JCheckBox("D - decimal");
		dRegisterCheckBox.addActionListener(new ToggleD());
		flagsPanel.add(dRegisterCheckBox);
		bRegisterCheckBox = new JCheckBox("B - break");
		bRegisterCheckBox.addActionListener(new ToggleB());
		flagsPanel.add(bRegisterCheckBox);
		iRegisterCheckBox = new JCheckBox("I - interrupt");
		iRegisterCheckBox.addActionListener(new ToggleI());
		flagsPanel.add(iRegisterCheckBox);
		zRegisterCheckBox = new JCheckBox("Z - zero");
		zRegisterCheckBox.addActionListener(new ToggleZ());
		flagsPanel.add(zRegisterCheckBox);
		cRegisterCheckBox = new JCheckBox("C - carry");
		cRegisterCheckBox.addActionListener(new ToggleC());
		flagsPanel.add(cRegisterCheckBox);

	}

	public class regAchanged implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String str = regAvalue.getText();
				int val = Integer.parseInt(str, 16);
				if (val >= 0x00 && val < 0x100) {
					CpuRegisters.getInstance().setRegA(val);
				} else {
					DebugPane.getInstance().outputDebugMessage("Enter a byte value in hex!\n");
					System.err.println("Enter a byte value in hex!");
				}
			} catch (NumberFormatException ex) {
				DebugPane.getInstance().outputDebugMessage("Enter a byte value in hex!\n");
				System.err.println("Input a byte value in hex!");
			}
		}

	}

	public class regXchanged implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String str = regXvalue.getText();
				int val = Integer.parseInt(str, 16);
				if (val >= 0x00 && val < 0x100) {
					CpuRegisters.getInstance().setRegX(val);
				} else {
					DebugPane.getInstance().outputDebugMessage("Enter a byte value in hex!\n");
					System.err.println("Enter a byte value in hex!");
				}
			} catch (NumberFormatException ex) {
				DebugPane.getInstance().outputDebugMessage("Enter a byte value in hex!\n");
				System.err.println("Input a byte value in hex!");
			}
		}

	}

	public class regYchanged implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String str = regYvalue.getText();
				int val =  Integer.parseInt(str, 16);
				if (val >= 0x00 && val < 0x100) {
					CpuRegisters.getInstance().setRegY(val);
				} else {
					DebugPane.getInstance().outputDebugMessage("Enter a byte value in hex!\n");
					System.err.println("Enter a byte value in hex!");
				}
			} catch (NumberFormatException ex) {
				DebugPane.getInstance().outputDebugMessage("Enter a byte value in hex!\n");
				System.err.println("Input a byte value in hex!");
			}
		}

	}

	public class regSPchanged implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String str = regSP.getText();
				int val =  Integer.parseInt(str, 16);
				if (val >= 0x00 && val < 0x100) {
					CpuRegisters.getInstance().setStack(val);
				} else {
					DebugPane.getInstance().outputDebugMessage("Enter a byte value in hex!\n");
					System.err.println("Enter a byte value in hex!");
				}
			} catch (NumberFormatException ex) {
				DebugPane.getInstance().outputDebugMessage("Enter a byte value in hex!\n");
				System.err.println("Input a byte value in hex!");
			}
			StackPane.getInstance().fireStateChanged();
		}

	}

	
	public class regPCchanged implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String str = regPCvalue.getText();
				int val =  Integer.parseInt(str, 16);
				if (val >= 0x00 && val < 0x10000) {
					CpuRegisters.getInstance().setPC(val);
				} else {
					DebugPane.getInstance().outputDebugMessage("Enter a two-byte value in hex!\n");
					System.err.println("Enter a two-byte value in hex!");
				}
				ExecutionPane.getInstance().fireStateChanged();

			} catch (NumberFormatException ex) {
				DebugPane.getInstance().outputDebugMessage("Enter a two-byte value in hex!\n");
				System.err.println("Input a two-byte value in hex!");
			}
		}

	}

	private void setupRegistersPanel() {

		registersPanel = new JPanel();
		registersPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		registersPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Registers"));

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0, 0, 5, 10);
		registersPanel.add(new JLabel("A: "), constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		registersPanel.add(new JLabel("0x"), constraints);

		regAvalue = new JTextField(2);
		regAvalue.addActionListener(new regAchanged());
		regAvalue.setText(String.format("%02X", CpuRegisters.getInstance()
				.getRegA()));
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		registersPanel.add(regAvalue, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0, 0, 5, 10);
		registersPanel.add(new JLabel("X: "), constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		registersPanel.add(new JLabel("0x"), constraints);

		regXvalue = new JTextField(2);
		regXvalue.addActionListener(new regXchanged());
		regXvalue.setText(String.format("%02X", CpuRegisters.getInstance()
				.getRegX()));
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		registersPanel.add(regXvalue, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0, 0, 5, 10);
		registersPanel.add(new JLabel("Y: "), constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		registersPanel.add(new JLabel("0x"), constraints);

		regYvalue = new JTextField(2);
		regYvalue.addActionListener(new regYchanged());
		regYvalue.setText(String.format("%02X", CpuRegisters.getInstance()
				.getRegY()));
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		registersPanel.add(regYvalue, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0, 0, 5, 10);
		registersPanel.add(new JLabel("SP: "), constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		registersPanel.add(new JLabel("0x"), constraints);

		regSP = new JTextField(2);
		regSP.addActionListener(new regSPchanged());
		regSP.setText(String.format("%02X", CpuRegisters.getInstance()
				.getStack()));
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		registersPanel.add(regSP, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0, 0, 5, 10);
		registersPanel.add(new JLabel("S: "), constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		registersPanel.add(new JLabel("0x"), constraints);

		valuePaneS = new JLabel(String.format("%02X", CpuRegisters.getInstance()
				.getFlagByte()));
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		registersPanel.add(valuePaneS, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0, 0, 5, 10);
		registersPanel.add(new JLabel("PC:"), constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		registersPanel.add(new JLabel("0x"), constraints);

		regPCvalue = new JTextField(4);
		regPCvalue.addActionListener(new regPCchanged());
		regPCvalue.setText(String.format("%04X", CpuRegisters.getInstance()
				.getPC()));
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		registersPanel.add(regPCvalue, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.insets = new Insets(0, 0, 5, 10);
		registersPanel.add(new JLabel("Clk:"), constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.WEST;
		cyclesPanel = new JLabel("00000000");
		registersPanel.add(cyclesPanel, constraints);

	}

	private CpuPane() {

		setupFlagsPanel();
		setupRegistersPanel();

		cpuPanel = new JPanel();
		cpuPanel.add(flagsPanel);
		cpuPanel.add(registersPanel);
		fireStateChanged();

	}

	public void fireStateChanged() {

		valuePaneS.setText(String.format("%02X", CpuRegisters.getInstance()
				.getFlagByte()));
		
		regAvalue.setText(String.format("%02X", CpuRegisters.getInstance().getRegA()));
		regXvalue.setText(String.format("%02X", CpuRegisters.getInstance().getRegX()));
		regYvalue.setText(String.format("%02X", CpuRegisters.getInstance().getRegY()));
		regSP.setText(String.format("%02X", CpuRegisters.getInstance().getStack()));
		regPCvalue.setText(String.format("%04X", CpuRegisters.getInstance().getPC()));
		cyclesPanel.setText(String.format("%08d", CpuRegisters.getInstance().getClockCycles()));
		
		if (CpuRegisters.getInstance().isNegative())
			nRegisterCheckBox.setSelected(true);
		else
			nRegisterCheckBox.setSelected(false);

		if (CpuRegisters.getInstance().isOverflow())
			vRegisterCheckBox.setSelected(true);
		else
			vRegisterCheckBox.setSelected(false);

		if (CpuRegisters.getInstance().isDecimal())
			dRegisterCheckBox.setSelected(true);
		else
			dRegisterCheckBox.setSelected(false);

		if (CpuRegisters.getInstance().isBreakFlag())
			bRegisterCheckBox.setSelected(true);
		else
			bRegisterCheckBox.setSelected(false);

		if (CpuRegisters.getInstance().isInterrupt())
			iRegisterCheckBox.setSelected(true);
		else
			iRegisterCheckBox.setSelected(false);

		if (CpuRegisters.getInstance().isZero())
			zRegisterCheckBox.setSelected(true);
		else
			zRegisterCheckBox.setSelected(false);

		if (CpuRegisters.getInstance().isCarry())
			cRegisterCheckBox.setSelected(true);
		else
			cRegisterCheckBox.setSelected(false);

	}

}
