package sophie.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by sophie on 2015. 11. 16..
 */
class ReaderWriter {
    // 어떤 reader, writer를 쓸 지의 구체적 전략이 들어있다. - DI로 빼야할까?

    private BufferedReader reader = null;
    private PrintWriter writer = null;

    void open(Socket socket) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    String readLine() throws IOException {
        return reader.readLine();
    }

    void writeLine(String msg) {
        writer.println(msg);
    }

    void close() throws IOException {
        reader.close();
        writer.close();
    }
}
