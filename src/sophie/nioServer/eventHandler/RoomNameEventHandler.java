package sophie.nioServer.eventHandler;

import sophie.model.MessageType;
import sophie.nioServer.Demultiplexer;
import sophie.nioServer.NioHandleMap;
import sophie.utils.CastUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;

/**
 * Created by sophie on 2015. 12. 14..
 */
public class RoomNameEventHandler implements NioEventHandler {
    private static final MessageType TYPE = MessageType.ROOM_NAME;
    private static final int TYPE_AS_INT = TYPE.getValue();
    private static final int LENGTH_DATA_SIZE = 4;
    private static final int CONTENT_DATA_LIMIT = 1020; //Length data size 와 합하여 1024가 되도록

    AsynchronousSocketChannel channel;
    NioHandleMap handleMap;

    @Override
    public int getType() {
        return TYPE_AS_INT;
    }

    @Override
    public void initialize(AsynchronousSocketChannel channel, NioHandleMap handleMap) {
        this.channel = channel;
        this.handleMap = handleMap;
    }

    @Override
    public int getDataSize() {
        return TYPE_SIZE + LENGTH_DATA_SIZE + CONTENT_DATA_LIMIT;
    }

    @Override
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
            String roomName = new String(Arrays.copyOfRange(bufferAsArray, LENGTH_DATA_SIZE, LENGTH_DATA_SIZE + contentLength));

            roomListManager.makeRoom(roomName, clientHandler);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(TYPE_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel, handleMap));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
