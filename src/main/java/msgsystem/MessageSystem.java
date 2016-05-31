package msgsystem;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by vladislav on 21.05.16.
 */
@Singleton
public class MessageSystem {
    public static final int IDLE_TIME = 100;

    private final Map<Address, ConcurrentLinkedQueue<MsgBase>> messages = new HashMap<>();

    public void register(Address address) {
        messages.put(address, new ConcurrentLinkedQueue<>());
    }

    public void remove(Address address) {
        messages.remove(address);
    }

    public void sendMessage(MsgBase message) {
        final Queue<MsgBase> messageQueue = messages.get(message.getTo());
        messageQueue.add(message);
    }

    public void execForSubscriber(Subscriber subscriber) {
        final Queue<MsgBase> messageQueue = messages.get(subscriber.getAddress());
        while (!messageQueue.isEmpty()) {
            messageQueue.poll().exec(subscriber);
        }
    }
}
