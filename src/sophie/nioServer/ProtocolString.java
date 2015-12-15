package sophie.nioServer;

/**
 * Created by sophie on 2015. 12. 15..
 */
public class ProtocolString {
    //UserNameEventHandler 에서 필요
    public static final String INFO_MESSAGE_NO_ROOM = "There is no room. You should make the first room!";
    public static final String ASKING_MESSAGE_MAKING = "Do you wanna make room? (yes/any key)";
    public static final String ASKING_MESSAGE_ROOM_NAME = "Type the room name you want to make: ";

    //RoomNumberEventHandler 필요
    public static final String INFO_MESSAGE_INVALID_ROOM_NUMBER = "올바른 방 번호가 아닙니다.";
    public static final String INFO_MESSAGE_ROOM_PARTICIPATE = "------- 방에 참가하였습니다 ---------";
    public static final String ASKING_MESSAGE_ROOM_NUMBER = "Enter room number if you want to participate: ";

    //Dispatcher에서 필요
    public static final String ASKING_MESSAGE_USERNAME = "Enter your name: ";
}
