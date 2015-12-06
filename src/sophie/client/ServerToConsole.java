package sophie.client;

import sophie.model.Message;
import sophie.model.MessageType;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by sophie on 2015. 11. 16..
 * 서버와의 프로토콜 핸들링
 */
class ServerToConsole extends Thread{
    private DataInputStream dis = null;
    private ServerToConsoleHandler handler = null;

    ServerToConsole(DataInputStream dis, ServerToConsoleHandler handler) {
        this.dis = dis;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = getMessage();
                handler.handle(message);
            } catch (IOException ioe) {
                //ioe.printStackTrace();
                close();
                break;
            }
        }
    }

    Message getMessage() throws IOException{
        //TODO. 이 부분 util로 뺄 수 있지 않을까?
        //header 분석
        int type = dis.readInt();
        int length = dis.readInt();

        //body
        byte[] body = new byte[length];
        dis.read(body, 0, length);
        return new Message(MessageType.fromInteger(type), body);
    }

   void close() {
       try {
           if(dis != null) dis.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}
