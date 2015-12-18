package sophie.nioServer;

import sophie.model.FileMessage;
import sophie.model.GeneralMessage;
import sophie.model.MessageType;
import sophie.utils.IOUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;

/**
 * Created by sophie on 2015. 12. 2..
 */
class RoomManager {
    private Room room;
    private ArrayList<AsynchronousSocketChannel> clients = null;
    private int roomCapacity;

    RoomManager(int roomNumber, String roomName, int roomCapacity) {
        this.room = new Room(roomNumber, roomName);
        this.clients = new ArrayList<AsynchronousSocketChannel>();
        this.roomCapacity = roomCapacity;
    }

    void addClient(AsynchronousSocketChannel channel) {
        if (clients.size() < roomCapacity) {
            clients.add(channel);
            return;
        }
        //TODO. room capacity 초과 에러 처리
    }

    String getAvailableRoomInfo() {
        //방이 full이 아니면 room 정보를 리턴해준다.
        if (clients.size() < roomCapacity) {
            return room.toString();
        }
        return null;
    }

    public void broadcast(String content) {
        for(AsynchronousSocketChannel c : clients) {
            IOUtils.sendGeneralMessage(c, new GeneralMessage(MessageType.CHAT, content.getBytes()));
        }
    }

    public void broadcastFileWithoutHeader(AsynchronousSocketChannel channel, ByteBuffer buffer) {
        for(AsynchronousSocketChannel c : clients) {
            if(c != channel) {
                IOUtils.sendFileMessageWithoutHeader(c, buffer);
            }
        }
    }

    public void broadcastFileWithHeader(AsynchronousSocketChannel channel, int length, ByteBuffer buffer) {
        for(AsynchronousSocketChannel c : clients) {
            if(c != channel) {
                IOUtils.sendFileMessageWitHeader(c, length, buffer);
            }
        }
    }
}
