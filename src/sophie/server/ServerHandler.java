package sophie.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by sophie on 2015. 11. 16..
 */
public class ServerHandler {
    private ArrayList<ServerThread> serverThreads = null;
    private int curThreadNum = 0;
    private int threadMaxNum = 0;

    ServerHandler(int maxClientNum) {
        serverThreads = new ArrayList<ServerThread>(maxClientNum);
        this.threadMaxNum = maxClientNum;
    }

    public void handle(int port, String msg) {
        if (msg.equals(".bye")) {
            int index = findThreadIndex(port);
            serverThreads.get(index).sendBye();
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
                serverThread.start(); //run() 이 실행된다.
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
            //toTerminate.stop() 대신 toTerminate.interrupt() 이거 쓰는 건가?
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
