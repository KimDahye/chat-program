import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by sophie on 2015. 12. 14..
 */
class EventHandlerFactory {
    private static final String SPRING_APPLICATION_CONTEXT_FILE_NAME = "applicationContext-nio-server.xml";
    ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_APPLICATION_CONTEXT_FILE_NAME);

    public NioEventHandler getEventHandler(int typeValue) {
        switch(typeValue) {
            case 0:
                return (ChatEventHandler) context.getBean("ChatEventHandler");
            case 1:
                return (FileEventHandler) context.getBean("FileEventHandler");
            case 2:
                return (UserNameEventHandler) context.getBean("UserNameEventHandler");
            case 3:
                return new RoomMakingEventHandler();
            case 4:
                return (RoomNameEventHandler) context.getBean("RoomNameEventHandler");
            case 5:
                return (RoomNumberEventHandler) context.getBean("RoomNumberEventHandler");
            case 6:
                return new InfoEventHandler();
        }
        return null;
    }
}
