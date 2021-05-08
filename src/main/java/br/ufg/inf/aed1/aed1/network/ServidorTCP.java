package br.ufg.inf.aed1.aed1.network;

import br.ufg.inf.aed1.aed1.carta.Carta;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class ServidorTCP {

    private ServerSocket serverSocketMessage;
    private Socket connectionSocketMessage;
    private Socket connectionSocketObject;
    private ServerSocket serverSocketObject;
    private BufferedReader inputFromClient;
    private DataOutputStream outputToClient;

    public static final int TIME_OUT = 20000;
    public static final int PORTA_MESSAGE = 6557;
    public static final int PORTA_OBJECT = 6558;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public void initialize() throws IOException {
        serverSocketMessage = new ServerSocket(PORTA_MESSAGE);
        serverSocketObject = new ServerSocket(PORTA_OBJECT);

        serverSocketMessage.setSoTimeout(TIME_OUT);
        serverSocketObject.setSoTimeout(TIME_OUT);

        connectionSocketMessage = serverSocketMessage.accept();
        connectionSocketObject = serverSocketObject.accept();

        System.out.printf("Cliente conectado: + %s\n", connectionSocketMessage.getInetAddress().getHostAddress());

        inputFromClient = new BufferedReader(new InputStreamReader(connectionSocketMessage.getInputStream()));
        outputToClient = new DataOutputStream(connectionSocketMessage.getOutputStream());

        objectInputStream = new ObjectInputStream(connectionSocketObject.getInputStream());
        objectOutputStream = new ObjectOutputStream(connectionSocketObject.getOutputStream());
    }

    public String readMessage() throws IOException {
        return inputFromClient.readLine();
    }

    public void sendMessage(String message) throws IOException {
        outputToClient.writeBytes(message + '\r');
    }

    public Carta readCarta() throws IOException, ClassNotFoundException {
        return (Carta) objectInputStream.readObject();
    }

    public void sendCarta(Carta carta) throws IOException {
        objectOutputStream.writeObject(carta);
        objectOutputStream.flush();
    }

    public void close() throws IOException {
        serverSocketMessage.close();
        serverSocketObject.close();
    }

}
