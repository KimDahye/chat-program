package sophie.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by sophie on 2015. 12. 2..
 */
public class ClientHandler extends Thread {
    private Socket socket = null;
    private ReaderWriter readerWriter = null;
    private RoomManager roomManager = null;
    private String nickname = null; // unique 하면 좋지만 그건 DB에 저장할 때 처리될 부분이라 생각하여 패스.

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        readerWriter = new ReaderWriter();
        readerWriter.open(socket);
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public void run() {
        while (true) {
            String msg = receive();
            roomManager.handle(this, msg);
        }
    }

    public String receive( ) {
        try {
            return readerWriter.readLine();
        }catch (IOException e){
            e.getStackTrace();
        }
        return null;
    }

    public void send(String msg) {
        readerWriter.writeLine(msg);
    }

    public void sendBye(String endMsg) {
        readerWriter.writeLine(endMsg);
    }

    public void close() {
        try {
            socket.close();
            readerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
