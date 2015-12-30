import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;

/**
 * Created by sophie on 2015. 12. 17..
 */
abstract class AbstractNioEventHandler implements NioEventHandler {
    AsynchronousSocketChannel channel;
    EventHandlerFactory factory;
    int length;
    static final int HEADER_SIZE = 8; // IOUtils 에 중복있다.

    @Override
    public void initialize(AsynchronousSocketChannel channel, int length, EventHandlerFactory factory) {
        this.channel = channel;
        this.length = length;
        this.factory = factory;
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
        int position = buffer.position();
        buffer.flip();
        byte[] bufferAsArray = buffer.array();
        if(bufferAsArray.length < length) {
            throw new IllegalArgumentException("length가 더 크면 안됩니다."); //TODO. test case 만들자.
        }
        buffer.position(position);
        return Arrays.copyOfRange(bufferAsArray, 0, length);
    }
}
