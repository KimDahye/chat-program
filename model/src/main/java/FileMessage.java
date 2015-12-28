/**
 * Created by sophie on 2015. 12. 15..
 */
public class FileMessage extends Message {
    //private final static int HEADER_LENGTH = 16; //TODO. TYPE, FileNameLength, FileExtLength, FileContentLength
    private final static int HEADER_LENGTH = 8; //일단 Type과 length 만.

    public FileMessage(byte[] body) {
        super(MessageType.FILE, body);
    }

    @Override
    public int getHeaderLength() {
        //이건 쓰이지 않도록 하자....
        return HEADER_LENGTH;
    }
}
