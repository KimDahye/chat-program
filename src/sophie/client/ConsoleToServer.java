package sophie.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sophie on 2015. 11. 16..
 * 클라이언트 규칙 handling
 */
class ConsoleToServer extends Thread {

    private Scanner scanner = new Scanner(System.in);
    private PrintWriter writer = null;

    ConsoleToServer(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {

            writer.println(scanner.nextLine());
        }
    }

    @Override
    public void interrupt() {
        scanner.close();
        writer.close();
        super.interrupt();
    }

}
