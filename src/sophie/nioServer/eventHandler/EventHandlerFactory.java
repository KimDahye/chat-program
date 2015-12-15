package sophie.nioServer.eventHandler;

/**
 * Created by sophie on 2015. 12. 14..
 */
public class EventHandlerFactory {
    private EventHandlerFactory(){};

    public static NioEventHandler getEventHandler(int typeValue) {
        switch(typeValue) {
            case 0:
                return new ChatEventHandler();
            case 1:
                return new FileEventHandler();
            case 2:
                return new UserNameEventHandler();
            case 3:
                return new RoomMakingEventHandler();
            case 4:
                return new RoomNameEventHandler();
            case 5:
                return new RoomNumberEventHandler();
            case 6:
                return new InfoEventHandler();
        }
        return null;
    }
}
