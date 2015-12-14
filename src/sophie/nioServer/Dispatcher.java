package sophie.nioServer;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sophie on 2015. 12. 13..
 */
public class Dispatcher implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    private static final int HEADER_SIZE = 4;
    private final NioHandleMap handleMap;

    public Dispatcher(NioHandleMap handleMap) {
        this.handleMap = handleMap;
    }

    public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel listener) {
        listener.accept(listener, this);
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        channel.read(buffer, buffer, new Demultiplexer(channel, handleMap));
    }

    public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {

    }
}
