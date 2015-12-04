package sophie.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by sophie on 2015. 12. 4..
 */
class FileSender implements Runnable {
    private Socket socket;
    private String fileName;

    private DataOutputStream dataOutputStream; // 데이터 전송용 스트림
    private FileInputStream fileInputStream; //파일 읽는용 스트림
    private BufferedInputStream bufferedInputStream; // 파일 읽는용 스트림 - buffered

    public FileSender(Socket socket, String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("파일 전송 작업을 시작합니다.");

            // 파일 내용을 읽으면서 전송
            File file = new File(fileName);
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);

            int len;
            int size = 4096;
            byte[] data = new byte[size];
            while ((len = bufferedInputStream.read(data)) != -1) {
                dataOutputStream.write(data, 0, len);
            }

            dataOutputStream.flush();
            dataOutputStream.close();
            bufferedInputStream.close();
            fileInputStream.close();
            System.out.println("파일 전송 작업을 완료하였습니다.");
            System.out.println("보낸 파일의 사이즈 : " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
