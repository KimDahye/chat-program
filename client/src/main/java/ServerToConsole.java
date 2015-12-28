import java.io.DataInputStream;
import java.io.IOException;

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
                Message message = IOUtils.getMessage(dis);
                handler.handle(message);
            } catch (IOException ioe) {
                //ioe.printStackTrace();
                close();
                break;
            }
        }
    }

   void close() {
       try {
           if(dis != null) dis.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}
