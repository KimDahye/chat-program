import java.io.IOException;
import java.util.Scanner;

/**
 * Created by sophie on 2015. 11. 16..
 */
class ConsoleToServer implements Runnable{
    private Scanner scanner = null;
    private ConsoleMessageHandler handler = null;
    private Client client = null;

    ConsoleToServer(ConsoleMessageHandler handler, Client client) {
        scanner = new Scanner(System.in);
        this.handler = handler;
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                handler.handle(scanner.nextLine());
            } catch (ClientEndException cee) {
                client.closeAllResources();
                break;
            } catch (IOException ioe) {
                System.out.println("서버와의 연결이 끊겼습니다.");
                client.closeAllResources();
                break;
            }
        }
    }

    void close() {
        scanner.close();
        handler.close();
    }
}
