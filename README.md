# jMessenger
An easy to use messaging library for java.

## Installing
This library can be installed via [JitPack](https://jitpack.io/), use `master-SNAPSHOT` as version to grab and build the latest commit automatically. Note that this may cause gradle to time out, which can be resolved by waiting up to 5 minutes, then trying to rebuild the project.

Also note that gradle may cache the latest `SNAPSHOT` version. To re-download the newest commit, run gradle with the `--refresh-dependencies` flag. Refer to the note above for what to do when gradle times out during this operation.

## Usage
This library uses concepts such as "messages" and "message subscribers". The former being an instance of any object.

All message subscribers are methods with one argument of type T, and no return type (V), annotated with `@MessageSubscription`. The handler methods get called by the library automatically, when a message of type T is emitted. Note that any message that can be cast to T will be sent to every handler listening for T, which means that a handler listening to `java/lang/Object` will receive **every** message being emitted.

To register a handler, `MessageManager#registerSubscribers` is called, the argument being an instance of any object owning the message subscriber methods. A `SubscriberRegisterEvent` is emitted by the library, for each new handler being registered. If one of the handlers listens to `SubscriberRegisterEvent`, it is invoked immediately, following its subscription, since the library is treating it as an already existing handler for the message.

To unregister a handler entirely, call `MessageManager#unregister`, the argument either being **any** instance of class T, or class T directly. All message subscribers owned by class T will be removed, and a `SubscriberUnregisterEvent` is emitted by the library. If one of the handlers being unregistered was listening for `SubscriberUnregisterEvent`, it is **not** being called, following its own removal.

The owning class can be registered again, following `#unregister`.

## Example
```java
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
```

This example can also be viewed in `me.x150.Example`

## Reporting bugs
To report issues or suggest features, please open an issue on the issue tracker.