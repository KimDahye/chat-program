package sophie.model;

/**
 * Created by sophie on 2015. 12. 7..
 */
public class Message {
    MessageType messageType;
    byte[] body;

    public Message(MessageType messageType, byte[] body) {
        this.messageType = messageType;
        this.body = body;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public byte[] getBody() {
        return body;
    }
}
