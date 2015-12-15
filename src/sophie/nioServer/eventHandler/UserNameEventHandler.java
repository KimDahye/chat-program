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
class UserNameEventHandler implements NioEventHandler{
    private static final int LENGTH_DATA_SIZE = 4;
    private static final int CONTENT_DATA_LIMIT = 1020; //Length data size 와 합하여 1024가 되도록
    private AsynchronousSocketChannel channel;
    private RoomListManager roomListManager = RoomListManager.getInstance();

    @Override
    public void initialize(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public int getDataSize() {
        return LENGTH_DATA_SIZE + CONTENT_DATA_LIMIT;
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
            String userName = new String(Arrays.copyOfRange(bufferAsArray, LENGTH_DATA_SIZE, LENGTH_DATA_SIZE + contentLength));

            //user name save
            roomListManager.saveClientName(channel, userName);

            GeneralMessage infoMessage;
            GeneralMessage askingMessage;
            if(roomListManager.isRoomListEmpty()) {
                //방이 없다는 정보
                infoMessage = new GeneralMessage(MessageType.INFO, ProtocolString.INFO_MESSAGE_NO_ROOM.getBytes());
                //새로 만들 방 이름을 묻는 질문
                askingMessage = new GeneralMessage(MessageType.ROOM_NAME, ProtocolString.ASKING_MESSAGE_ROOM_NAME.getBytes());
            } else {
                // 이미 만들어진 방 정보
                infoMessage = new GeneralMessage(MessageType.INFO, roomListManager.getAvailableRoomInfoList().getBytes());
                // 방 만들 건지 묻는 질문
                askingMessage = new GeneralMessage(MessageType.ROOM_MAKING, ProtocolString.ASKING_MESSAGE_MAKING.getBytes());
            }
            IOUtils.sendGeneralMessage(channel, infoMessage);
            IOUtils.sendGeneralMessage(channel, askingMessage);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(TYPE_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}