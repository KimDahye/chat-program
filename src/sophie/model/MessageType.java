package sophie.model;

/**
 * Created by sophie on 2015. 12. 7..
 */
public enum MessageType {
    CHAT(0), FILE(1), USER_NAME(2), ROOM_MAKING(3), ROOM_NAME(4), ROOM_NUM(5);

    private final int value;
    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageType fromInteger(int x) {
        switch(x) {
            case 0:
                return CHAT;
            case 1:
                return FILE;
            case 2:
                return USER_NAME;
            case 3:
                return ROOM_MAKING;
            case 4:
                return ROOM_NAME;
            case 5:
                return ROOM_NUM;
        }
        return null;
    }
}
