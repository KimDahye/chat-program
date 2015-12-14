package sophie.nioServer;

import sophie.nioServer.eventHandler.NioEventHandler;
import sophie.utils.CastUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sophie on 2015. 12. 13..
 */
public class Demultiplexer implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;
    private NioHandleMap handleMap;

    public Demultiplexer(AsynchronousSocketChannel channel, NioHandleMap handleMap) {
        this.channel = channel;
        this.handleMap = handleMap;
    }

    public void completed(Integer result, ByteBuffer buffer) {
        if (result == -1) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (result > 0) {
            buffer.flip();
            int type = CastUtils.byteArrayToInt(buffer.array());
            NioEventHandler handler = handleMap.get(type);
            ByteBuffer newBuffer = ByteBuffer.allocate(handler.getDataSize());
            handler.initialize(channel, handleMap);
            channel.read(newBuffer, newBuffer, handler);
        }
    }

    public void failed(Throwable exc, ByteBuffer buffer) {

    }
}
