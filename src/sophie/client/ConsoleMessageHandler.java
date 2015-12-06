package sophie.client;

import sophie.client.exception.ClientEndException;
import sophie.client.exception.OutOfFileLengthLimitException;
import sophie.model.MessageType;

import java.io.*;

/**
 * Created by sophie on 2015. 12. 7..
 * 클라이언트 프로그램 규칙 handling
 */
public class ConsoleMessageHandler {
    private DataOutputStream dos = null;
    private static final String END_COMMAND = ".bye";
    private static final String FILE_COMMAND = ".file";

    ConsoleMessageHandler(DataOutputStream dos) {
        this.dos = dos;
    }

    void handle(String msg) throws ClientEndException {
        if (msg.equals(END_COMMAND)) {
            throw new ClientEndException();
        }
        if (msg.startsWith(FILE_COMMAND)) {
            String[] split = msg.split("\\s+");
            String fileName = split[1];
            try {
                sendFileMessage(fileName);
            } catch (IOException e) {
                e.getMessage();
            } catch (OutOfFileLengthLimitException e) {
                e.getMessage();
            }
            System.out.println(fileName + " 파일을 전송하였습니다.");
            return;
        }
        sendChatMessage(msg);
    }

    private void sendChatMessage(String msg) {
        int typeValue = MessageType.CHAT.getValue();
        byte[] body = msg.getBytes();
        try {
            sendMessage(typeValue, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileMessage(String fileName) throws IOException, OutOfFileLengthLimitException {
        int typeValue = MessageType.FILE.getValue();
        byte [] body = getBytesFromFile(new File(fileName));
        sendMessage(typeValue, body);
    }

    private void sendMessage(int typeValue, byte[] body) throws IOException {
        synchronized (dos) {
            //TODO. 이 부분 util로 뺄 수 있지 않을까?
            dos.writeInt(typeValue);
            dos.writeInt(body.length);
            dos.write(body);
            dos.flush();
        }
    }

    //TODO. Maven 적용하면 commons.apache 에서 toByteArray 사용하자.
    // 참고. http://commons.apache.org/proper/commons-io/apidocs/org/apache/commons/io/IOUtils.html#toByteArray%28java.io.InputStream%29
    public static byte[] getBytesFromFile(File file) throws IOException, OutOfFileLengthLimitException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new OutOfFileLengthLimitException("보낼 수 있는 파일의 크기를 초과하였습니다.");
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public void close() {
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}