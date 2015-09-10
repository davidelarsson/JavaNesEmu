package debugger;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class WindowCollection extends JFrame implements ActionListener,
		ItemListener {

	/**
	 * Creates a JFrame and adds internal windows and menus to it
	 */
	private static final long serialVersionUID = 1L;
	private JFrame mainFrame;
	private JMenuBar bar;

	public ArrayList<JInternalFrame> windowList = new ArrayList<JInternalFrame>();

	public void addPane(JPanel panel, Point point) {
		JInternalFrame internal = new JInternalFrame(panel.getName());
		internal.setBounds(point.x, point.y, panel.getSize().width,
				panel.getSize().height);
		internal.add(panel);
		windowList.add(internal);
		internal.setResizable(true);
		mainFrame.add(internal);
		internal.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// First check if the command is from the File menu
		if (e.getActionCommand().equals("Open File...")) {
			System.out.println("Loading a new file...");
		}
		if (e.getActionCommand().equals("Close")) {
			System.out.println("Exiting...");
			System.exit(0);
		}
	}

	/*
	 * This method is only used for the windows menu
	 */
	@Override
	public void itemStateChanged(ItemEvent item) {
		Object source = item.getItemSelectable();
		int numOfWindows = windowList.size();
		for (int x = 0; x < numOfWindows; x++) {
			if (bar.getMenu(1).getItem(x) == source) {
				if (item.getStateChange() == ItemEvent.DESELECTED) {
					windowList.get(x).setVisible(false);
				} else
					windowList.get(x).setVisible(true);
			}
		}
	}

	private void setupMenus() {
		bar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		JMenuItem openFileMenuItem = new JMenuItem("Open File...");
		openFileMenuItem.addActionListener(this);
		fileMenu.add(openFileMenuItem);

		JMenuItem closeMenuItem = new JMenuItem("Close");
		closeMenuItem.addActionListener(this);
		fileMenu.add(closeMenuItem);

		JMenu windowMenu = new JMenu("Window");
		for (JInternalFrame frame : windowList) {
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(frame.getTitle());
			item.setSelected(true);
			windowMenu.add(item);
			item.addItemListener(this);
		}

		bar.add(fileMenu);
		bar.add(windowMenu);
		setJMenuBar(bar);

	}

	/**
	 * 
	 * @param name
	 *            is the title of the main window.
	 * @param paneList
	 *            is the list of panes to be created as internal frames
	 * @param sizeX
	 *            is the width of the main window.
	 * @param sizeY
	 *            is the height of the main window.
	 */
	public WindowCollection(String name, Object[][] paneList, int sizeX,
			int sizeY) {
		super(name);
		mainFrame = this;
		// FIXME!! No need to clone?
		JDesktopPane desktop = new JDesktopPane();
		setContentPane(desktop);
		setSize(new Dimension(sizeX, sizeY));
		setLocation(new Point(0, 0));
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		for (Object[] p : paneList) {
			addPane((JPanel) p[0], (Point) p[1]);
		}
		// This must be done last, or otherwise we don't know what windows to
		// add to the menus
		setupMenus();

	}

	// Print a list of the JPanels currently in windowList
	public String toString() {
		String str = "The currently registered windows are:\n";
		for (JInternalFrame frame : windowList) {
			str = str.concat(frame.toString() + "\n");
		}
		return str;
	}

}
