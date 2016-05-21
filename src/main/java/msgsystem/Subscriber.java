package msgsystem;

/**
 * Created by vladislav on 21.05.16.
 */
@SuppressWarnings("unused")
public interface Subscriber {
    Address getAddress();
    MessageSystem getMessageSystem();
    void start();
}
