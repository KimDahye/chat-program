package sophie.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by sophie on 2015. 11. 16..
 */
public class ServerThread extends Thread {
    private Socket socket = null;
    private ReaderWriter readerWriter;
    private ServerHandler serverHandler;

    public ServerThread(ServerHandler serverHandler, Socket socket) throws IOException {
        this.socket = socket;
        this.serverHandler = serverHandler;
        readerWriter = new ReaderWriter();
        readerWriter.open(socket);
    }

    public void run() {
        while (true) {
            try {
                String msg = readerWriter.readLine();
                serverHandler.handle(socket.getPort(), msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String msg) {
        readerWriter.writeLine(socket.getPort() + ": " + msg);
    }

    public void sendBye(String endMsg) {
        readerWriter.writeLine(endMsg);
    }

    public void close() throws IOException {
        socket.close();
        readerWriter.close();
    }

    public int getPort() {
        return socket.getPort();
    }
}
