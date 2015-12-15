package sophie.nioServer.eventHandler;

import sophie.model.GeneralMessage;
import sophie.model.Message;
import sophie.model.MessageType;
import sophie.nioServer.Demultiplexer;
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
class RoomNumberEventHandler implements NioEventHandler{
    private static final String INFO_MESSAGE_INVALID_ROOM_NUMBER = "올바른 방 번호가 아닙니다.";
    private static final String INFO_MESSAGE_ROOM_PARTICIPATE = "------- 방에 참가하였습니다 ---------";
    private static final String ASKING_MESSAGE_ROOM_NUMBER = "Enter room number if you want to participate: ";

    private static final int LENGTH_DATA_SIZE = 4;
    private static final int CONTENT_DATA_LIMIT = 1020;

    AsynchronousSocketChannel channel;
    RoomListManager roomListManager = RoomListManager.getInstance();

    @Override
    public void initialize(AsynchronousSocketChannel channel) {
        this.channel = channel;
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
            int roomNumber = CastUtils.byteArrayToInt(Arrays.copyOfRange(bufferAsArray, LENGTH_DATA_SIZE, LENGTH_DATA_SIZE + contentLength));

            boolean isPassable = roomListManager.isExistentRoomNumber(roomNumber);

            Message message;
            if(isPassable) {
                roomListManager.participateRoomAt(roomNumber, channel);
                message = new GeneralMessage(MessageType.INFO, INFO_MESSAGE_ROOM_PARTICIPATE.getBytes());
            } else {
                String content = INFO_MESSAGE_INVALID_ROOM_NUMBER + '\n' + ASKING_MESSAGE_ROOM_NUMBER;
                message = new GeneralMessage(MessageType.ROOM_NUM, content.getBytes());
            }
            IOUtils.sendGeneralMessage(channel, message);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(TYPE_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
