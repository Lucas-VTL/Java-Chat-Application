package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 3000;
    private static List<ClientHandler> clientList = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started. Served on port: " + PORT);

            while (true) {
                Socket socket = server.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(socket, System.currentTimeMillis() + "", this);
                clientList.add(clientHandler);

                new Thread(clientHandler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSystemMessage(String messsage, String ID) {
        for (ClientHandler client : clientList) {
            if (client.getClientID().equals(ID)) {
                client.sendMessage(messsage);
                break;
            }
        }
    }
}
