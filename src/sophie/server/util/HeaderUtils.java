package sophie.server.util;

/**
 * Created by sophie on 2015. 12. 7..
 */
public class HeaderUtils {
    public static String getContentType(String type) {

    }

    public static String getContentLength(byte[] body) {
        return "Content-Length: " + body.length + "\r\n";
    }
}
