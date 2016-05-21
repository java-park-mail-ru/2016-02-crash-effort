package msgsystem;

/**
 * Created by vladislav on 21.05.16.
 */
public abstract class MsgBase {
    private final Address from;
    private final Address to;

    public MsgBase(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    protected Address getFrom() {
        return from;
    }

    protected Address getTo() {
        return to;
    }

    public abstract void exec(Subscriber subscriber);
}
