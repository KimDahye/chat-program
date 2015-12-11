package sophie.server;

/**
 * Created by sophie on 2015. 12. 11..
 */
public class ServerInitializer {
    public static void main(String args[]) {
        final int MESSAGE_PORT = 9001;
        //final int ROOM_CAPACITY = 10;

        Server server = new Server(MESSAGE_PORT);
        server.accept();
    }
}
