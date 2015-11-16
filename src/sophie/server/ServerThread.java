package sophie.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by sophie on 2015. 11. 16..
 * TODO. 적당한 이름을 못 짓겠음 ㅠㅠ 일단 대충 지었다. ClientReader
 */
public class ServerThread extends Thread{
    private Socket socket = null;
    private ReaderWriter readerWriter;
    private ServerHandler serverHandler;

    public ServerThread (ServerHandler serverHandler, Socket socket) throws IOException{
        this.socket = socket;
        this.serverHandler = serverHandler;
        readerWriter = new ReaderWriter();
        readerWriter.open(socket);
    }

    public void run () {
        while(true){
            try {
                String  msg = readerWriter.readLine();
                serverHandler.handle(socket.getPort(), msg);
            } catch (IOException ioe) {

            }
        }
    }

    public void send(String msg) {
        readerWriter.write(socket.getPort() + ": " + msg);
    }

    public void sendBye(){
        readerWriter.write(".Bye");
    }

    public void close() throws IOException {
        readerWriter.close();
    }

    public int getPort() {
        return socket.getPort();
    }
}
