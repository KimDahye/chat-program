package sophie.nioServer;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;

/**
 * Created by sophie on 2015. 12. 2..
 */
public class RoomListManager {
    private static final RoomListManager instance = new RoomListManager(); //singleton
    private HashMap<Integer, RoomManager> roomList = new HashMap<Integer, RoomManager>();
    private static final int MAX_THREAD_NUM = 100; // ClientHandler 쓰레드 풀의 max number
    private static int roomNumber = 0;

    // proactor 패턴때문에 새로 생긴 필드
    private static final HashMap<AsynchronousSocketChannel, ClientInfo> clientInfoList = new HashMap<AsynchronousSocketChannel, ClientInfo>();

    private RoomListManager() {
        //singleton
    }

    public static RoomListManager getInstance() {
        return instance;
    }

    public String getAvailableRoomInfoList() {
        // capacity가 MAX_THREAD_NUM 미만 인것만 알려준다.
        String result = "";
        for(Integer key : roomList.keySet()) {
            result = result + roomList.get(key).getAvailableRoomInfo();
        }

        return result;
    }

    public boolean isExistentRoomNumber(int num) {
        //room number 중 있는 number 인지
        return roomList.containsKey(num);
    }

    public synchronized void makeRoom(String roomName, AsynchronousSocketChannel channel) {
        // 새로운 RoomManager 만들고, - unique 번호 make 해야함. autoIncrementRoomNumber 이용하자. -
        // roomList 에 add 해주고.
        // 거기에 add client (clientHandler)
        Integer roomNum = autoIncrementRoomNumber();
        RoomManager newRoom = new RoomManager(roomNum, roomName, MAX_THREAD_NUM);
        roomList.put(roomNum, newRoom);
        newRoom.addClient(channel);
        saveClientRoomNumber(channel, roomNum);
    }

    public void participateRoomAt(int roomNum, AsynchronousSocketChannel channel) {
        roomList.get(roomNum).addClient(channel);
        saveClientRoomNumber(channel, roomNum);
    }

    public boolean isRoomListEmpty(){
        return roomList.isEmpty();
    }

    public void saveClientName(AsynchronousSocketChannel channel, String name){
        clientInfoList.put(channel, new ClientInfo(name));
    }

    private void saveClientRoomNumber(AsynchronousSocketChannel channel, int roomNum) {
        ClientInfo clientInfo = clientInfoList.get(channel);
        clientInfo.setRoomNum(roomNum);
    }

    private int autoIncrementRoomNumber() {
        return ++roomNumber;
    }

    public String getClientName(AsynchronousSocketChannel channel) {
        return clientInfoList.get(channel).getName();
    }

    public void broadcast(AsynchronousSocketChannel channel, String content) {
        int roomNum = clientInfoList.get(channel).getRoomNum();
        RoomManager room = roomList.get(roomNum);
        room.broadcast(content);
    }

    //remove room 은 외부 태스크로 주기적으로 방에 애들 아무도 없으면 없애는 것으로? 고민해보자. RoomManager에서 자기 없애라고 할 것인지...
}
