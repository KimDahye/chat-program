package sophie;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sophie on 2015. 11. 10..
 */
public class Client {
    private static final int DEFAULT_PORT = 9000;

    public static void main(String[] args) throws IOException {
        //  server ip address input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter IP Address of a machine that is running the date service on port " + DEFAULT_PORT + ":");
        String serverAddress = scanner.nextLine();

        //connection
        Socket socket = new Socket(serverAddress, DEFAULT_PORT);

        //message read
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String answer = input.readLine();
        System.out.println(answer);


        socket.close();
        System.exit(0); // 이건 왜하는거지? 그냥 끝내면 안되나? main 함수의 리턴 타입이 void니까 종료했을 때 값을 반환하려면 System.exit 을 사용한다. 보통 정상종료 했을 때 0을 반환, 에러 있을 때 1을 반환한다고 함.
    }
}
