package sophie.server;

/**
 * Created by sophie on 2015. 12. 2..
 */
public class RoomSelector implements Runnable {
    // 클래스 이름이 before participate room 이런 거였으면 좋겠는데? room selecting 이외에도 닉네임 결정 등 다양한 것들 할 수 있는듯?
    private ClientHandler clientHandler;
    private RoomListManager roomListManager;
    private static final String ASKING_MESSAGE_MAKING = "Do you wanna make room? (yes/any key)";
    private static final String ASKING_MESSAGE_ROOM_NAME = "Type the room name you want to make: ";
    private static final String ASKING_MESSAGE_ROOM_NUMBER = "Enter room number if you want to participate: ";
    private static final String CLIENT_MESSAGE_WANT_TO_MAKE_ROOM = "yes";

    RoomSelector(ClientHandler clientHandler, RoomListManager roomListManager) {
        this.clientHandler = clientHandler;
        this.roomListManager = roomListManager;
    }

    @Override
    public void run() {
        //ClientHandler에 client 의 닉네임 넣는 기능 넣기

        if(doYouWannaMake(clientHandler)){
            String roomName = decideRoomName();
            roomListManager.makeRoom(roomName, clientHandler);
            return;
        }

        int roomNum = whichRoom();
        roomListManager.participateRoomAt(roomNum, clientHandler);
    }

    private boolean doYouWannaMake(ClientHandler clientHandler) {
        clientHandler.send(ASKING_MESSAGE_MAKING + roomListManager.getAvailableRoomInfoList());
        String msg = clientHandler.receive();
        return msg.equals(CLIENT_MESSAGE_WANT_TO_MAKE_ROOM);
    }

    private String decideRoomName() {
        clientHandler.send(ASKING_MESSAGE_ROOM_NAME);
        return clientHandler.receive();
    }

    private int whichRoom() {
        boolean isPassable;
        int roomNumber = -1;
        do {
            clientHandler.send(ASKING_MESSAGE_ROOM_NUMBER);
            String msg = clientHandler.receive();
            try {
                roomNumber = Integer.parseInt(msg);
                isPassable = roomListManager.isExistentRoomNumber(roomNumber);
            }catch (NumberFormatException e){
                isPassable = false;
            }
        } while(!isPassable);
        return roomNumber;
    }
}
