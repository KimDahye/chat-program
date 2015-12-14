package sophie.utils;

import sophie.client.exception.OutOfFileLengthLimitException;
import sophie.model.Message;
import sophie.model.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by sophie on 2015. 12. 10..
 */
public class IOUtils {
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
        dis.read(body, 0, length);
        return new Message(MessageType.fromInteger(type), body);
    }

    //NIO 관련 utils
    public static void sendGeneralMessage(AsynchronousSocketChannel channel, int bufferSize, int type, int contentLength, byte[] content) {
        ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize).putInt(type).putInt(contentLength).put(content);
        writeBuffer.rewind(); //이걸 하지 않으면 제대로 안간다.
        channel.write(writeBuffer);
    }

    public static void sendGeneralMessage(AsynchronousSocketChannel channel, Message message) {
        int bodyLength = message.getBodyLength();
        int bufferSize = message.getHeaderLength() + bodyLength;
        int type = message.getType().value();

        ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize).putInt(type).putInt(bodyLength).put(message.getBody());
        writeBuffer.rewind(); //이걸 하지 않으면 제대로 안간다.
        channel.write(writeBuffer);
    }

}
