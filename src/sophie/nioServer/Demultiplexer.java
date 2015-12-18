package sophie.nioServer;

import sophie.nioServer.eventHandler.AbstractNioEventHandler;
import sophie.nioServer.eventHandler.EventHandlerFactory;
import sophie.nioServer.eventHandler.NioEventHandler;
import sophie.utils.CastUtils;
import sophie.utils.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;

/**
 * Created by sophie on 2015. 12. 13..
 * 헤더의 첫 4byte 를 읽고 나서 실행되는 handler
 * 여기서 타입 종류에 따라 다른 handler 등록해준다(demultiplex).
 */
public class Demultiplexer implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;
    private static final int TYPE_HEADER_SIZE = 4;
    private int current=0;

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
            current = current + result;
            if(result < AbstractNioEventHandler.getHeaderSize()) {
                channel.read(buffer, buffer, this);
                return;
            }

            buffer.flip();
            byte[] bufferAsArray = buffer.array();

            //TODO. 사실 result가 8 임을 확인하고 아래를 진행해야 하는데 그냥 함....
            int typeValue = CastUtils.byteArrayToInt(Arrays.copyOfRange(bufferAsArray, 0, TYPE_HEADER_SIZE));
            int length = CastUtils.byteArrayToInt(Arrays.copyOfRange(bufferAsArray, TYPE_HEADER_SIZE, AbstractNioEventHandler.getHeaderSize()));

            //팩토리 클래스를 통해 handler 인스턴스를 얻는다.
            NioEventHandler handler = EventHandlerFactory.getEventHandler(typeValue);
            if (handler != null) {
                handler.initialize(channel, length);
            }

            // read 작업에 대해 handler 등록
            ByteBuffer newBuffer = ByteBuffer.allocate(handler.getDataSize());
            channel.read(newBuffer, newBuffer, handler);
        }
    }

    public void failed(Throwable exc, ByteBuffer buffer) {

    }
}
