package sophie.nioServer.eventHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by sophie on 2015. 12. 14..
 */
class InfoEventHandler extends AbstractNioEventHandler {
    //TODO. 아래 구현해야하지만 일단 쓰지 않으니 보류
    @Override
    public void completed(Integer result, ByteBuffer attachment) {

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
