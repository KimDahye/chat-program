package sophie.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by sophie on 2015. 12. 4..
 */
public class FileReceiver implements Runnable {
    private Socket socket;
    private String fileName;

    public FileReceiver(Socket socket, String fileName) {
        this.socket = socket;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            System.out.println("파일 수신 작업을 시작합니다.");
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            // 파일명을 전송 받고 파일명 수정.
            System.out.println("파일명 " + fileName + "을 전송받았습니다.");
            String[] splitted = fileName.split("\\.");
            fileName = splitted[0] + "_download" + "." + splitted[1];

            // 파일을 생성하고 파일에 대한 출력 스트림 생성
            File file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            System.out.println(fileName + "파일을 생성하였습니다.");

            // 바이트 데이터를 전송받으면서 파일에 기록
            int len;
            int size = 4096;
            byte[] data = new byte[size];
            while ((len = dataInputStream.read(data)) != -1) {
                bufferedOutputStream.write(data, 0, len);
            }

            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            fileOutputStream.close();
            dataInputStream.close();
            
            System.out.println("파일 수신 작업을 완료하였습니다.");
            System.out.println("받은 파일의 사이즈 : " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
