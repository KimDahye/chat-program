package sophie.nioServer.eventHandler;

import sophie.model.GeneralMessage;
import sophie.model.Message;
import sophie.model.MessageType;
import sophie.nioServer.Demultiplexer;
import sophie.nioServer.ProtocolString;
import sophie.nioServer.RoomListManager;
import sophie.utils.CastUtils;
import sophie.utils.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;

/**
 * Created by sophie on 2015. 12. 14..
 */
class RoomNameEventHandler extends AbstractNioEventHandler {
    private RoomListManager roomListManager = RoomListManager.getInstance();

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
            String roomName = new String(getContent(buffer, result));

            roomListManager.makeRoom(roomName, channel);
            GeneralMessage message = new GeneralMessage(MessageType.CHAT_START, ProtocolString.INFO_MESSAGE_ROOM_PARTICIPATE.getBytes());
            IOUtils.sendGeneralMessage(channel, message);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(HEADER_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
