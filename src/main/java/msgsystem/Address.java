package msgsystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vladislav on 21.05.16.
 */
public class Address {
    static final AtomicInteger ID_CREATOR = new AtomicInteger();
    final int subscriberId;

    public Address() {
        this.subscriberId = ID_CREATOR.incrementAndGet();
    }

    @Override
    public int hashCode() {
        return subscriberId;
    }

    @Override
    public boolean equals(Object a) {
        return (a instanceof Address && ((Address) a).subscriberId == this.subscriberId);
    }
}
