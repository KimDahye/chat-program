import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by sophie on 2015. 12. 14..
 */
class UserNameEventHandler extends AbstractNioEventHandler {
    private RoomListManager roomListManager = RoomListManager.getInstance();

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if(result == -1) try {
            System.out.println(channel + "channel이 종료되었습니다.");
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        else if (result > 0) {
            String userName = new String(getContent(buffer, result));

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
            ByteBuffer newBuffer = ByteBuffer.allocate(HEADER_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}