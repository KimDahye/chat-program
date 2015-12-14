package sophie.nioServer;

import sophie.nioServer.eventHandler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sophie on 2015. 12. 13..
 */
public class ServerInitializer {
    private static final int PORT = 9001;
    private static final int THREAD_POOL_SIZE = 8;
    private static final int INITIAL_SIZE = 4;
    private static final int BACK_LOG = 50;

    public static void main(String[] args) {
        System.out.println("Server Start!");

        // Message Type에 따른 다양한 핸들러 생성 및 등록 - TODO. FactoryClass로 바꾸기. 싱글톤이 아닌 인스턴스가 필요하다.
        NioHandleMap handleMap = new NioHandleMap();
        NioEventHandler chatEventHandler = new ChatEventHandler();
        NioEventHandler fileEventHandler = new FileEventHandler();
        NioEventHandler userNameEventHandler = new UserNameEventHandler();
        NioEventHandler roomMakingEventHandler = new RoomMakingEventHandler();
        NioEventHandler roomNameEventHandler = new RoomNameEventHandler();
        NioEventHandler roomNumberEventHandler = new RoomNumberEventHandler();
        handleMap.put(chatEventHandler.getType(), chatEventHandler);
        handleMap.put(fileEventHandler.getType(), fileEventHandler);
        handleMap.put(userNameEventHandler.getType(), userNameEventHandler);
        handleMap.put(roomMakingEventHandler.getType(), roomMakingEventHandler);
        handleMap.put(roomNameEventHandler.getType(), roomNameEventHandler);
        handleMap.put(roomNumberEventHandler.getType(), roomNumberEventHandler);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(executor, INITIAL_SIZE);

            // 스트림 지향의 리스닝 소켓을 위한 비동기 채널
            AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(group);
            listener.bind(new InetSocketAddress(PORT), BACK_LOG);
            listener.accept(listener, new Dispatcher(handleMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
