package chat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	JFrame frame;
	JTextArea logArea;
	JTextField inputField;
	JLabel hostLabel;
	PrintWriter out;
	DataInputStream in;
	int port = 4014;
	String host = "localhost";
	JPanel serverPanel = new JPanel();
	JPanel clientPanel = new JPanel();
	JPanel cards;
	JComboBox comboBox;
	
	public static void main(String[] args) {
		Server a = new Server();
		a.setup();

	}

	public void setup() {
		setupGui();
		try {
			initializeServer();
		} catch (IOException e) {
			System.err.println("Server won't initialize!");
		}

	}

	private void setupGui() {
		frame = new JFrame("Host");
		hostLabel = new JLabel();

		frame.setLayout(new BorderLayout());
		Container cont = frame.getContentPane();
		cont.setLayout(new BorderLayout());

		cont.add(new ControlPanel(), BorderLayout.NORTH);
		cont.add(new MessagePanel(), BorderLayout.CENTER);
		cont.add(new LogPanel(), BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	private void initializeServer() throws IOException {
		ServerSocket serverSocket = null;

		/*
		 * Initialize the port that we're going to listen to
		 */
		try {
			serverSocket = new ServerSocket(port);
			System.err.println(serverSocket);
		} catch (Exception e) {
			System.err.println("Couldn't listen to port: " + port);
			System.exit(-1);
		}

		/*
		 * Accept connections to the port
		 */
		Socket clientSocket = null;
		try {
			clientSocket = serverSocket.accept();
//			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new DataInputStream(clientSocket.getInputStream());

		} catch (Exception e) {
			System.err.println("Couldn't accept");
			System.exit(-1);
		}
		int readValue;
		try {
			while ((readValue = in.readChar()) != -1) {
				logArea.append(String.format("%c", readValue));
			}
		} catch (Exception e) {

		} finally {

		}

	}

	class MessagePanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public MessagePanel() {
			super();
			setLayout(new GridBagLayout());
			setBorder(new TitledBorder(new EtchedBorder(), "Message"));
			GridBagConstraints con = new GridBagConstraints();

			con.gridx = 0;
			con.gridy = 0;
			con.gridwidth = 2;
			inputField = new JTextField(50);
			inputField.addActionListener(new SendMessageAction());
			add(inputField, con);

			con.gridx = 1;
			con.gridy = 1;
			con.gridwidth = 1;
			JButton sendButton = new JButton("Send!");
			sendButton.addActionListener(new SendMessageAction());
			add(sendButton, con);
		}
	}

	class ControlPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private String[] MODES = { "Server", "Client" };

		public ControlPanel() {
			super();

			clientPanel = new JPanel();
			setBorder(new TitledBorder(new EtchedBorder(), "Control"));

			/*
			 * The server panel
			 */
			serverPanel = new JPanel();
			JButton listenButton = new JButton("Listen");
			serverPanel.add(listenButton);

			/*
			 * The client panel
			 */
			clientPanel = new JPanel();
			clientPanel.setLayout(new GridBagLayout());
			GridBagConstraints con = new GridBagConstraints();

			con.gridx = 0;
			con.gridy = 0;
			JButton setupHostButton = new JButton("host");
			setupHostButton.addActionListener(new SetupHostAction());
			clientPanel.add(setupHostButton, con);

			con.gridx = 1;
			JButton setupPortButton = new JButton("port");
			setupPortButton.addActionListener(new SetupPortAction());
			clientPanel.add(setupPortButton, con);

			con.gridx = 2;
			JButton connectButton = new JButton("connect");
			clientPanel.add(connectButton, con);

			con.gridy = 1;
			con.gridx = 0;
			con.gridwidth = 3;
			clientPanel.add(hostLabel, con);

			/*
			 * Make a ComboBox pane to the top
			 */
			JPanel comboBoxPanel = new JPanel();

			comboBox = new JComboBox(MODES);
			comboBox.addItemListener(new ComboBoxAction());
			comboBox.setEditable(false);
			comboBoxPanel.add(comboBox);

			/*
			 * Make a pane below the ComboBox pane that is the currently viewed
			 * card
			 */
			cards = new JPanel();
			cards.setLayout(new CardLayout());
			cards.add(serverPanel, MODES[0]);
			cards.add(clientPanel, MODES[1]);

			/*
			 * Setup our Control Panel
			 */
			setLayout(new BorderLayout());
			add(comboBoxPanel, BorderLayout.NORTH);
			add(cards, BorderLayout.SOUTH);
			

		}

	}
	
	private class ComboBoxAction implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent item) {
			CardLayout cl = (CardLayout) cards.getLayout();
			cl.show(cards, (String)item.getItem());
		}
		
	}

	private void updateHostLabel() {
		hostLabel.setText("Host: " + host + ":" + port);
	}

	class SetupPortAction implements ActionListener {
		String tmpHost;
		int tmpPort;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				String str = JOptionPane.showInputDialog(
						frame.getContentPane(), "Port?");
				tmpPort = Integer.parseInt(str);
				port = tmpPort;
				host = tmpHost;
			} catch (Exception e) {
				System.err.println("Not a valid port!");
			}
			System.out.println(tmpHost);
			updateHostLabel();
		}

	}

	class SetupHostAction implements ActionListener {
		String tmpHost = null;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				tmpHost = JOptionPane.showInputDialog(frame.getContentPane(),
						"Host?");
				host = tmpHost;
			} catch (Exception e) {
				System.err.println(tmpHost + " is not a valid hostname!");
			}
			updateHostLabel();
		}

	}

	class SendMessageAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			logArea.append("Server (me): " + inputField.getText() + "\n");
		}

	}

	/**
	 * Call this when a message is received from the other host.
	 * 
	 * @author madman
	 * 
	 */
	class MessageReceived {
		public void messageReceived(String msg) {
			logArea.append("Client (other): " + msg + "\n");
		}
	}

	class LogPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public LogPanel() {
			super(new BorderLayout());
			logArea = new JTextArea(10, 20);
			JScrollPane scrollPanel = new JScrollPane(logArea);
			scrollPanel
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPanel.setBorder(new TitledBorder(new EtchedBorder(), "Log"));

			add(scrollPanel, BorderLayout.CENTER);

		}

	}
}