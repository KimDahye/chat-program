package sophie.client;

import sophie.client.exception.ClientEndException;
import sophie.client.exception.OutOfFileLengthLimitException;
import sophie.utils.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by sophie on 2015. 12. 7..
 * 클라이언트 프로그램 규칙 handling
 */
class ConsoleMessageHandler {
    private DataOutputStream dos = null;
    private static final String END_COMMAND = ".bye";
    private static final String FILE_COMMAND = ".file";

    ConsoleMessageHandler(DataOutputStream dos) {
        this.dos = dos;
    }

    void handle(String msg) throws ClientEndException, IOException {
        if (msg.equals(END_COMMAND)) {
            throw new ClientEndException();
        }
        if (msg.startsWith(FILE_COMMAND)) {
            String[] split = msg.split("\\s+");
            String fileName = split[1];
            try {
                IOUtils.sendFileMessage(dos, fileName);
                System.out.println(fileName + " 파일을 전송하였습니다.");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (OutOfFileLengthLimitException e) {
                System.out.println(e.getMessage());
            }
            return;
        }
        IOUtils.sendChatMessage(dos, msg);
    }

    public void close() {
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}