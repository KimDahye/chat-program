package sophie;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sophie on 2015. 11. 10..
 * 참고 사이트 http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html 보면서 이해해보기
 * step1
 */
public class Server {
    private ServerSocket listener = null;
    private Socket socket = null;
    private DataInputStream  streamIn  =  null;
    private DataOutputStream streamOut = null;

    private boolean done = false;
    private static final int DEFAULT_PORT = 9000;

    public void run(int port) {
        openListener(port);
        acceptClient();
        prepareReaderAndWriter();
        while (!done) {
           echo();
        }
        closeResource();
        System.exit(0);
    }

    private void openListener(int port) {
        try {
            System.out.println("Welcome. I'm s server.");
            listener = new ServerSocket(port);
            System.out.println("Listener started: " + listener);
        } catch (IOException ioe) {
            System.out.println(ioe); // TODO. #1. 에러 일 때 print하는 게 따로 있으려나? 찾아보자.
            System.exit(1);
        }
    }

    private void acceptClient() {
        try {
            System.out.println("waiting for a client...");
            socket = listener.accept();
            System.out.println("Client accepted: " + socket);
        } catch (IOException ioe) {
            System.out.println(ioe); // TODO. #1
            // TODO #2 listener.close(); 여기서 닫아야 할 거 같은데 그럼 또 try catch를 써야 함 ㅠㅠ 고민해보자.
            System.exit(1);
        }
    }

    private void closeResource() {
        try {
            streamIn.close();
            streamOut.close();
            socket.close();
            listener.close();
        } catch (IOException ioe) {
            System.out.println(ioe); // TODO. #1
            System.exit(1);
        }
    }

    private void echo() {
        String message = messageRead();
        messageWrite(message);

        if(message.equals(".bye")){
            done = true;
        }
    }

    private void prepareReaderAndWriter() {
        try {
            streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException ioe) {
            System.out.println(ioe); //연결이 끊겼거나 등등 inputStream을 가져올 수없을 때
            System.exit(1);
        }
    }

    private String messageRead() {
        try {
            String line = streamIn.readUTF();
            System.out.println("server read: " + line);
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

    /* main method */
    public static void main(String args[]) {
        Server server = new Server();
        server.run(DEFAULT_PORT); //나중에 PORT 번호를 받을 수 있도록 하기 위해
    }
}
