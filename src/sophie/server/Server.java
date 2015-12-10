package sophie.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sophie on 2015. 12. 2..
 */
class Server {
    private ServerSocket listener = null;
    private RoomListManager roomListManager = null;
    private ExecutorService executor = null;
    private static final int MAX_THREAD_NUM = 100; //RoomSelector Thread 개수

    Server(int messagePort) {
        try {
            System.out.println("Binding to port " + messagePort + ", please wait  ...");
            listener = new ServerSocket(messagePort);
            System.out.println("Server started: " + listener);
            roomListManager = RoomListManager.getInstance();
            executor = Executors.newFixedThreadPool(MAX_THREAD_NUM);
        } catch (IOException e) {
            System.out.println("Can not bind to port " + messagePort + ": " + e.getMessage());
            System.exit(1);
        }
    }

    void accept() {
        while (true) {
            try {
                Socket client = listener.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                executor.execute(new RoomSelector(clientHandler, roomListManager));
            } catch (IOException e) {
                e.printStackTrace();
                // 여기서 나오는 Exception까지 잡아버리면 너무 코드가 보기 싫어지는데... 어떻게 해야하나..? ㅠㅠ
                try {
                    listener.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                System.exit(1);
            }
        }
    }

    public static void main(String args[]) {
        final int MESSAGE_PORT = 9001;
        //final int ROOM_CAPACITY = 10;

        Server server = new Server(MESSAGE_PORT);
        server.accept();
    }
}
