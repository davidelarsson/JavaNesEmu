package debugger;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import memory.CpuMem;


public class MemoryPane {

	/**
	 * Make sure there only one instance of this object
	 */
	private static MemoryPane instance;

	private JTable valueTable;
	private JTable addressTable;
	private JSpinner offsetSpinner;
	private JButton addBookmarkButton;
	private JButton removeBookmarkButton;
	private JComboBox bookmarksComboBox;
	// The initial bookmarks
	private String[] bookmarksInitTable = { "", "0x0000 (Zero page)",
			"0x0100 (Stack page)", "0xFF00 (Last page)" , "0x8000 (Start)", "0xC000 (Start)"};
	private JPanel memoryFrame;
	private JPanel bookmarkPanel;
	private JPanel tablesPanel;
	private JPanel offsetPanel;
	private JPanel topPanel;
	private int offset = 0x0000;

	/**
	 * Table model for the left-most column that only tells us where we
	 * currently are in memory
	 */
	private class AddressTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 2087696848246674231L;

		public int getColumnCount() {
			return 0x01;
		}

		public int getRowCount() {
			return 0x10;
		}

		public Object getValueAt(int row, int col) {
			return String.format("0x%04X", offset + row * 0x10);

		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public String getColumnName(int col) {
			return "Location";
		}
	}

	/**
	 * Table model for memory values, editable 16x16 matrix with String values
	 */
	private class ValueTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 4154761176941298605L;

		public int getColumnCount() {
			return 0x10;
		}

		public int getRowCount() {
			return 0x10;
		}

		public Object getValueAt(int row, int column) {
			return String.format(
					"%02X",
					CpuMem.getInstance().readMemQuiet(
							offset + row * 0x10 + column));
		}

		public void setValueAt(Object obj, int row, int column) {
			try {
				String str = (String) obj;
				int val = Integer.parseInt(str, 16);
				if (val >= 0x00 && val < 0x100) {
					CpuMem.getInstance().writeMemQuiet(
							(offset + row * 0x10 + column), val);
					StackPane.getInstance().fireStateChanged();
				} else {
					System.err.println("Enter a byte value in hex!");
					DebugPane.getInstance().outputDebugMessage(
							"Enter a byte value in hex!\n");
				}

			} catch (NumberFormatException e) {
				System.err.println("Enter a byte value in hex!");
				DebugPane.getInstance().outputDebugMessage(
						"Enter a byte value in hex!\n");
			} finally {
			}
		}

		public boolean isCellEditable(int row, int column) {
			return true;
		}

		public String getColumnName(int col) {
			return Integer.toHexString(col).toUpperCase();
		}

