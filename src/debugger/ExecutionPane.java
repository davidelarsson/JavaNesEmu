package debugger;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputListener;
import other.Emulator;
import memory.CpuMem;
import assembler.Disassembler;
import cpu.Cpu;
import cpu.CpuRegisters;

public class ExecutionPane {
	private static ExecutionPane instance;
	private JPanel executionPanel;
	private JPanel breakpointsPanel;
	private JPanel buttonPanel;
	private JPanel disasmPanel;
	private JList breakpointsList;
	private JCheckBox breakpointsEnabledCheckBox;
	private JTextArea disasmTextArea;
	public boolean stopButtonPressed;

	JButton startButton;
	JButton stopButton;
	JButton stepButton;
	
	public static ExecutionPane getInstance() {
		if (instance == null)
			instance = new ExecutionPane();
		return instance;
	}

	public JPanel getPanel() {
		return executionPanel;
	}

	public boolean isBreakpoint(int address) {
		int[] breakpoints = getBreakpoints();
		for (int b : breakpoints) {
			if (address == b)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @return A list of breakpoints
	 */
	public int[] getBreakpoints() {

		int size = breakpointsList.getModel().getSize();

		int[] breakpoints = new int[size];

		for (int i = 0; i < size; i++) {
			String str = (String) breakpointsList.getModel().getElementAt(i);
			str = str.substring(2, 6);
			breakpoints[i] = Integer.parseInt(str, 16);
		}
		return breakpoints;
	}

	private class AddBreakpointListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {
			int address;
			String addressString;
			String label;

			addressString = JOptionPane.showInternalInputDialog(executionPanel,
					"Address in hex?");

			try {
				if (addressString.startsWith("0x"))
					addressString = addressString.substring(2);
				address = Integer.parseInt(addressString, 16);
			} catch (Exception e) {
				DebugPane.getInstance().outputDebugMessage("Not a hex value!");
				return;
			}

			label = JOptionPane
					.showInternalInputDialog(executionPanel, "Name?");

			String listLabel = String.format("0x%04X (%s)", address, label);
			((DefaultListModel) breakpointsList.getModel())
					.addElement(listLabel);
		}

	}

	private class RemoveBreakpointListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {
			if (breakpointsList.getSelectedIndex() == -1) {
				DebugPane.getInstance()
						.outputDebugMessage("Nothing to remove!");
				return;
			}
			((DefaultListModel) breakpointsList.getModel())
					.remove(breakpointsList.getSelectedIndex());
		}

	}

	private class StartButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			DebugPane.getInstance().outputDebugMessage("Running...");
			Emulator emu = new Emulator();
			Thread t = new Thread(emu);
			t.start();
			stepButton.setEnabled(false);
			startButton.setEnabled(false);
			
		}
	}

	private class StopButtonListener implements MouseInputListener {

		public void mouseClicked(MouseEvent arg0) {
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
			stopButtonPressed = true;
		}

		public void mouseReleased(MouseEvent arg0) {
			stepButton.setEnabled(true);
			startButton.setEnabled(true);
			stopButtonPressed = false;
			CpuPane.getInstance().fireStateChanged();
			StackPane.getInstance().fireStateChanged();
			MemoryPane.getInstance().fireStateChanged();
			fireStateChanged();
		}

		public void mouseDragged(MouseEvent arg0) {
		}

		public void mouseMoved(MouseEvent arg0) {

		}
	}

	private class StepButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			DebugPane.getInstance().outputDebugMessage(
					"Executing one instruction");
			Cpu.getInstance().executeInstruction();
