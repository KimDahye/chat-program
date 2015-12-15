package sophie.model;

/**
 * Created by sophie on 2015. 12. 14..
 */
public class GeneralMessage extends Message {
    private final static int HEADER_LENGTH = 8;
    public GeneralMessage(MessageType messageType, byte[] body) {
        super(messageType, body);
    }

    @Override
    public int getHeaderLength() {
        return HEADER_LENGTH;
    }
}
