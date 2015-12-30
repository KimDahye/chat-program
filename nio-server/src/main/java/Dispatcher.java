import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sophie on 2015. 12. 13..
 */
class Dispatcher implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    EventHandlerFactory factory;

    Dispatcher(EventHandlerFactory factory) {
        this.factory = factory;
    }

    public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel listener) {
        listener.accept(listener, this);

        // USER_NAME 물어본다.
        GeneralMessage message = new GeneralMessage(MessageType.USER_NAME, ProtocolString.ASKING_MESSAGE_USERNAME.getBytes());
        IOUtils.sendGeneralMessage(channel, message);

        // 응답에 대한 핸들러 등록
        ByteBuffer buffer = ByteBuffer.allocate(AbstractNioEventHandler.getHeaderSize());
        channel.read(buffer, buffer, new Demultiplexer(channel, factory));
    }

    public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {

    }
}
