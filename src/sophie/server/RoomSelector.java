package sophie.server;

import sophie.model.GeneralMessage;
import sophie.model.Message;
import sophie.model.MessageType;

import java.io.IOException;

/**
 * Created by sophie on 2015. 12. 2..
 */
class RoomSelector implements Runnable {
    // 클래스 이름이 before participate room 이런 거였으면 좋겠는데? room selecting 이외에도 닉네임 결정 등 다양한 것들 할 수 있는듯?
    private ClientHandler clientHandler;
    private RoomListManager roomListManager;
    private static final String ASKING_MESSAGE_NICKNAME = "Enter your nickname: ";
    private static final String ASKING_MESSAGE_MAKING = "Do you wanna make room? (yes/any key)";
    private static final String ASKING_MESSAGE_ROOM_NAME = "Type the room name you want to make: ";
    private static final String ASKING_MESSAGE_ROOM_NUMBER = "Enter room number if you want to participate: ";
    private static final String CLIENT_MESSAGE_WANT_TO_MAKE_ROOM = "yes";
    private static final String INFO_MESSAGE_NO_ROOM = "There is no room. You should make the first room!";

    RoomSelector(ClientHandler clientHandler, RoomListManager roomListManager) {
        this.clientHandler = clientHandler;
        this.roomListManager = roomListManager;
    }

    @Override
    public void run() {
        //ClientHandler에 client 의 닉네임 넣는 기능 넣기
        String nickname = ask(ASKING_MESSAGE_NICKNAME);
        clientHandler.setNickname(nickname);

        //방이 없을 때
        if(isRoomListEmpty()){
            send(INFO_MESSAGE_NO_ROOM);
            makeRoom();
            return;
        }

        // 방이 있을 때
        sendRoomInfo();
        if(askWannaMakeRoom()){
            makeRoom();
            return;
        }
        int roomNum = askWhichRoom();
        roomListManager.participateRoomAt(roomNum, clientHandler);
    }

    private String ask(String query){
        clientHandler.sendMessage(new GeneralMessage(MessageType.CHAT, query.getBytes()));
        return getMessage();
    }

    private boolean askWannaMakeRoom() {
        String msg = ask(ASKING_MESSAGE_MAKING);
        return msg.equals(CLIENT_MESSAGE_WANT_TO_MAKE_ROOM);
    }

    private int askWhichRoom() {
        boolean isPassable;
        int roomNumber = -1;
        do {
            String msg = ask(ASKING_MESSAGE_ROOM_NUMBER);
            try {
                roomNumber = Integer.parseInt(msg);
                isPassable = roomListManager.isExistentRoomNumber(roomNumber);
            }catch (NumberFormatException e){
                isPassable = false;
            }
        } while(!isPassable);
        return roomNumber;
    }

    private void send(String msg) {
        clientHandler.sendMessage(new GeneralMessage(MessageType.CHAT, msg.getBytes()));
    }

    private void sendRoomInfo() {
        send("room info: " + roomListManager.getAvailableRoomInfoList());
    }

    private void makeRoom() {
        String roomName = ask(ASKING_MESSAGE_ROOM_NAME);
        roomListManager.makeRoom(roomName, clientHandler);
    }

    private boolean isRoomListEmpty(){
        return roomListManager.isRoomListEmpty();
    }

    private String getMessage(){
        //
        try {
            return new String(clientHandler.getMessage().getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}