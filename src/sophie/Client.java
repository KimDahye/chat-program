package sophie;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sophie on 2015. 11. 10..
 */
public class Client {
    private Socket socket = null;
    BufferedReader input = null;
    PrintWriter output = null;
    private boolean done = false;
    private static final int DEFAULT_PORT = 9000;

    public void run (int port) {
        //  server ip address input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter IP Address of a machine that is running the date service on port " + port + ":");
        String serverAddress = scanner.nextLine();

        //connection
        connect(serverAddress, port);

        prepareReaderAndWriter();
        String message = null;
        while(!done) {
            System.out.print("message: ");
            message = scanner.nextLine();
            messageWrite(message);
            messageRead();
            if(message.equals(".bye")) {
                done = true;
            }
        }
        closeResource();
        System.exit(0);
    }

    private void connect(String serverAddress, int port) {
        try {
            System.out.println("Welcome. I'm s client.");
            socket = new Socket(serverAddress, DEFAULT_PORT);

        } catch (IOException ioe) {
            System.out.println(ioe); // TODO. #1.
            System.exit(1);
        }
    }

    private void prepareReaderAndWriter() {
        //TODO. reader, writer 관련 메소드를 하나의 클래스로 빼자. helper class
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ioe) {
            System.out.println(ioe); //연결이 끊겼거나 등등 inputStream을 가져올 수없을 때
            System.exit(1);
        }
    }

    private void closeResource() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException ioe) {
            System.out.println(ioe); // TODO. #1
            System.exit(1);
        }
    }

    private String messageRead() {
        try {
            return input.readLine();
        } catch (IOException ioe) {
            System.out.println(ioe); //연결이 끊겼거나 등등 inputStream을 가져올 수없을 때
            done = true;
            return null;
        }
    }

    private void messageWrite(String message) {
        output.print(message); //TODO. println안해도 되겠지? message에 /n 이 속해있지 않을까?
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run(DEFAULT_PORT); //나중에 PORT 번호를 받을 수 있도록 하기 위해
    }


}
