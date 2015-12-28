import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by sophie on 2015. 12. 17..
 */
public class WriteEventHandler implements NioEventHandler {
    private int length = 0;
    private int currentLength = 0;
    private AsynchronousSocketChannel channel;

    public WriteEventHandler(AsynchronousSocketChannel channel, int length) {
        this.channel = channel;
        this.length = length;
    }

    @Override
    public void initialize(AsynchronousSocketChannel channel, int length) {
        this.channel = channel;
        this.length = length;
    }

    @Override
    public int getDataSize() {
        return length;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        currentLength = currentLength + result;
        if (currentLength < length) {
            channel.write(buffer, buffer, this);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }

}
