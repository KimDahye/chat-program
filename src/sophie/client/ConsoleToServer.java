package sophie.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sophie on 2015. 11. 16..
 */
public class ConsoleToServer {

    private Scanner scanner = new Scanner(System.in);
    private PrintWriter writer = null;

    public ConsoleToServer(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            writer.println(scanner.nextLine());
        }
    }

    public void stop() {
        scanner.close();
        writer.close();
    }

}
