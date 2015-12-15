package sophie.nioServer;

import sophie.nioServer.eventHandler.EventHandlerFactory;
import sophie.nioServer.eventHandler.NioEventHandler;
import sophie.utils.CastUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by sophie on 2015. 12. 13..
 * 헤더의 첫 4byte 를 읽고 나서 실행되는 handler
 * 여기서 타입 종류에 따라 다른 handler 등록해준다(demultiplex).
 */
public class Demultiplexer implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public Demultiplexer(AsynchronousSocketChannel channel) {
        this.channel = channel;
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
            int typeValue = CastUtils.byteArrayToInt(buffer.array());

            //팩토리 클래스를 통해 handler 인스턴스를 얻는다.
            NioEventHandler handler = EventHandlerFactory.getEventHandler(typeValue);
            handler.initialize(channel);

            // read 작업에 대해 handler 등록
            ByteBuffer newBuffer = ByteBuffer.allocate(handler.getDataSize());
            channel.read(newBuffer, newBuffer, handler);
        }
    }

    public void failed(Throwable exc, ByteBuffer buffer) {

    }
}
