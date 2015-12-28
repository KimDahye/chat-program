import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Created by sophie on 2015. 12. 7..
 */
class ServerToConsoleHandler {
    private Scanner scanner = null;
    Executor executor;
    ConsoleToServer consoleToServer;
    DataOutputStream dos;

    public ServerToConsoleHandler(ExecutorService executor, ConsoleToServer consoleToServer, DataOutputStream dos) {
        scanner = new Scanner(System.in);
        this.executor = executor;
        this.consoleToServer = consoleToServer;
        this.dos = dos;
    }

    void handle(Message msg) {
        //TODO. 방 들어가는 거에 관련된 것들은 거의 같은 작업을 한다... 이 부분 중복 제거해보자.
        if (msg.getMessageType() == MessageType.CHAT) {
            System.out.println("CHAT-" + new String(msg.getBody()));
            return;
        }
        if (msg.getMessageType() == MessageType.FILE) {
            FileUtils.saveFile(msg.getBody());
            return;
        }
        if(msg.getMessageType() == MessageType.USER_NAME){
            System.out.println(new String(msg.getBody()));
            String content = scanner.nextLine();
            Message message = new GeneralMessage(MessageType.USER_NAME, content.getBytes());
            try {
                IOUtils.sendGeneralMessage(dos, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(msg.getMessageType() == MessageType.ROOM_MAKING) {
            System.out.println(new String(msg.getBody()));

            String content = scanner.nextLine();
            Message message = new GeneralMessage(MessageType.ROOM_MAKING, content.getBytes());
            try {
                IOUtils.sendGeneralMessage(dos, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(msg.getMessageType() == MessageType.ROOM_NAME) {
            System.out.println(new String(msg.getBody()));

            String content = scanner.nextLine();
            Message message = new GeneralMessage(MessageType.ROOM_NAME, content.getBytes());
            try {
                IOUtils.sendGeneralMessage(dos, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(msg.getMessageType() == MessageType.ROOM_NUM) {
            System.out.println(new String(msg.getBody()));

            String content = scanner.nextLine();
            Message message = new GeneralMessage(MessageType.ROOM_NUM, content.getBytes());
            try {
                IOUtils.sendGeneralMessage(dos, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(msg.getMessageType() == MessageType.INFO) {
            System.out.println(new String(msg.getBody()));
        }
        if(msg.getMessageType() == MessageType.CHAT_START) {
            System.out.println(new String(msg.getBody()));

            executor.execute(consoleToServer);
        }
    }
}
