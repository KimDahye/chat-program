package sophie.nioServer;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sophie on 2015. 12. 13..
 */
public class Disaptacher implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    private int DATA_SIZE = 1024;

    public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel listener) {
        listener.accept(listener, this);
        ByteBuffer buffer = ByteBuffer.allocate(DATA_SIZE);
        channel.read(buffer, buffer, new EchoHandler(channel));
    }

    public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {

    }
}
