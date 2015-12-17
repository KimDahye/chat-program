package sophie.nioServer.eventHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;

/**
 * Created by sophie on 2015. 12. 17..
 */
public abstract class AbstractNioEventHandler implements NioEventHandler {
    AsynchronousSocketChannel channel;
    int length;
    static final int HEADER_SIZE = 8;

    @Override
    public void initialize(AsynchronousSocketChannel channel, int length) {
        this.channel = channel;
        this.length = length;
    }

    @Override
    public int getDataSize() {
        return length;
    }

    /** public 함수*/
    public static int getHeaderSize() {
        return HEADER_SIZE;
    }

    /** protected 함수*/
    byte[] getContent(ByteBuffer buffer, int length) {
        buffer.flip();
        byte[] bufferAsArray = buffer.array();
        if(bufferAsArray.length < length) {
            throw new IllegalArgumentException("length가 더 크면 안됩니다."); //TODO. test case 만들자.
        }
        return Arrays.copyOfRange(bufferAsArray, 0, length);
    }
}
