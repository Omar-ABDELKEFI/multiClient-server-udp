import java.net.DatagramSocket;

public class ChatClient {
	public static void main(String args[]) throws Exception {
		ClientWindow window = new ClientWindow();
		String host = window.getHostName();
		window.setTitle("UDP CHAT Server: " + host);
		DatagramSocket socket = new DatagramSocket();
		MessageReceiver receiver = new MessageReceiver(socket, window);
		MessageSender sender = new MessageSender(socket, host, window);
		Thread receiverThread = new Thread(receiver);
		Thread senderThread = new Thread(sender);
		receiverThread.start();
		senderThread.start();
	}
}