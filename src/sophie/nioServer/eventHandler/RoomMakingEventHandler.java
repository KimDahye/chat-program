package sophie.nioServer.eventHandler;

import sophie.model.GeneralMessage;
import sophie.model.Message;
import sophie.model.MessageType;
import sophie.nioServer.Demultiplexer;
import sophie.utils.CastUtils;
import sophie.utils.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;

/**
 * Created by sophie on 2015. 12. 14..
 */
class RoomMakingEventHandler implements NioEventHandler {
    private static final String CLIENT_MESSAGE_WANT_TO_MAKE_ROOM = "yes";
    private static final String ASKING_MESSAGE_ROOM_NAME = "Type the room name you want to make: ";
    private static final String ASKING_MESSAGE_ROOM_NUMBER = "Enter room number if you want to participate: ";

    private static final int LENGTH_DATA_SIZE = 4;
    private static final int CONTENT_DATA_LIMIT = 1020; //Length data size 와 합하여 1024가 되도록
    private AsynchronousSocketChannel channel;

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
            String answer = new String(Arrays.copyOfRange(bufferAsArray, LENGTH_DATA_SIZE, LENGTH_DATA_SIZE + contentLength));

            GeneralMessage message = null;
            if(answer.equals(CLIENT_MESSAGE_WANT_TO_MAKE_ROOM)) {
                // 만들 방 이름 묻기
                message = new GeneralMessage(MessageType.ROOM_NAME, ASKING_MESSAGE_ROOM_NAME.getBytes());
            } else {
                //참가할 방 번호 묻기
                message = new GeneralMessage(MessageType.ROOM_NUM, ASKING_MESSAGE_ROOM_NUMBER.getBytes());
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
