package sophie.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by sophie on 2015. 11. 16..
 * 서버와의 프로토콜 핸들링
 */
class ServerToConsole extends Thread{
    Client handler = null;
    private BufferedReader reader = null;

    ServerToConsole(Client handler, Socket socket) {
        try {
            this.handler = handler;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String serverMsg = reader.readLine();
                System.out.println(serverMsg);
                handler.handle(serverMsg);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    @Override
    public void interrupt() {
        try {
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            super.interrupt();
        }
    }
}
