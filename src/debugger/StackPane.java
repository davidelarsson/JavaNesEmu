package debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import memory.CpuMem;

import cpu.CpuRegisters;


public class StackPane {
	private static StackPane instance;
	private JPanel stackFrame;

	private JTable addressTable;
	private JTable valueTable;

	public static StackPane getInstance() {
		if (instance == null) {
			instance = new StackPane();
		}
		return instance;
	}

	public JPanel getPanel() {
		return stackFrame;
	}

	private class AddressTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public int getRowCount() {
			return 0x10;
		}

		@Override
		public Object getValueAt(int row, int col) {
			int address = 0x100 + CpuRegisters.getInstance().getStack() - row;
			if(CpuRegisters.getInstance().getStack() < 0x10) {
				address = 0x10F - row;
			}
			return String.format("0x%04X", (short) address);
		}

		public String getColumnName(int col) {
			return "Address";
		}

	}

	private class ValueTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public int getRowCount() {
			return 0x10;
		}

		@Override
		public Object getValueAt(int row, int col) {
			int address = 0x0100 + CpuRegisters.getInstance().getStack() - row;
			if(CpuRegisters.getInstance().getStack() < 0x10) {
				address = 0x10F - row;
			}
			return String.format("%02X",
					CpuMem.getInstance().readMemQuiet(address));
		}

		public void setValueAt(Object obj, int row, int col) {
			try {
				int value = Integer.parseInt((String) obj, 0x10);
				int address = 0x0100 + CpuRegisters.getInstance().getStack() - row;
				if(CpuRegisters.getInstance().getStack() < 0x10) {
					address = 0x10F - row;
				}
				CpuMem.getInstance().writeMemQuiet(address, value);
			} catch (NumberFormatException e) {
				System.err.println("Enter a hex value!");
			}
			MemoryPane.getInstance().fireStateChanged();
		}

		@Override
		public boolean isCellEditable(int x, int y) {
			return true;
		}

		public String getColumnName(int col) {
			return "Val";
		}

	}

	public void fireStateChanged() {
		((AbstractTableModel) valueTable.getModel()).fireTableDataChanged();
		((AbstractTableModel) addressTable.getModel()).fireTableDataChanged();
	}

	private StackPane() {
		/*
		 * First, set up the tables
		 */
		addressTable = new JTable(new AddressTableModel());
		addressTable.getTableHeader().setName("address");
		addressTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		addressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// addressTable.getTableHeader().setReorderingAllowed(false);
		addressTable.setCellSelectionEnabled(false);
		addressTable.getColumnModel().getColumn(0).setPreferredWidth(60);
		addressTable.getColumnModel().getColumn(0).setResizable(false);

		valueTable = new JTable(new ValueTableModel());
		valueTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		valueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// valueTable.getTableHeader().setReorderingAllowed(false);
		valueTable.setCellSelectionEnabled(false);
		valueTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		valueTable.getColumnModel().getColumn(0).setResizable(false);

		stackFrame = new JPanel();
		stackFrame.setBorder(new TitledBorder(new EtchedBorder(), "Stack"));
		stackFrame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		stackFrame.add(addressTable.getTableHeader(), constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		stackFrame.add(addressTable, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		stackFrame.add(valueTable.getTableHeader(), constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		stackFrame.add(valueTable, constraints);

	}
}
