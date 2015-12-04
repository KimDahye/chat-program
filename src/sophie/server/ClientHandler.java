package sophie.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by sophie on 2015. 12. 2..
 */
class ClientHandler extends Thread {
    private Socket socket = null;
    private Socket dataSocket = null;
    private ReaderWriter readerWriter = null;
    private RoomManager roomManager = null;
    private String nickname = null; // unique 하면 좋지만 그건 DB에 저장할 때 처리될 부분이라 생각하여 패스.

    ClientHandler(Socket socket, Socket dataSocket) throws IOException {
        this.socket = socket;
        this.dataSocket = dataSocket;
        readerWriter = new ReaderWriter();
        readerWriter.open(socket);
    }

    void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = receiveWithException();
                roomManager.handle(this, msg);
            }catch (IOException e) {
                //연결이 끝났으니 while loop break해야함.
                // e.printStackTrace();
                System.out.println(nickname + " is disconnected.");
                break;
            }
        }
    }

    String receive(){
        //이것은 IOExceiption 던지지 않는 용도.
        try {
            return readerWriter.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private String receiveWithException() throws IOException{
        return readerWriter.readLine();
    }

    void send(String msg) {
        readerWriter.writeLine(msg);
    }

    void sendBye(String endMsg) {
        readerWriter.writeLine(endMsg);
    }

    void close() {
        try {
            socket.close();
            dataSocket.close();
            readerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getNickname() {
        return nickname;
    }

    void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
