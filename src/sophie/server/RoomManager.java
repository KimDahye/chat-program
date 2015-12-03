package sophie.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sophie on 2015. 12. 2..
 */
public class RoomManager {
    private Room room;
    private ArrayList<ClientHandler> clients = null;
    private ExecutorService executor = null;
    private int roomCapacity = 0;
    private static final String END_MESSAGE = ".bye";

    RoomManager(int roomNumber, String roomName, int maxThreadNum) {
        this.room = new Room(roomNumber, roomName);
        this.clients = new ArrayList<ClientHandler>();
        this.executor = Executors.newFixedThreadPool(maxThreadNum); // TODO. RejectedExecutionHandler 등록 필요함.
        this.roomCapacity = maxThreadNum;
    }

    void addClient(ClientHandler clientHandler) {
        if (clients.size() < roomCapacity) {
            clients.add(clientHandler);
            clientHandler.send("Room" + room + "에 입장하셨습니다.");
            executor.execute(clientHandler);
            clientHandler.setRoomManager(this);
            return;
        }
        //TODO. room capacity 초과 에러 처리
    }

    void handle(ClientHandler clientHandler, String msg) {
        if (msg.equals(END_MESSAGE)) {
            clientHandler.sendBye(END_MESSAGE);
            remove(findClientIndex(clientHandler));
        } else {
            for (ClientHandler c : clients) {
                if (c != null) c.send(clientHandler.getNickname() + ": " + msg);
            }
        }
    }

    void remove(int index) {
        //synchronized 키워드... 필요 없겠지?
        ClientHandler toTerminate = clients.get(index);
        clients.remove(index);
        toTerminate.close();
        toTerminate.interrupt();
    }

    String getAvailableRoomInfo(){
        //방이 full이 아니면 room 정보를 리턴해준다.
        if(clients.size() < roomCapacity){
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
