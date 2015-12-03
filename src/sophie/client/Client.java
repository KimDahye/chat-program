package sophie.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sophie on 2015. 11. 16..
 */
class Client {
    private Socket socket = null;
    private ExecutorService executor = null;
    private static final int THREAD_NUMBER = 2;


    private static final String END_MESSAGE = ".bye";


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
        executor.execute(new Thread(new ConsoleToServer(socket)));
        executor.execute(new Thread(new ServerToConsole(this, socket)));
    }

    void handle(String serverMsg) throws IOException {
        if (serverMsg.equals(END_MESSAGE)) {
            System.out.println("Good Bye~!");
            closeAllResources();
        }
    }

    void closeAllResources() throws IOException {
        executor.shutdown(); //ThreadPoolExecution 의 shutdown 메소드에서 큐에 있는 Thread를 interrupt 하니깐 override 된 interrupt 메소드가 잘 불릴 것이라 예상함.
        socket.close();
        System.exit(0);
    }

    public static void main(String args[]) {
        final int DEFAULT_PORT = 9001;
        final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";

        Client client = new Client(DEFAULT_SERVER_ADDRESS, DEFAULT_PORT);
        client.operate();
    }
}