		// FIXME!! What is this?! And why?!
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int col) {
			return String.class;
		}

	}

	// Make sure we have only ever one instance of this object
	public static MemoryPane getInstance() {
		if (instance == null) {
			instance = new MemoryPane();
		}
		return instance;
	}

	public int getOffset() {
		return offset;
	}

	/*
	 * offset can only be set to values between 0x000 and 0xff00, and only with
	 * 0x10 intervals
	 */
	public void setOffset(int offset) {
		if (offset >= 0 && offset < 0xff00)
			this.offset = offset & 0xfff0;
		else if (offset < 0x10000)
			this.offset = offset & 0xff00;
		else {
			DebugPane.getInstance().outputDebugMessage("Out of range!\n");
			System.err.println("Out of range!");
		}
		// Update our tables and offsetField
		((AbstractTableModel) valueTable.getModel()).fireTableDataChanged();
		((AbstractTableModel) addressTable.getModel()).fireTableDataChanged();
		offsetSpinner.setValue(String.format("0x%04X", offset));
	}

	class GotoBookmark implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String str = (String) bookmarksComboBox.getSelectedItem();
			// If no bookmark is selected (or rather, if the first option of the
			// non-erasable bookmarks is selected), don't change the offset
			if (str.length() == 0)
				return;
			String newString;
			int newOffset;
			// Extract the address from the currently selected bookmark (i.e.
			// the hex word after the "0x")
			newString = str.substring(2, 6);
			newOffset = Integer.parseInt(newString, 0x10);
			setOffset(newOffset);

		}

	}

	private class AddBookmark implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String bookmarkName;
			int nameLength = 20; // Maximum length of a name of a bookmark
			bookmarkName = JOptionPane.showInternalInputDialog(memoryFrame,
					"Please enter a name of your bookmark. Use up to "
							+ nameLength + " characters");
			if (bookmarkName == null) {
				System.err.println("Bookmark aborted.");
				DebugPane.getInstance().outputDebugMessage("Bookmark aborted.\n");

			} else if (bookmarkName.length() <= nameLength
					&& bookmarkName.length() > 0) {
				bookmarksComboBox.addItem(String.format("0x%04X", offset)
						+ " (" + bookmarkName + ")");
				// Make sure our newly added bookmark is selected
				bookmarksComboBox.setSelectedIndex(bookmarksComboBox
						.getItemCount() - 1);
			} else {
				System.err.println("Use a name between 1 and " + nameLength
						+ " characters.");
				DebugPane.getInstance().outputDebugMessage(
						"Use a name between 1 and " + nameLength
								+ " characters.\n");
			}

		}

	}

	private class RemoveBookmark implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// Never remove the first empty bookmarks
			if (bookmarksComboBox.getSelectedIndex() <= 0) {
				return;
			}
			int oldOffset = offset;
			bookmarksComboBox
					.removeItemAt(bookmarksComboBox.getSelectedIndex());
			// Make sure we don't change the current page to another bookmark
			// just because we removed one
			setOffset(oldOffset);
			bookmarksComboBox.setSelectedIndex(0);
		}

	}

	// If the mouse is scrolled within the table, change the offset
	private class TableScrollEvent implements MouseWheelListener {

		public void mouseWheelMoved(MouseWheelEvent e) {
			int i = e.getWheelRotation();
			if (i == 1)
				offsetSpinner.getNextValue();
			if (i == -1)
				offsetSpinner.getPreviousValue();
		}

	}

	private class OffsetSpinnerModel extends AbstractSpinnerModel {

		public Object getNextValue() {
			if (offset < 0xff00)
				setOffset(offset + 0x0010);
			bookmarksComboBox.setSelectedIndex(0);
			return String.format("0x%04X", offset);
		}

		public Object getPreviousValue() {
			if (offset >= 0x0010) {
				setOffset(offset - 0x0010);
			}
			bookmarksComboBox.setSelectedIndex(0);
			return String.format("0x%04X", offset);

		}

		@Override
		public Object getValue() {
			return String.format("0x%04X", offset);
		}

		public void setValue(Object value) {
			String str = (String) value;
			if (str.startsWith("0x")) {
				str = str.substring(2);
			}
			int newOffset = Integer.parseInt(str, 0x10);
			if (newOffset >= 0 && newOffset < 0xff00)
				offset = newOffset & 0xfff0;
			else if (newOffset < 0x10000)
				offset = newOffset & 0xff00;
			else {
				System.err.println("Out of range!");
				DebugPane.getInstance().outputDebugMessage("Out of range!\n");
			}
			((AbstractTableModel) valueTable.getModel()).fireTableDataChanged();
			((AbstractTableModel) addressTable.getModel())
					.fireTableDataChanged();
			fireStateChanged();
		}

	}

	private class OffsetSpinnerEditor extends JSpinner.DefaultEditor {

		/**
		 * Editor for OffsetSpinner. Just an editable text field.
		 */
		private static final long serialVersionUID = 4313217377655803733L;

		public OffsetSpinnerEditor(JSpinner spinner) {
			super(spinner);
			getTextField().setEditable(true);
		}

	}

	public void fireStateChanged() {
		((AbstractTableModel) valueTable.getModel()).fireTableDataChanged();
		((AbstractTableModel) addressTable.getModel()).fireTableDataChanged();
	}

	public JPanel getPanel() {
		return memoryFrame;
	}

	/**
	 * Initialize stuff
	 */
	@SuppressWarnings("serial")
	private MemoryPane() {
		/*
		 * Set up the table that shows the currently displayed location of
		 * memory
		 */
		addressTable = new JTable(new AddressTableModel());
		addressTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		addressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addressTable.getTableHeader().setReorderingAllowed(false);
		addressTable.setCellSelectionEnabled(false);
		addressTable.getColumnModel().getColumn(0).setPreferredWidth(60);
		addressTable.getColumnModel().getColumn(0).setResizable(false);

		/*
		 * Set up the table that displays memory values. This includes adding a
		 * cell-specific tooltip
		 */
		valueTable = new JTable(new ValueTableModel()) {
			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int col) {
				JComponent c = (JComponent) super.prepareRenderer(renderer,
						row, col);
				c.setToolTipText(String.format("0x%04X", offset + row * 0x10
						+ col));
				return c;
			}
		};
		valueTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		valueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		valueTable.getTableHeader().setReorderingAllowed(false);
		valueTable.setCellSelectionEnabled(false);
		for (int i = 0; i < valueTable.getColumnCount(); i++) {
			valueTable.getColumnModel().getColumn(i).setPreferredWidth(25);
			valueTable.getColumnModel().getColumn(i).setResizable(false);
			valueTable.getColumnModel().getColumn(i).setCellRenderer(null);

		}

		/*
		 * This is our one and only JFrame for MemoryPane
		 */
		memoryFrame = new JPanel();

		offsetSpinner = new JSpinner(new OffsetSpinnerModel());
		// FIXME!! I really don't get this. Why do I have to use offsetSpinner
		// twice?!
		offsetSpinner.setEditor(new OffsetSpinnerEditor(offsetSpinner));
		offsetSpinner.setPreferredSize(new Dimension(75, 20));

		offsetPanel = new JPanel();
		offsetPanel.setPreferredSize(new Dimension(100, 60));
		offsetPanel.setBorder(new TitledBorder(new EtchedBorder(), "Offset"));
		offsetPanel.add(offsetSpinner);

		// A button that adds the current offset to the bookmark list
		addBookmarkButton = new JButton("Add");
		addBookmarkButton.addActionListener(new AddBookmark());

		// A button that removes the currently selected bookmark
		removeBookmarkButton = new JButton("Remove");
		removeBookmarkButton.addActionListener(new RemoveBookmark());

		// A JComboBox that contains the bookmarks
		bookmarksComboBox = new JComboBox(bookmarksInitTable);
		bookmarksComboBox.addActionListener(new GotoBookmark());
		bookmarksComboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");

		// A panel at the top to which we add our buttons and combobox
		bookmarkPanel = new JPanel();
		bookmarkPanel.setPreferredSize(new Dimension(370, 60));
		bookmarkPanel.setLayout(new GridBagLayout());
		bookmarkPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Bookmarks"));
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		bookmarkPanel.add(offsetPanel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		bookmarkPanel.add(bookmarksComboBox, constraints);
		constraints.gridx = 2;
		constraints.gridy = 0;
		bookmarkPanel.add(addBookmarkButton, constraints);
		constraints.gridx = 3;
		constraints.gridy = 0;
		bookmarkPanel.add(removeBookmarkButton, constraints);

		topPanel = new JPanel();
		topPanel.add(offsetPanel);
		topPanel.add(bookmarkPanel);

		// tablesPanel contains all our tables in a nice-looking combined way
		tablesPanel = new JPanel();
		tablesPanel.setLayout(new GridBagLayout());
		tablesPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Main memory"));
		constraints.gridx = 0;
		constraints.gridy = 0;
		tablesPanel.add(addressTable.getTableHeader(), constraints);
		constraints.gridx = 1;
		tablesPanel.add(valueTable.getTableHeader(), constraints);
		constraints.gridy = 1;
		constraints.gridx = 0;
		tablesPanel.add(addressTable, constraints);
		constraints.gridx = 1;
		tablesPanel.add(valueTable, constraints);

		// We want tablesPanel to be scrollable with the mouse
		TableScrollEvent l = new TableScrollEvent();
		tablesPanel.addMouseWheelListener(l);
		offsetPanel.addMouseWheelListener(l);

		// Add the two panels to our JFrame
		memoryFrame.setLayout(new GridBagLayout());
		constraints.gridy = 0;
		constraints.gridx = 0;
		memoryFrame.add(topPanel, constraints);
		constraints.gridy = 1;
		memoryFrame.add(tablesPanel, constraints);
		memoryFrame.setName("Memory");
	}
}
