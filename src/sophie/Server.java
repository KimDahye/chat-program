package sophie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    BufferedReader input = null;
    PrintWriter output = null;
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
            input.close();
            output.close();
            socket.close();
            listener.close();
        } catch (IOException ioe) {
            System.out.println(ioe); // TODO. #1
            System.exit(1);
        }
    }

    private void echo() {
        String message = messageRead();
        System.out.println(message);
        messageWrite(message);

        if(message.equals(".bye")){
            done = true;
        }
    }

    private void prepareReaderAndWriter() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ioe) {
            System.out.println(ioe); //연결이 끊겼거나 등등 inputStream을 가져올 수없을 때
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


    /* main method */
    public static void main(String args[]) {
        Server server = new Server();
        server.run(DEFAULT_PORT); //나중에 PORT 번호를 받을 수 있도록 하기 위해
    }
}
