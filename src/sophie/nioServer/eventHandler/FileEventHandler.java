package sophie.nioServer.eventHandler;

import sophie.nioServer.Demultiplexer;
import sophie.nioServer.RoomListManager;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by sophie on 2015. 12. 14..
 */
class FileEventHandler extends AbstractNioEventHandler {
    private RoomListManager roomListManager = RoomListManager.getInstance();
    private int currentLength = 0;

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
            byte[] content = getContent(buffer, result);
            if(currentLength == 0) {
                // broadcasting
                roomListManager.broadcastFile(channel, content);
            } else {
                roomListManager.broadcastFileWithoutHeader(channel, content);
            }

            currentLength = currentLength + result;
            if(currentLength == length) {
                // 다시 헤더 읽기 준비
                ByteBuffer newBuffer = ByteBuffer.allocate(HEADER_SIZE);
                channel.read(newBuffer, newBuffer, new Demultiplexer(channel));
            } else  {
                // 남은 파일 이어서 읽기 준비
                // TODO. 버퍼 재활용... 잘 될까?
                ByteBuffer newBuffer = ByteBuffer.allocate(length - currentLength);
                channel.read(newBuffer, newBuffer, this);
            }
        }
    }

    //TODO
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
