package chat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

	JTextArea logArea;
	JTextField inputField;
	DataOutputStream out;
	BufferedReader in;

	public static void main(String[] args) {
		Client a = new Client();
		a.setup();
	}

	public void setup() {
		setupGui();
		initializeNetwork();

	}

	private void initializeNetwork() {

		int port = 4014;
		Socket socket;

		try {
			socket = new Socket("localhost", port);
			out = new DataOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (Exception e) {
			System.err.println("Could not connect!");
		}

	}

	private void setupGui() {
		JFrame frame = new JFrame("Client");
		frame.setLayout(new BorderLayout());
		Container cont = frame.getContentPane();
		cont.setLayout(new BorderLayout());

		cont.add(new LogPanel(), BorderLayout.SOUTH);
		cont.add(new ControlPanel(), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	class ControlPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public ControlPanel() {
			super();
			setLayout(new BorderLayout());
			setBorder(new TitledBorder(new EtchedBorder(), "Control"));
			inputField = new JTextField("Text to send");
			inputField.addActionListener(new SendMessageAction());
			add(inputField, BorderLayout.NORTH);

			JButton sendButton = new JButton("send message!");
			sendButton.addActionListener(new SendMessageAction());
			add(sendButton, BorderLayout.SOUTH);
		}
	}

	class SendMessageAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String str = inputField.getText();
			logArea.append("Me: " + str + "\n");
			try {
				out.writeChars(str + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
			logArea.append("Server (other): " + msg + "\n");
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