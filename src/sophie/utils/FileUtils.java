package sophie.utils;

import sophie.client.exception.OutOfFileLengthLimitException;

import java.io.*;
import java.util.UUID;

/**
 * Created by sophie on 2015. 12. 10..
 */
public class FileUtils {
    private static String uuid = UUID.randomUUID().toString().split("-")[0];
    private static int downloadCount = 1;

    //TODO. Maven 적용하면 commons.apache 에서 toByteArray 사용하자.
    // 참고. http://commons.apache.org/proper/commons-io/apidocs/org/apache/commons/io/IOUtils.html#toByteArray%28java.io.InputStream%29
    public static byte[] getBytesFromFile(File file) throws IOException, OutOfFileLengthLimitException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new OutOfFileLengthLimitException("보낼 수 있는 파일의 크기를 초과하였습니다.");
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static void saveFile(byte[] body) {
        try {
            //TODO. 프로토콜에 파일네임 넣는 게 없어서 일단 파일네임은 임의로 정한다. 수정해야 할 부분.
            String fileName = uuid + "download" + (downloadCount++) + ".txt";

            // 파일을 생성하고 파일에 대한 출력 스트림 생성
            File file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            // 바이트 데이터를 전송받으면서 파일에 기록
            bufferedOutputStream.write(body, 0, body.length);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            fileOutputStream.close();

            System.out.println("파일 수신 작업을 완료하였습니다.");
            System.out.println("받은 파일의 사이즈 : " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
