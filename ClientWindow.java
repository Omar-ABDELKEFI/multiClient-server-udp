import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class ClientWindow extends JFrame {
	// Instance variables to store hostName, message, username, and message readiness flag.
	String hostName;
	String message = "";
	String username = "";
	boolean messageIsReady = false;

	// Text panes for displaying messages and entering new messages.
	JTextPane messageField;
	JTextPane roomField;

	public ClientWindow() {
		// Create a text field and "OK" button for entering the server address.
		JTextField hostField = new JTextField("                            ");
		JButton ok = new JButton("OK");

		// Create a dialog for entering the server address.
		JDialog hostNameDialog = new JDialog(this, "Enter server address: ", true);
		hostNameDialog.setLayout(new FlowLayout());
		hostNameDialog.add(hostField);
		hostNameDialog.add(ok);
		hostNameDialog.setLocationRelativeTo(null);
		hostNameDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		hostNameDialog.setSize(250, 150);
		hostNameDialog.setResizable(false);

		// Action listener for the "OK" button to capture the server address.
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hostName = hostField.getText().trim();
				hostNameDialog.dispose();
			}
		});

		// Make the server address dialog visible.
		hostNameDialog.setVisible(true);

		// Set up the main chat window.
		setSize(800, 600);
		setTitle("UDP Chat Room");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Initialize text panes for displaying messages and entering new messages.
		roomField = new JTextPane();
		messageField = new JTextPane();
		roomField.setEditable(false);

		// Create scroll panes for both text panes.
		JScrollPane roomScrollPane = new JScrollPane(roomField);
		JScrollPane messageScrollPane = new JScrollPane(messageField);
		messageScrollPane.setPreferredSize(new Dimension(100, 100));

		// Add text panes to the main window's layout.
		add(roomScrollPane, BorderLayout.CENTER);
		add(messageScrollPane, BorderLayout.SOUTH);

		// Make the main window visible.
		setVisible(true);

		// Key listener for the message text pane to capture user input.
		messageField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// Check if the Enter key is pressed and a message is not ready to send.
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !messageIsReady) {
					message = messageField.getText().trim();
					messageField.setText(null);
					if (!message.isEmpty()) {
						messageIsReady = true;
					}
				}
			}
		});
	}

	// Getter for the username (currently unused).
	public String getUsername() {
		return username;
	}

	// Method to display a received message in the chat room.
	public void displayMessage(String receivedMessage) {
		StyledDocument doc = roomField.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), receivedMessage + "\n", null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	// Setter for message readiness flag.
	public void setMessageReady(boolean messageReady) {
		this.messageIsReady = messageReady;
	}

	// Getter for the composed message.
	public String getMessage() {
		return message;
	}

	// Getter for the server address.
	public String getHostName() {
		return hostName;
	}
}
