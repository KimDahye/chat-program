package sophie.nioServer;

import jdk.management.resource.internal.inst.AsynchronousSocketChannelImplRMHooks;
import sophie.model.MessageType;
import sophie.utils.CastUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;

/**
 * Created by sophie on 2015. 12. 14..
 */
public class ChatEventHandler implements NioEventHandler{
    private static final int LENGTH_DATA_SIZE = 4;
    private static final int CONTENT_DATA_LIMIT = 1020; //Length data size 와 합하여 1024가 되도록
    private static final int CHAT_TYPE = MessageType.CHAT.getValue();
    AsynchronousSocketChannel channel;
    NioHandleMap handleMap;

    //TODO. cliet list 들고 있어야 한다. 싱글톤 레퍼런스.

    public int getType() {
        return CHAT_TYPE;
    }

    public void initialize(AsynchronousSocketChannel channel, NioHandleMap handleMap) {
        this.channel = channel;
        this.handleMap= handleMap;
    }

    public int getDataSize() {
        return LENGTH_DATA_SIZE + CONTENT_DATA_LIMIT;
    }

    public void completed(Integer result, ByteBuffer buffer) {
        if(result == -1) {
            try {
                System.out.println(channel + "channel이 종료되었습니다.");
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (result > 0) {
            buffer.flip();
            byte[] bufferAsArray = buffer.array();
            int contentLength = CastUtils.byteArrayToInt(Arrays.copyOfRange(bufferAsArray, 0, LENGTH_DATA_SIZE)); //TODO. 가독성 떨어지니 메소드로 분리해보자.
            String content = new String(Arrays.copyOfRange(bufferAsArray, LENGTH_DATA_SIZE, LENGTH_DATA_SIZE + contentLength));
            System.out.println(content); //클라이언트 메세지 저장용으로

            // broadcasting
            // TODO. for 문이 들어가야 한다.
            ByteBuffer writeBuffer = ByteBuffer.allocate(TYPE_SIZE+LENGTH_DATA_SIZE+contentLength).putInt(CHAT_TYPE).putInt(contentLength).put(content.getBytes());
            writeBuffer.rewind(); //이걸 하지 않으면 제대로 안간다.
            channel.write(writeBuffer);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(TYPE_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel, handleMap));
        }
    }

    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
