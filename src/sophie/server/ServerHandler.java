package sophie.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sophie on 2015. 11. 16..
 */
public class ServerHandler {
    private ArrayList<ServerThread> serverThreads = null; //이걸 없애보려고 했지만 어짜피 thread 리스트 필드를 갖고있어야만 run 이외의 메소드를 부를 수 있어서 남겨둘 수 밖에 없다.
    private ExecutorService executor = null;
    private int curThreadNum = 0;
    private int threadMaxNum = 0;
    private static final String END_MESSAGE = ".bye";

    ServerHandler(int maxClientNum) {
        serverThreads = new ArrayList<ServerThread>(maxClientNum);
        this.threadMaxNum = maxClientNum;
        executor = Executors.newFixedThreadPool(maxClientNum);
    }

    public void handle(int port, String msg) {
        if (msg.equals(END_MESSAGE)) {
            int index = findThreadIndex(port);
            serverThreads.get(index).sendBye(END_MESSAGE);
            remove(index);
        } else {
            for (ServerThread thread : serverThreads) {
                if(thread != null) thread.send(msg);
            }
        }
    }

    public void addClient(Socket socket) {
        if (curThreadNum < threadMaxNum) {
            System.out.println("Client accepted: " + socket);
            try {
                ServerThread serverThread = new ServerThread(this, socket);
                executor.execute(serverThread);
                serverThreads.add(serverThread);
                curThreadNum++;
            } catch (IOException ioe) {
                System.out.println("Client refused: max client count");
            }
        }
    }

    public synchronized void remove(int index){
        ServerThread toTerminate = serverThreads.get(index);
        serverThreads.remove(index);
        curThreadNum--;
        try {
            toTerminate.close();
            toTerminate.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int findThreadIndex(int port){
        for(int i = 0; i < curThreadNum; i++ ){
            ServerThread temp = serverThreads.get(i);
            if(temp != null && temp.getPort() == port) {
                return i;
            }
        }
        return -1;
    }
}
