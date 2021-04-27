package networkpackage;

import cartapackage.Carta;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteTCP {

    private Socket clientSocket;
    private Socket clientSocketObject;
    private BufferedReader inputFromServer;
    private DataOutputStream outputToServer;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public static final int PORTA_MESSAGE = 6557;
    public static final int PORTA_OBJECT = 6558;

    public void connect(String IP) throws IOException {
        clientSocket = new Socket(IP, PORTA_MESSAGE);
        clientSocketObject = new Socket(IP, PORTA_OBJECT);
        _connect_default();
    }

    public void connect(String IP, String IPLocal, int portaLocal) throws IOException {
        clientSocket = new Socket(IP, PORTA_MESSAGE, InetAddress.getByName(IPLocal), portaLocal);
        clientSocketObject = new Socket(IP, PORTA_OBJECT, InetAddress.getByName(IPLocal), portaLocal);
        _connect_default();
    }

    private void _connect_default() throws IOException {
        System.out.println("Conectado ao servidor: " + clientSocket.getInetAddress());
        inputFromServer = new BufferedReader(new InputStreamReader(clientSocketObject.getInputStream()));
        outputToServer = new DataOutputStream(clientSocket.getOutputStream());

        objectInputStream = new ObjectInputStream(clientSocketObject.getInputStream());
        objectOutputStream = new ObjectOutputStream(clientSocketObject.getOutputStream());
    }

    public void sendMessage(String message) throws IOException {
        outputToServer.writeBytes(message + '\r');
        outputToServer.flush();
    }

    public String readMessage() throws IOException {
        return inputFromServer.readLine();
    }

    public void sendCarta(Carta carta) throws IOException {
        objectOutputStream.writeObject(carta);
        objectOutputStream.flush();
    }

    // Fonte: https://stackoverflow.com/questions/32500182/find-all-ip-addresses-in-local-network
    public String[] getNetworkIP() {
        final byte[] IP;
        ArrayList<String> IPs = new ArrayList<>();

        try {
            IP = InetAddress.getLocalHost().getAddress();
        } catch (Exception exception) {
            System.err.println("Error: " + exception.getMessage());
            return null;  // Exit method, otherwise "IP might not have been initialized"
        }

        for (int i = 0; i <= 255; i++) {
            final int j = 1;  // i as non-final variable cannot be referenced from inner class

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        IP[3] = (byte) j;
                        InetAddress address = InetAddress.getByAddress(IP);
                        String output = address.toString().substring(1);
                        if (address.isReachable(20)) {
                            IPs.add(output);
                        }
                    } catch (Exception exception) {
                        System.err.println("Error: " + exception.getMessage());
                    }
                }
            }).start();
        }

        String[] IPsArray = new String[IPs.size()];
        IPsArray = IPs.toArray(IPsArray);

        return IPsArray;
    }

    public void close() throws IOException {
        clientSocket.close();
    }

}
