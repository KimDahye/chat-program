package sophie.test;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by sophie on 2015. 12. 4..
 */

public class SendClient {
    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        Socket socket = null;

        try {
            // 서버 연결
            socket = new Socket(serverIp, 7777);
            System.out.println("서버에 연결되었습니다.");

            FileSender fs = new FileSender(socket);
            fs.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FileSender extends Thread {
    Socket socket;
    DataOutputStream dos;
    FileInputStream fis;
    BufferedInputStream bis;

    public FileSender(Socket socket) {
        this.socket = socket;
        try {
            // 데이터 전송용 스트림 생성
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("파일 전송 작업을 시작합니다.");

            // 파일 이름 전송
            // String fName = "작업용a.txt";
            // String fName = "피티a.ppt";
            // String fName = "작업용a.jpg";
            String fName = "src/sophie/test/readme.md";
            dos.writeUTF(fName);
            System.out.printf("파일 이름(%s)을 전송하였습니다.\n", fName);

            // 파일 내용을 읽으면서 전송
            File f = new File(fName);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);

            int len;
            int size = 4096;
            byte[] data = new byte[size];
            while ((len = bis.read(data)) != -1) {
                dos.write(data, 0, len);
            }

            dos.flush();
            dos.close();
            bis.close();
            fis.close();
            System.out.println("파일 전송 작업을 완료하였습니다.");
            System.out.println("보낸 파일의 사이즈 : " + f.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}