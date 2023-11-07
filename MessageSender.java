import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MessageSender extends Thread {
    public final static int PORT = 2020;     // The UDP port used for communication.
    private DatagramSocket socket;          // Datagram socket for sending messages.
    private String hostName;                // Server's hostname or IP address.
    private ClientWindow window;            // Reference to the client's GUI window.

    // Constructor to initialize the MessageSender.
    public MessageSender(DatagramSocket sock, String host, ClientWindow win) {
        socket = sock;                       // Initialize the socket for message sending.
        hostName = host;                     // Initialize the hostname or IP address of the server.
        window = win;                        // Initialize the reference to the client's GUI window.
    }

    // Helper method to send a message over the UDP network.
    private void sendMessage(String s) throws Exception {
        byte[] buffer = s.getBytes();                     // Convert the message string to a byte array.
        InetAddress address = InetAddress.getByName(hostName);  // Resolve the server's address.
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);  // Create a packet with the message and destination information.
        socket.send(packet);                             // Send the packet through the socket.
    }

    @Override
    public void run() {
        boolean connected = false;  // A flag to track if the client is connected to the server.
        do {
            try {
                sendMessage("New client connected - welcome!");  // Send a connection message to the server.
                connected = true;  // Mark the client as connected.
            } catch (Exception e) {
                window.displayMessage(e.getMessage());  // Display any errors in the client's GUI window.
            }
        } while (!connected);  // Repeat until the client is connected to the server.

        while (true) {
            try {
                while (!window.messageIsReady) {
                    Thread.sleep(100);  // Wait for a message to be ready in the GUI.
                }
                // Compose a message with the username and user's message, then send it.
                sendMessage(  window.getMessage());
                window.setMessageReady(false);  // Reset the message readiness flag.
            } catch (Exception e) {
                window.displayMessage(e.getMessage());  // Display any errors in the client's GUI window.
            }
        }
    }
}
