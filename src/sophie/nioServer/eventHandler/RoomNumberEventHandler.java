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
class RoomNumberEventHandler implements NioEventHandler{
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
            String roomNumString = new String(Arrays.copyOfRange(bufferAsArray, LENGTH_DATA_SIZE, LENGTH_DATA_SIZE + contentLength));

            //String을 integer로 parsing
            int roomNumber = 0;
            boolean isPassable;
            try {
                roomNumber = Integer.parseInt(roomNumString);
                isPassable = roomListManager.isExistentRoomNumber(roomNumber);
            }
            catch (NumberFormatException e){
                isPassable = false;
            }

            //isPassable 값에 따라 분기
            Message message;
            if(isPassable) {
                roomListManager.participateRoomAt(roomNumber, channel);
                message = new GeneralMessage(MessageType.CHAT_START, ProtocolString.INFO_MESSAGE_ROOM_PARTICIPATE.getBytes());
            } else {
                String content = ProtocolString.INFO_MESSAGE_INVALID_ROOM_NUMBER + '\n' + ProtocolString.ASKING_MESSAGE_ROOM_NUMBER;
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
