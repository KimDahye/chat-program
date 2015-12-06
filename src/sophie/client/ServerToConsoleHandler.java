package sophie.client;

import sophie.model.Message;
import sophie.model.MessageType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sophie on 2015. 12. 7..
 */
public class ServerToConsoleHandler {
    private static int downloadCount = 1;

    void handle(Message msg) {
        if (msg.getMessageType() == MessageType.CHAT) {
            System.out.println(new String(msg.getBody()));
            return;
        }
        if (msg.getMessageType() == MessageType.FILE) {
            saveFile(msg.getBody());
        }
    }

    void saveFile(byte[] body) {
        try {
            //TODO. 프로토콜에 파일네임 넣는 게 없어서 일단 파일네임은 임의로 정한다. 수정해야 할 부분.
            String fileName = "download" + (downloadCount++) + ".txt";

            // 파일을 생성하고 파일에 대한 출력 스트림 생성
            File file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            // 바이트 데이터를 전송받으면서 파일에 기록
            bufferedOutputStream.write(body, 0, body.length);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            fileOutputStream.close();

            System.out.println("파일 수신 작업을 완료하였습니다.");
            System.out.println("받은 파일의 사이즈 : " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
