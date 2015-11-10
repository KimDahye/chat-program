package sophie;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by sophie on 2015. 11. 10..
 */
public class Server {
    private static final int DEFAULT_PORT = 9000;

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(DEFAULT_PORT);
        System.out.println("Welcome. I am a Server!");
        Socket socket;

        while ((socket = listener.accept()) != null) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(new Date().toString());
            } finally {
                socket.close();
            }
        }

        listener.close();
    }
}
