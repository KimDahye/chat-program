package sophie.server;

import sophie.model.Message;
import sophie.model.MessageType;

import java.net.Socket;

/**
 * Created by sophie on 2015. 12. 11..
 */
class Dispatcher {
    void handle(ClientHandler clientHandler, MessageType contentType, byte[] body) {
        if (contentType == contentType.CHAT) {
            byte[] bodyWithNickname = clientHandler.getBodyWithNickname(body);
            for (ClientHandler c : clients) {
                if (c != null) c.sendMessage(new Message(contentType, bodyWithNickname));
            }
        } else if (contentType == contentType.FILE) {
            for (ClientHandler c : clients) {
                if (c != clientHandler) c.sendMessage(new Message(contentType, body));
            }
        }
    }
}