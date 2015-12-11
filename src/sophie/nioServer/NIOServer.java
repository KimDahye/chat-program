package sophie.nioServer;

import sophie.utils.CastUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by sophie on 2015. 12. 10..
 */
public class NIOServer {
    private static final String HOST = "localhost";
    private static final int PORT = 9001;
    private Selector selector;

    private LinkedList<SocketChannel> room = new LinkedList<SocketChannel>();

    // 재활용을 위한 버퍼들 - 제대로 동작 안해서 주석처리
    ByteBuffer typeBuffer = ByteBuffer.allocate(4);
    ByteBuffer lengthBuffer = ByteBuffer.allocate(4);

    /**
     * 서버소켓 생성, 셀렉터에 서버소켓 등록
     */
    void init() {
        try {
            //서버 소켓 채널 생성
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // nonblocking mode로 전환
            serverSocketChannel.configureBlocking(false);
            // 서버 소켓 채널 에서 연결된 서버소켓을 가져온다.
            ServerSocket serverSocket = serverSocketChannel.socket();
            // 호스트, 포트로 인터넷주소를 얻는다.
            InetSocketAddress isa = new InetSocketAddress(HOST, PORT);
            // 서버 소켓에 주소 바인딩
            serverSocket.bind(isa);

            // 셀렉터 열기
            selector = Selector.open();
            // 셀렉터에 서버소켓 등록
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * selector 에서 select 하여 이벤트 종류에 따라 handling
     * 클라이언트 접속이면 클라이언트 소켓을 room에 등록시켜둠
     * 클라이언트의 메세지면 클라이언트의 메세지를 broadcasting
     */
    void start() {
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 클라이언트 접속 시도
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    // 클라이언트 메세지
                    if (key.isReadable()) {
                        readAndBroadcast(key);
                    }
                    //이미 처리한 이벤트는 반드시 삭제해야 한다.
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            registerChannel(socketChannel, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAndBroadcast(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            //TODO. 여기 read의 리턴값과 buffer 크기가 일치하는지 확인해줘야 할듯.. nonblocking이라서 조금만 읽고 리턴할 수 있다...
            int typeCount= 0;
            int lengthCount = 0;
            int contentCount = 0;
            do {
                typeCount = typeCount + socketChannel.read(typeBuffer);
            } while(typeCount < 4);
            do {
                lengthCount = lengthCount + socketChannel.read(lengthBuffer);
            } while(lengthCount < 4);
            System.out.println(lengthBuffer);
            lengthBuffer.flip();
            int contentLength = lengthBuffer.getInt();
            lengthBuffer.rewind();
            System.out.println(contentLength);
            ByteBuffer contentBuffer = ByteBuffer.allocate(contentLength);
            do {
                contentCount = contentCount + socketChannel.read(contentBuffer);
            } while(contentCount != contentLength);
            broadcast(typeBuffer, lengthBuffer, contentBuffer);
            clearRecyclableBuffers();
        } catch (IOException e) {
            try {
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(socketChannel + " 접속종료");
            room.remove(socketChannel);
        }
    }

    private void registerChannel(SocketChannel sc, int ops) throws IOException {
        if (sc == null) {
            System.out.println("Invalid Connection");
            return;
        }
        sc.configureBlocking(false);
        sc.register(selector, ops);
        room.add(sc);
    }

    private void broadcast(ByteBuffer typeBuffer, ByteBuffer lengthBuffer, ByteBuffer contentBuffer) throws IOException{
        typeBuffer.flip();
        contentBuffer.flip();
        Iterator<SocketChannel> iterator = room.iterator();
        while(iterator.hasNext()) {
            SocketChannel socketChannel = iterator.next();
            if(socketChannel != null) {
                socketChannel.write(typeBuffer);
                socketChannel.write(lengthBuffer);
                socketChannel.write(contentBuffer);
                typeBuffer.rewind();
                lengthBuffer.rewind();
                contentBuffer.rewind();
            }
        }
    }

    /*재활용 하니까 제대로 동작하지 않아서 주석처리*/
    private void clearRecyclableBuffers() {
        if (typeBuffer != null) {
            typeBuffer.clear();
            //typeBuffer.flip();
        }
        if (lengthBuffer != null) {
            lengthBuffer.clear();
            //lengthBuffer.flip();
        }
    }

    /**
     * main
     */
    public static void main(String args[]) {
        NIOServer server = new NIOServer();
        server.init();
        server.start();
    }
}
