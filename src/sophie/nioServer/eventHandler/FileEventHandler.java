package sophie.nioServer.eventHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by sophie on 2015. 12. 14..
 */
class FileEventHandler implements NioEventHandler {
    private static final int LENGTH_DATA_SIZE = 12;
    private static final int CONTENT_DATA_LIMIT = 2036; // Length data와 합해서 2048이 된다.
    AsynchronousSocketChannel channel;

    public void initialize(AsynchronousSocketChannel channel)
    {
        this.channel = channel;
    }

    public int getDataSize() {
        return LENGTH_DATA_SIZE + CONTENT_DATA_LIMIT;
    }

    //TODO
    public void completed(Integer result, ByteBuffer attachment) {

    }

    //TODO
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
