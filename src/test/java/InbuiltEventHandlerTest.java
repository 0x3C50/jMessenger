import me.x150.MessageManager;
import me.x150.MessageSubscription;
import me.x150.impl.SubscriberRegisterEvent;
import me.x150.impl.SubscriberUnregisterEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InbuiltEventHandlerTest {
    MessageManager manager = new MessageManager();

    public InbuiltEventHandlerTest() {
        manager.registerSubscribers(new EventHandler());
    }

    @Test
    void testRegister() {
        Object handler = new Object() {
            @MessageSubscription
            void test(Object o) {

            }
        };
        manager.registerSubscribers(handler);
        manager.unregister(handler);
    }

    static class EventHandler {
        @MessageSubscription
        void logRegister(SubscriberRegisterEvent e) {
            if (e.handler().ownerInstance() instanceof EventHandler) return; // this is us being registered, skip
            assertEquals("test", e.handler().callee().getName());
        }

        @MessageSubscription
        void logUnregister(SubscriberUnregisterEvent e) {
            assertEquals("InbuiltEventHandlerTest$1", e.handlerClass().getName());
        }
    }
}
