package client;

import java.net.Socket;
import javax.swing.JFrame;

public class Client {
    private static final String URL = "localhost";
    private static final int PORT = 3000;
    private Socket socket;
    private JFrame frame;
    private String socketID = "";
    private String contact = "";
    private String contactStatus = "";

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();
    }

    public void startClient() {
        try {
            socket = new Socket(URL, PORT);
            System.out.println("Connected to server");

            ClientListener clientListener = new ClientListener(this);
            new Thread(clientListener).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setSocketID(String socketID) {
        this.socketID = socketID;
    }

    public String getSocketID() {
        return socketID;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setContactStatus(String status) {
        this.contactStatus = status;
    }

    public String getContactStatus() {
        return contactStatus;
    }
}
