import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Arrays;

public class ChatServer implements Runnable {
    public final static int PORT = 2020;
    private final static int BUFFER = 1024;

    private DatagramSocket socket;
    private ArrayList<InetAddress> client_addresses;
    private ArrayList<Integer> client_ports;
    static private HashMap<String, String> clientUsernames; // Mapping of client addresses to usernames

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ChatServer() throws IOException {
        socket = new DatagramSocket(PORT);
        System.out.println("Server is running and is listening on port " + PORT);
        client_addresses = new ArrayList();
        client_ports = new ArrayList();
        clientUsernames = new HashMap<>(); // Initialize the mapping
    }

    public void run() {
        byte[] buffer = new byte[BUFFER];
        while (true) {
            try {
                System.out.println(clientUsernames.toString());
                Arrays.fill(buffer, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(buffer, 0, buffer.length);

                InetAddress clientAddress = packet.getAddress();
                int client_port = packet.getPort();

                String id = clientAddress.toString() + "|" + client_port;

                // Prompt the client for a username and store it
                while ( !clientUsernames.containsKey(id)) {
                       {
                        DatagramPacket usernamePromptPacket = new DatagramPacket("Please enter your username".getBytes(), "Please enter your username".length(), clientAddress, client_port);
                        socket.send(usernamePromptPacket);
                        DatagramPacket usernameResponsePacket = new DatagramPacket(new byte[BUFFER], BUFFER);
                        socket.receive(usernameResponsePacket);
                        String username = new String(usernameResponsePacket.getData(), 0, usernameResponsePacket.getLength()).trim();
                        System.out.println("username++++++++ " + username);
                        // Check if the username already exists
                        if (clientUsernames.containsValue(username)) {
                            // Username exists, repeat the prompt
                            continue;
                        } else {
                            // Username is unique, store it and break the loop
                            clientUsernames.put(id, username);
                            client_ports.add(client_port);
                            client_addresses.add(clientAddress);
                            break;
                        }
                    }
                }
// Prepend the username to the message

                String username = clientUsernames.get(id);
                System.out.println(username + " : " + message);

                String[] parts = message.split(" - ");
                if (parts.length == 3 && parts[0].equals("username")) {
                    System.out.println(parts[1] + "ffffffffffffffff");
                    String specifiedUsername = parts[1];
                    System.out.println(specifiedUsername +"specifiedUsername");
                    System.out.println(clientUsernames.values() +"specifiedUsername");

                    // Check if the specified username exists
                    if (clientUsernames.values().contains(specifiedUsername)) {
                        String[] partsCopy = Arrays.copyOfRange(parts, 2, parts.length);
                        String modifiedString = String.join(" - ", partsCopy);

                        byte[] data = (username + " : " + modifiedString).getBytes();
                        for (String key : clientUsernames.keySet()) {
                            if (clientUsernames.get(key).equals(specifiedUsername)) {
                                // Split the key to extract clientAddress and client_port
                                String[] partsKey = key.split("\\|");
                                System.out.println(Arrays.toString(partsKey)+"partsKeypartsKey");
                                if (partsKey.length == 2) {
                                    InetAddress clientAddressTo = InetAddress.getByName(partsKey[0].substring(1));
                                    int clientPort = Integer.parseInt(partsKey[1]);
                                    packet = new DatagramPacket(data, data.length, clientAddressTo, clientPort);
                                    socket.send(packet);
                                    // Now, you have the clientAddress and clientPort for the specified value
                                    System.out.println("Client Address: " + clientAddress);
                                    System.out.println("Client Port: " + clientPort);
                                }
                            }
                        }
                    } else {
                        // Send a message to the current client indicating that the specified username does not exist
                        String errorMessage = "The specified username does not exist: " + specifiedUsername;
                        byte[] errorData = errorMessage.getBytes();
                        packet = new DatagramPacket(errorData, errorData.length, clientAddress, client_port);
                        socket.send(packet);
                    }
                } else {
                    byte[] data = (username + " : " + message).getBytes();
                    for (int i = 0; i < client_addresses.size(); i++) {
                        InetAddress cl_address = client_addresses.get(i);
                        int cl_port = client_ports.get(i);
                        packet = new DatagramPacket(data, data.length, cl_address, cl_port);
                        socket.send(packet);
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String args[]) throws Exception {
        ChatServer server_thread = new ChatServer();
        Thread serverThread = new Thread(server_thread);
        serverThread.start();
    }
}
