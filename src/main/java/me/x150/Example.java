package me.x150;

class Example {
    record Message(String content) {}
    record NotAMessage(int v) {}
    static MessageManager manager = new MessageManager();

    public static void main(String[] args) {
        manager.registerSubscribers(new EverythingHandler()); // Register EverythingHandler
        manager.registerSubscribers(new Example()); // Register Example

        manager.send(new Message("Hello, World!")); // Send Message with content "Hello, World!" to EverythingHandler and then Example
        manager.send(new NotAMessage(123)); // Send NotAMessage to EverythingHandler, but not Example
        manager.send(456); // Send generic type to EverythingHandler, but not Example

        manager.unregister(Example.class); // Unregister all handlers owned by Example
        manager.send(new Message("Only the EverythingHandler will receive this")); // Send Message to EverythingHandler, but not Example, since it's no longer registered
    }

    static class EverythingHandler {
        @MessageSubscription(priority = -10) // Priority of -10 means this handler will be called before Example
        void handleAllMessages(Object message) {
            System.out.printf("Received message of type %s: %s%n", message.getClass().getName(), message);
        }
    }

    @MessageSubscription(priority = 10) // Priority of 10 means this handler will be called after EverythingHandler
    void handleMessage(Message message) {
        System.out.printf("(This will be printed after EverythingHandler) Received message: %s%n", message.content);
    }
}
