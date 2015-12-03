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
    private static final String INFO_MESSAGE_NO_ROOM = "There is no room. You should make the first room!";
    private static final String ASKING_MESSAGE_NICKNAME = "Enter your nickname: ";

    RoomSelector(ClientHandler clientHandler, RoomListManager roomListManager) {
        this.clientHandler = clientHandler;
        this.roomListManager = roomListManager;
    }

    @Override
    public void run() {
        //ClientHandler에 client 의 닉네임 넣는 기능 넣기
        String nickname = askNickname();
        clientHandler.setNickname(nickname);

        //방이 없을 때
        if(isRoomListEmpty()){
            sendEmptyRoomInfo();
            makeRoom();
            return;
        }

        // 방이 있을 때
        sendRoomInfo();
        if(askWannaMakeRoom(clientHandler)){
            makeRoom();
            return;
        }
        int roomNum = askWhichRoom();
        roomListManager.participateRoomAt(roomNum, clientHandler);
    }

    private String askNickname(){
        clientHandler.send(ASKING_MESSAGE_NICKNAME);
        String msg = clientHandler.receive();
        return msg;
    }

    private boolean askWannaMakeRoom(ClientHandler clientHandler) {
        clientHandler.send(ASKING_MESSAGE_MAKING);
        String msg = clientHandler.receive();
        return msg.equals(CLIENT_MESSAGE_WANT_TO_MAKE_ROOM);
    }

    private String askRoomName() {
        clientHandler.send(ASKING_MESSAGE_ROOM_NAME);
        return clientHandler.receive();
    }

    private int askWhichRoom() {
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

    private void sendRoomInfo() {
        clientHandler.send("room info: " + roomListManager.getAvailableRoomInfoList());
    }

    private void sendEmptyRoomInfo() {
        clientHandler.send(INFO_MESSAGE_NO_ROOM);
    }

    private void makeRoom() {
        String roomName = askRoomName();
        roomListManager.makeRoom(roomName, clientHandler);
    }

    private boolean isRoomListEmpty(){
        return roomListManager.isRoomListEmpty();
    }
}
