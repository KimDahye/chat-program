package sophie.nioServer.eventHandler;

import sophie.nioServer.NioHandleMap;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sophie on 2015. 12. 13..
 */
public interface NioEventHandler extends CompletionHandler<Integer, ByteBuffer>{
    int TYPE_SIZE = 4;

    int getType();
    void initialize(AsynchronousSocketChannel channel, NioHandleMap handleMap);
    int getDataSize();
}