/*
			DebugPane.getInstance().outputDebugMessage("Stepping one... uh.. step.");
			Emulator.getInstance().step();
			*/
			CpuPane.getInstance().fireStateChanged();
			MemoryPane.getInstance().fireStateChanged();
			StackPane.getInstance().fireStateChanged();
			GraphicsPane.getInstance().updateImage();
			fireStateChanged();
		}

	}

	private class BreakpointsEnabledListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			DebugPane
					.getInstance()
					.outputDebugMessage(
							"Breakpoints are: "
									+ (breakpointsEnabledCheckBox.isSelected() ? "enabled"
											: "disabled"));
		}

	}

	void setupBreakpointsPanel() {
		/*
		 * breakpointsPanel contains a split pane, with a list to the left and
		 * two buttons to the right.
		 */
		JPanel leftPanel = new JPanel();

		DefaultListModel model = new DefaultListModel();
		breakpointsList = new JList(model);
		breakpointsList.setVisibleRowCount(6);

		model.addElement("0xFFFE (NMI)");

		JScrollPane listScrollPane = new JScrollPane(breakpointsList);
		listScrollPane.setPreferredSize(new Dimension(210, 80));
		leftPanel.add(listScrollPane);
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new AddBreakpointListener());
		rightPanel.add(addButton);
		rightPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new RemoveBreakpointListener());
		rightPanel.add(removeButton);

		breakpointsEnabledCheckBox = new JCheckBox("Enabled");
		breakpointsEnabledCheckBox
				.addActionListener(new BreakpointsEnabledListener());
		rightPanel.add(breakpointsEnabledCheckBox);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftPanel, rightPanel);
		splitPane.resetToPreferredSizes();
		breakpointsPanel = new JPanel();
		breakpointsPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Breakpoints"));
		breakpointsPanel.add(splitPane);
	}

	void setupButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel
				.setBorder(new TitledBorder(new EtchedBorder(), "Execution"));
		startButton = new JButton("Start");
		startButton.addActionListener(new StartButtonListener());
		buttonPanel.add(startButton);
		
		stopButton = new JButton("Stop");
		stopButton.addMouseListener(new StopButtonListener());
		buttonPanel.add(stopButton);
		
		stepButton = new JButton("Step");
		stepButton.addActionListener(new StepButtonListener());
		buttonPanel.add(stepButton);
	}

	void setupDisasmPanel() {
		disasmPanel = new JPanel();
		disasmPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Disassembler"));
		disasmTextArea = new JTextArea(8, 40);
		Font font = new Font("Monospaced", Font.PLAIN, 14);
		disasmTextArea.setFont(font);
		disasmTextArea.append("");
		disasmPanel.add(disasmTextArea);

	}

	public void fireStateChanged() {
		// First, clear out the disasmTextArea
		disasmTextArea.setText("");
		int pc = CpuRegisters.getInstance().getPC();
		for (int rows = 0; rows < disasmTextArea.getRows(); rows++) {
			// Set up the 3-byte array that disasm() needs
			int[] bytes = new int[3];
			for (int i = 0; i < 3; i++) {
				if (pc + i < 0x10000)
					bytes[i] = CpuMem.getInstance().readMemQuiet(pc + i);
				else
					bytes[i] = 0x00;
			}

			/*
			 * The Object array that disasm() returns is as follows:
			 * 
			 * Two Object objects, the first is an Integer, that indicates the
			 * length of the instruction in bytes.
			 * 
			 * The second Object is a String that is the disassembled
			 * instruction.
			 */
			Disassembler disasm = new Disassembler(pc);
			Object[] array = disasm.disasm(bytes);
			int instructionLength = (Integer) array[0]; // Length of instruction
			String disasmString = String.format("0x%04X:  ", pc);
			for (int i = 0; i < 3; i++) {
				if (i < instructionLength)
					if (pc + i < 0x10000)
						disasmString += String.format("%02X ", CpuMem
								.getInstance().readMemQuiet(pc + i));
					else
						bytes[i] = 0x00;
				else
					disasmString += "   ";
			}
			pc += instructionLength;
			disasmString += (String) array[1]; // Disasm'ed instruction
			disasmTextArea.append(disasmString + "\n");
		}
	}

	private ExecutionPane() {
		executionPanel = new JPanel();
		executionPanel.setLayout(new BorderLayout());
		setupBreakpointsPanel();
		setupButtonPanel();
		setupDisasmPanel();
		fireStateChanged();

		executionPanel.add(breakpointsPanel, BorderLayout.NORTH);
		executionPanel.add(buttonPanel, BorderLayout.CENTER);
		executionPanel.add(disasmPanel, BorderLayout.SOUTH);
	}
}
