package sophie.model;

/**
 * Created by sophie on 2015. 12. 7..
 */
public enum MessageType {
    CHAT(0), FILE(1);

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
        }
        return null;
    }
}
