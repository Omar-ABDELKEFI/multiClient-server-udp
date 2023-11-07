import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MessageReceiver implements Runnable {
    DatagramSocket socket;      // Datagram socket for receiving messages.
    byte[] buffer;              // Byte array to store received message data.
    ClientWindow window;        // Reference to the GUI window for displaying received messages.

    public MessageReceiver(DatagramSocket sock, ClientWindow win) {
        socket = sock;           // Initialize the socket for message reception.
        buffer = new byte[1024];  // Initialize the buffer to store received message data.
        window = win;            // Initialize the reference to the client's GUI window.
    }

    public void run() {
        while (true) {
            try {
                // Create a DatagramPacket to receive incoming messages.
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);  // Receive a message into the packet.

                // Extract the received message data from the packet and trim it.
                String received = new String(packet.getData(), 0, packet.getLength() - 1).trim();

                // Display the received message in the client's GUI window.
                window.displayMessage(received);
            } catch (Exception e) {
                System.err.println(e);  // Print any exceptions that occur during message reception.
            }
        }
    }
}