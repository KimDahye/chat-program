package sophie.server.contentHandler;

/**
 * Created by sophie on 2015. 12. 7..
 */
public class MessageHandler implements ContentHandler{

    @Override
    public String makeResoponseHeader(byte[] body) {

    }

    private String getContentType() {
        return "Content-Type: Message\r\n";
    }

    private String getContentLength(byte[] body) {
        return "Content-Length: " + body.length + "\r\n";
    }
}
