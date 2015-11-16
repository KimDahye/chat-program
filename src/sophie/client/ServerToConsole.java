package sophie.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sophie on 2015. 11. 16..
 */
public class ServerToConsole {
    Client handler = null;
    private BufferedReader reader = null;

    public ServerToConsole(Client handler, Socket socket) {
        try {
            this.handler = handler;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void stop() {
        try {
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
