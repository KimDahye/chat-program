package sophie;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sophie on 2015. 11. 10..
 */
public class Client {
    private Socket socket = null;
    private DataInputStream streamIn  =  null;
    private DataOutputStream streamOut = null;
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
        // TODO. reader, writer 관련 메소드를 하나의 클래스로 빼자. helper class
        try {
            streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException ioe) {
            System.out.println(ioe); //연결이 끊겼거나 등등 inputStream을 가져올 수없을 때
            System.exit(1);
        }
    }

    private void closeResource() {
        try {
            streamIn.close();
            streamOut.close();
            socket.close();
        } catch (IOException ioe) {
            System.out.println(ioe); // TODO. #1
            System.exit(1);
        }
    }

    private String messageRead() {
        try {
            String line = streamIn.readUTF();
            System.out.println("client read: " + line);
            return line;
        } catch (IOException ioe) {
            System.out.println(ioe); //연결이 끊겼거나 등등 inputStream을 가져올 수없을 때
            done = true;
            return null;
        }
    }

    private void messageWrite(String message) {
        try {
            streamOut.writeUTF(message);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println(ioe); //연결이 끊겼거나 등등 inputStream을 가져올 수없을 때
            done = true;
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run(DEFAULT_PORT); //나중에 PORT 번호를 받을 수 있도록 하기 위해
    }
}
