package sophie.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sophie on 2015. 12. 2..
 */
public class RoomListManager {
    private HashMap<Integer, RoomManager> roomList = new HashMap<Integer, RoomManager>();
    private static final RoomListManager instance = new RoomListManager(); //singleton
    private static final int MAX_THREAD_NUM = 100; // ClientHandler 쓰레드 풀의 max number
    private static int roomNumber = 0;

    private RoomListManager() {

    };

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
        for(Integer key : roomList.keySet() ){
            if(roomList.get(key).isYourRoomNumber(num)){
                return true;
            }
        }
        return false;
    }

    public void makeRoom(String roomName, ClientHandler clientHandler) {
        // 새로운 RoomManager 만들고, - unique 번호 make 해야함. autoIncrementRoomNumber 이용하자. -
        // roomList 에 add 해주고.
        // 거기에 add client (clientHandler)
        Integer roomNum = autoIncrementRoomNumber();
        RoomManager newRoom = new RoomManager(roomNum, roomName, MAX_THREAD_NUM);
        roomList.put(roomNum, newRoom);
        newRoom.addClient(clientHandler);
    }

    public void participateRoomAt(int roomNum, ClientHandler clientHandler) {
        //O(1)으로 찾으면 좋겠는데? => ArrayList가 아닌 HashMap을 쓰자! => 완료
        roomList.get(roomNum).addClient(clientHandler);
    }

    public boolean isRoomListEmpty(){
        return roomList.isEmpty();
    }

    private int autoIncrementRoomNumber() {
        //singleton이니까 synchronized 붙일 필요 없다.
        return ++roomNumber;
    }


    //remove room 은 외부 태스크로 주기적으로 방에 애들 아무도 없으면 없애는 것으로? 고민해보자. RoomManager에서 자기 없애라고 할 것인지...
}
