import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by sophie on 2015. 12. 14..
 */
class RoomMakingEventHandler extends AbstractNioEventHandler {

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
            String answer = new String(getContent(buffer, result));

            GeneralMessage message = null;
            if(answer.equals(ProtocolString.CLIENT_MESSAGE_WANT_TO_MAKE_ROOM)) {
                // 만들 방 이름 묻기
                message = new GeneralMessage(MessageType.ROOM_NAME, ProtocolString.ASKING_MESSAGE_ROOM_NAME.getBytes());
            } else {
                //참가할 방 번호 묻기
                message = new GeneralMessage(MessageType.ROOM_NUM, ProtocolString.ASKING_MESSAGE_ROOM_NUMBER.getBytes());
            }
            IOUtils.sendGeneralMessage(channel, message);

            // 다시 읽기 준비
            ByteBuffer newBuffer = ByteBuffer.allocate(HEADER_SIZE);
            channel.read(newBuffer, newBuffer, new Demultiplexer(channel, factory));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
