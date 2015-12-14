package sophie.nioServer;

import sophie.model.GeneralMessage;
import sophie.model.Message;
import sophie.model.MessageType;
import sophie.utils.IOUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sophie on 2015. 12. 13..
 */
public class Dispatcher implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    private static final int HEADER_TYPE_SIZE = 4;
    private static final int HEADER_LENGTH_SIZE = 4;
    private static final String ASKING_MESSAGE_NICKNAME = "Enter your nickname: ";
    private static final int MESSAGE_LENGTH = ASKING_MESSAGE_NICKNAME.length();
    private final NioHandleMap handleMap;

    public Dispatcher(NioHandleMap handleMap) {
        this.handleMap = handleMap;
    }

    public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel listener) {
        listener.accept(listener, this);

        // USER_NAME 물어본다.
        Message message = new GeneralMessage(MessageType.USER_NAME, ASKING_MESSAGE_NICKNAME.getBytes());
        IOUtils.sendGeneralMessage(channel, message);

        // 응답에 대한 핸들러 등록
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_TYPE_SIZE);
        channel.read(buffer, buffer, new Demultiplexer(channel, handleMap));
    }

    public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {

    }
}
