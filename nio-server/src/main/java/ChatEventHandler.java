import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by sophie on 2015. 12. 14..
 */
class ChatEventHandler extends AbstractNioEventHandler {
    private RoomListManager roomListManager;

    public void setRoomListManager(RoomListManager roomListManager) {
        System.out.println("DI with setter");
        this.roomListManager = roomListManager;
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
            String content = new String(getContent(buffer, result));
            System.out.println(content);

            // broadcasting
            // TODO. for 문이 들어가야 한다.
            content = roomListManager.getClientName(channel) + ": " + content;
            roomListManager.broadcast(channel, content);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(HEADER_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel));
        }
    }

    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}