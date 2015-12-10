package sophie.server;

import sophie.model.Message;
import sophie.utils.IOUtils;

import java.io.*;
import java.net.Socket;

/**
 * Created by sophie on 2015. 12. 2..
 */
class ClientHandler extends Thread {
    private Socket socket = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private RoomManager roomManager = null;
    private String nickname = null; // unique 하면 좋지만 그건 DB에 저장할 때 처리될 부분이라 생각하여 패스.

    ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        InputStream in = socket.getInputStream();
        this.dis = new DataInputStream(new BufferedInputStream(in));
        this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    void setNickname(String nickname) {
        this.nickname = nickname;
    }

    String getNickname() {
        return this.nickname;
    }

    @Override
    public void run() {
        while (true) {
            try {
                dispatchRequest();
            } catch (IOException e) {
                closeAll();
                roomManager.remove(this);
                System.out.println(nickname + " is disconnected.");
                break;
            }
        }
    }

    void dispatchRequest() throws IOException {
        Message message = IOUtils.getMessage(dis);
        roomManager.handle(this, message.getMessageType(), message.getBody());
    }

    Message getMessage() throws IOException {
        return IOUtils.getMessage(dis);
    }

    //TODO. 이 부분 IOUtils 쓰도록 리팩토링.
    void sendMessage(Message message) {
        int typeValue = message.getMessageType().getValue();
        byte[] body = message.getBody();

        synchronized (dos) {
            try {
                dos.writeInt(typeValue);
                dos.writeInt(body.length);
                dos.write(body);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                closeAll();
                if (roomManager != null) roomManager.remove(this);
            }
        }
    }

    byte[] getBodyWithNickname(byte[] body) {
        return join((nickname + ": ").getBytes(), body);
    }

    //org.apache.commons.lang3.ArrayUtils 의 addAll(first, second)이용하면 한 줄에 끝낼 수 있다.
    // TODO. Maven 도입하면 org.apache.commons.lang3.ArrayUtils import하고 addAll 메소드 사용하도록 바꾼다.
    private byte[] join(byte[] arr1, byte[] arr2) {
        int size1 = arr1.length;
        int size2 = arr2.length;
        byte[] newArr = new byte[size1 + size2];
        System.arraycopy(arr1, 0, newArr, 0, size1);
        System.arraycopy(arr2, 0, newArr, size1, size2);
        return newArr;
    }

    private void closeAll() {
        try {
            if (dis != null) dis.close();
            if (dos != null) dos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
