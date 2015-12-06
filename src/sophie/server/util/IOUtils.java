package sophie.server.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by sophie on 2015. 12. 6..
 */
public class IOUtils {
    /**
     * @param br 은 Content Body를 시작하는 시점이어야 한다.
     * @param contentLength 는 Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String getContentType(String line) {
        String[] tokens = line.split(" ");
        return tokens[1];
    }

    public static int getContentLength(String line) {
        String[] tokens = line.split(" ");
        return Integer.parseInt(tokens[1]);
    }
}