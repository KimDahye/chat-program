package sophie.nioServer.eventHandler;

import sophie.nioServer.Demultiplexer;
import sophie.nioServer.RoomListManager;
import sophie.utils.CastUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;

/**
 * Created by sophie on 2015. 12. 14..
 */
class FileEventHandler implements NioEventHandler {
    private static final int LENGTH_DATA_SIZE = 4; //TODO. 12로 바뀌어야 함.
    private static final int CONTENT_DATA_LIMIT = 2040; // Header와 body 합해서 2048이 된다.
    private AsynchronousSocketChannel channel;
    private RoomListManager roomListManager = RoomListManager.getInstance();

    public void initialize(AsynchronousSocketChannel channel)
    {
        this.channel = channel;
    }

    public int getDataSize() {
        return LENGTH_DATA_SIZE + CONTENT_DATA_LIMIT;
    }

    public void completed(Integer result, ByteBuffer buffer) {
        //서버에 저장하지 않고 바로 방에 있는 클라이언트에게 전달해준다. - 받은 것 바로 전달해주면 됨.
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
            byte[] content = Arrays.copyOfRange(bufferAsArray, LENGTH_DATA_SIZE, LENGTH_DATA_SIZE + contentLength);

            // broadcasting
            roomListManager.broadcastFile(channel, content);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(TYPE_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel));
        }
    }

    //TODO
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
