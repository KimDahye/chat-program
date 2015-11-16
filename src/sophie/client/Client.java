package sophie.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by sophie on 2015. 11. 16..
 */
public class Client {
    private Socket socket = null;
    private ConsoleToServer consoleToServer = null;
    private ServerToConsole serverToConsole = null;

    public Client(String serverName, int serverPort) {
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            consoleToServer = new ConsoleToServer(socket);
            serverToConsole = new ServerToConsole(this, socket);
        }catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    public void operate(){
        consoleToServer.run();
        serverToConsole.run();
    }

    public void handle(String serverMsg) throws IOException {
        if(serverMsg.equals(".bye")){
            stop();
        }
    }

    public void stop() {
        consoleToServer.stop();
        serverToConsole.stop();

    }

    public static void main(String args[]) {
        final int DEFAULT_PORT = 9001;
        final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";

        Client client = new Client(DEFAULT_SERVER_ADDRESS, DEFAULT_PORT);
        client.operate();
    }
}
