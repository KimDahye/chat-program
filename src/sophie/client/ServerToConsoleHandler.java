package sophie.client;

import sophie.model.Message;
import sophie.model.MessageType;
import sophie.utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sophie on 2015. 12. 7..
 */
class ServerToConsoleHandler {
    void handle(Message msg) {
        if (msg.getMessageType() == MessageType.CHAT) {
            System.out.println(new String(msg.getBody()));
            return;
        }
        if (msg.getMessageType() == MessageType.FILE) {
            FileUtils.saveFile(msg.getBody());
        }
    }
}
