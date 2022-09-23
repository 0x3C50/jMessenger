import me.x150.exception.InvalidSubscriberException;
import me.x150.MessageManager;
import me.x150.MessageSubscription;
import me.x150.exception.SubscriberAlreadyRegisteredException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    static MessageManager manager = new MessageManager();

    @BeforeAll
    static void beforeAll() {
        manager.registerSubscribers(new EventModifier());
    }

    @Test
    void testEventStack() {
        Message m = new Message();
        m.content = "Hello.";
        manager.send(m);
        assertEquals("Hello. This message is modified. This message is modified by the 2nd handler.", m.content);
    }

    @Test
    void testInvalidHandler() {
        Object handler = new Object() {
            @MessageSubscription
            void invalidHandler(Object o, int a) {

            }
        };
        assertThrows(InvalidSubscriberException.class, () -> manager.registerSubscribers(handler));
    }

    @Test
    void testDuplicateRegistration() {
        Object handler = new Object() {
            @MessageSubscription
            void invalidHandler(Object o) {

            }
        };
        assertDoesNotThrow(() -> manager.registerSubscribers(handler));
        assertThrows(SubscriberAlreadyRegisteredException.class, () -> manager.registerSubscribers(handler));
    }

    static class EventModifier {
        @MessageSubscription(priority = 0)
        void modifyMessage(Message m) {
            m.content += " This message is modified.";
        }

        @MessageSubscription(priority = 1)
        void modifyMessageSecond(Message m) {
            m.content += " This message is modified by the 2nd handler.";
        }
    }

    static class Message {
        String content;
    }
}
