package sophie.server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by sophie on 2015. 11. 16..
 */
public class Server {
    private ServerSocket listener = null;

    private ServerHandler serverHandler = null;

    public Server(int port, int maxClientNum) {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            listener = new ServerSocket(port);
            System.out.println("Server started: " + listener);
            serverHandler = new ServerHandler(maxClientNum);

        } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
            System.exit(1);
        }
    }

    public void operate(){
        while(true) {
            try {
                serverHandler.addClient(listener.accept());
            } catch (IOException e) {
                e.printStackTrace();
                //listener, clientManger 닫아야 하나?
                System.exit(1);
            }
        }
    }

    public static void main(String args[]) {
        final int DEFAULT_PORT = 9001;
        final int MAX_CLIENT_NUMBER = 10;

        Server server = new Server(DEFAULT_PORT, MAX_CLIENT_NUMBER);
        server.operate();
    }
}
