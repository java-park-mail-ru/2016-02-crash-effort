package msgsystem;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by vladislav on 21.05.16.
 */
@Singleton
public class MessageSystem {
    public static final int IDLE_TIME = 100;

    final Map<Address, ConcurrentLinkedQueue<MsgBase>> messages = new ConcurrentHashMap<>();

    private Queue<MsgBase> getOrCreate(Address address) {
        ConcurrentLinkedQueue<MsgBase> messageQueue = messages.get(address);
        synchronized (this) {
            if (messageQueue == null) {
                messageQueue = new ConcurrentLinkedQueue<>();
                messages.put(address, messageQueue);
            }
        }
        return messageQueue;
    }

    public void sendMessage(MsgBase message) {
        final Queue<MsgBase> messageQueue = getOrCreate(message.getTo());
        messageQueue.add(message);
    }

    public void execForSubscriber(Subscriber subscriber) {
        final Queue<MsgBase> messageQueue = getOrCreate(subscriber.getAddress());
        while (!messageQueue.isEmpty()) {
            messageQueue.poll().exec(subscriber);
        }
    }
}
