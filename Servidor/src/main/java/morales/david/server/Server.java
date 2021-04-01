package morales.david.server;

import morales.david.server.clients.ClientRepository;
import morales.david.server.clients.ClientThread;
import morales.david.server.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket server;

    private ClientRepository clientRepository;

    private boolean running;

    private void init() throws IOException {

        server = new ServerSocket(Constants.SERVER_PORT);
        clientRepository = new ClientRepository();
        running = true;

        System.out.println(String.format(Constants.LOG_SERVER_INIT, Constants.SERVER_PORT));

        while(running) {

            Socket clientSocket = server.accept();

            ClientThread clientThread = new ClientThread(this, clientSocket);
            clientThread.setDaemon(true);
            clientThread.start();

            System.out.println(Constants.LOG_SERVER_USER_CONNECTED);

        }

    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public static void main(String[] args) {

        Server srv = new Server();
        try {
            srv.init();
        } catch (IOException e) {
            System.out.println(Constants.LOG_SERVER_ERROR);
            e.printStackTrace();
        }

    }

}
