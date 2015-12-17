package sophie.nioServer.eventHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sophie on 2015. 12. 13..
 */
public interface NioEventHandler extends CompletionHandler<Integer, ByteBuffer> {
    void initialize(AsynchronousSocketChannel channel, int length);

    int getDataSize();
}
