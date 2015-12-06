package sophie.server;

import sophie.model.Message;
import sophie.model.MessageType;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sophie on 2015. 12. 2..
 */
class RoomManager {
    private Room room;
    private ArrayList<ClientHandler> clients = null;
    private ExecutorService executor = null;
    private int roomCapacity = 0;

    RoomManager(int roomNumber, String roomName, int maxThreadNum) {
        this.room = new Room(roomNumber, roomName);
        this.clients = new ArrayList<ClientHandler>();
        this.executor = Executors.newFixedThreadPool(maxThreadNum); // TODO. RejectedExecutionHandler 등록 필요함.
        this.roomCapacity = maxThreadNum;
    }

    void addClient(ClientHandler clientHandler) {
        if (clients.size() < roomCapacity) {
            clients.add(clientHandler);
            String msg = "Room" + room + "에 입장하셨습니다.";
            clientHandler.sendMessage(new Message(MessageType.CHAT, msg.getBytes()));
            executor.execute(clientHandler);
            clientHandler.setRoomManager(this);
            return;
        }
        //TODO. room capacity 초과 에러 처리
    }

    void handle(ClientHandler clientHandler, MessageType contentType, byte[] body) {
        if (contentType == contentType.CHAT) {
            byte[] bodyWithNickname = clientHandler.getBodyWithNickname(body);
            for (ClientHandler c : clients) {
                if (c != null) c.sendMessage(new Message(contentType, bodyWithNickname));
            }
        } else if (contentType == contentType.FILE) {
            for (ClientHandler c : clients) {
                if (c != clientHandler) c.sendMessage(new Message(contentType, body));
            }
        }
    }

    void remove(ClientHandler clientHandler) {
        int index = findClientIndex(clientHandler);
        if(index == -1) return;
        clients.remove(index);
        String msg = "---- " + clientHandler.getNickname() + " 님이 방을 나갔습니다. ----";
        for(ClientHandler c : clients) {
            c.sendMessage(new Message(MessageType.CHAT, msg.getBytes()));
        }
    }

    String getAvailableRoomInfo() {
        //방이 full이 아니면 room 정보를 리턴해준다.
        if (clients.size() < roomCapacity) {
            return room.toString();
        }
        return null;
    }

    private int findClientIndex(ClientHandler clientHandler) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i) == clientHandler) return i;
        }
        return -1;
    }

    public boolean isYourRoomNumber(int num) {
        return room.getNumber() == num;
    }
}
