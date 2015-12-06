package sophie.server;

import sophie.server.util.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;

/**
 * Created by sophie on 2015. 12. 2..
 */
class ClientHandler extends Thread {
    private Socket socket = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private BufferedReader br = null;

    private RoomManager roomManager = null;
    private String nickname = null; // unique 하면 좋지만 그건 DB에 저장할 때 처리될 부분이라 생각하여 패스.

    private static final String MESSAGE = "Message";
    private static final String FILE = "File";

    ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        InputStream in = socket.getInputStream(); // 하나의 인풋 스트림 공유 -> br로 읽다가 dis로 이어서 읽을 수 있지 않을까?
        this.dis = new DataInputStream(new BufferedInputStream(in));
        this.br = new BufferedReader(new InputStreamReader(in));
        this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                dispatchRequest();
            } catch (IOException e) {
                // 클라이언트에서 연결 끊었을 때
//                TODO. close()해줘야 하지 않나?
//                br.close();
//                dos.close();
//                socket.close();
                e.printStackTrace();
                System.out.println(nickname + " is disconnected.");
                break;
            }
        }

        HttpURLConnection a = new HttpURLConnection() {
            @Override
            public void disconnect() {

            }

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {

            }
        };
        a.getResponseCode()
    }

    void dispatchRequest() throws IOException {
        String headerLine = br.readLine();
        String type = "";
        int contentLength = 0;
        byte[] body = null;


        // header 분석
        while (!"".equals(headerLine)) {
            if(headerLine.startsWith("Content-Type")) {
                type = IOUtils.getContentType(headerLine);
            } else if (headerLine.startsWith("Content-Length")) {
                contentLength = IOUtils.getContentLength(headerLine);
            }
            headerLine = br.readLine();
        }

        //body 읽기
        IOUtils.readData(dis, contentLength);

        //

        roomManager.handle(type, body) {

        }




    }


    String receive() {
        //이것은 IOExceiption 던지지 않는 용도.
        try {
            return readerWriter.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String receiveWithException() throws IOException {
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
