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
class ChatEventHandler implements NioEventHandler {
    private static final int LENGTH_DATA_SIZE = 4;
    private static final int CONTENT_DATA_LIMIT = 1020; //Length data size 와 합하여 1024가 되도록
    private AsynchronousSocketChannel channel;
    private RoomListManager roomListManager = RoomListManager.getInstance();

    //TODO. cliet list 들고 있어야 한다. 싱글톤 레퍼런스.

    public void initialize(AsynchronousSocketChannel channel) {
        this.channel = channel;
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
            content = roomListManager.getClientName(channel) + ": " + content;
            roomListManager.broadcast(channel, content);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(TYPE_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel));
        }
    }

    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
