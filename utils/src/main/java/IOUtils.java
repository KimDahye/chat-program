import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sophie on 2015. 12. 10..
 */
public class IOUtils {
    private static final int HEADER_SIZE = 8;


    //classic IO 관련 utils
    public static void sendMessage(DataOutputStream dos, int typeValue, byte[] body) throws IOException {
        dos.writeInt(typeValue);
        dos.writeInt(body.length);

        dos.write(body);
        dos.flush();
    }

    public static void sendChatMessage(DataOutputStream dos, String msg) throws IOException {
        int typeValue = MessageType.CHAT.getValue();
        byte[] body = msg.getBytes();
        sendMessage(dos, typeValue, body);
    }

    public static void sendFileMessage(DataOutputStream dos, String fileName) throws IOException, OutOfFileLengthLimitException {
        int typeValue = MessageType.FILE.getValue();
        byte[] body = FileUtils.getBytesFromFile(new File(fileName));
        sendMessage(dos, typeValue, body);
    }

    public static Message getMessage(DataInputStream dis) throws IOException {
        //header 분석
        int type = dis.readInt();
        int length = dis.readInt();

        //body
        byte[] body = new byte[length];
        int curLength = 0;
        while(curLength < length) {
            curLength = curLength + dis.read(body, curLength, length-curLength);
        }
        return new GeneralMessage(MessageType.fromInteger(type), body);
    }

    public static void sendGeneralMessage(DataOutputStream dos, Message message) throws IOException {
        int typeValue = message.getMessageType().getValue();
        byte[] body = message.getBody();

        dos.writeInt(typeValue);
        dos.writeInt(body.length);
        dos.write(body);
        dos.flush();
    }

    public static void sendGeneralMessage(AsynchronousSocketChannel channel, GeneralMessage message) {
        int bodyLength = message.getBodyLength();
        int bufferSize = message.getHeaderLength() + bodyLength;
        int typeValue = message.getMessageType().getValue();

        ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize).putInt(typeValue).putInt(bodyLength).put(message.getBody());
        writeBuffer.rewind(); //이걸 하지 않으면 제대로 안간다.
        channel.write(writeBuffer);
    }

    public static void sendFileMessageWithoutHeader(AsynchronousSocketChannel channel, ByteBuffer buffer) {
        buffer.rewind(); //이걸 하지 않으면 제대로 안간다.
        Future<Integer> w = channel.write(buffer);
        try {
            w.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void sendHeader(AsynchronousSocketChannel channel, int type, int length) {
        int headerSize = HEADER_SIZE;
        ByteBuffer writeBuffer = ByteBuffer.allocate(headerSize);
        writeBuffer.putInt(type).putInt(length);
        writeBuffer.rewind();
        channel.write(writeBuffer);
    }

    public static void sendFileMessageWitHeader(AsynchronousSocketChannel channel, int length, ByteBuffer buffer) {
        sendHeader(channel, MessageType.FILE.getValue(), length);
        sendFileMessageWithoutHeader(channel, buffer);
    }
}
