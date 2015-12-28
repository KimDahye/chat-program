import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sophie on 2015. 11. 16..
 */
public class Client {
    private Socket socket = null;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ConsoleToServer consoleToServer = null;
    private ServerToConsole serverToConsole = null;

    private ExecutorService executor = null;
    private static final int THREAD_NUMBER = 2;

    Client(String serverName, int serverPort) {
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            executor = Executors.newFixedThreadPool(THREAD_NUMBER);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    void operate() {
        OutputStream out = null;
        InputStream in = null;
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
        }catch (IOException e){
            e.printStackTrace();
            closeAllResources();
            return;
        }
        dos = new DataOutputStream(out);
        dis = new DataInputStream(in);
        ConsoleMessageHandler consoleMessageHandler = new ConsoleMessageHandler(dos);
        consoleToServer = new ConsoleToServer(consoleMessageHandler, this);
        serverToConsole = new ServerToConsole(dis, new ServerToConsoleHandler(executor, consoleToServer, dos));
        executor.execute(serverToConsole);
    }

    void closeAllResources() {
        consoleToServer.close();
        serverToConsole.close();
        executor.shutdown();
        try {
            dos.close();
            dis.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String args[]) {
        final int CHATTING_PORT = 9001;
        //final int FILE_PORT =
        final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";

        Client client = new Client(DEFAULT_SERVER_ADDRESS, CHATTING_PORT);
        client.operate();
    }
}
