package diamondEngine.diaEvents;

public class DiaEvent {

    // ATTRIBUTES
    public DiaEventType type;
    public Object recipient;
    public Object payload;
    public boolean isHandled = false;

    // CONSTRUCTORS
    public DiaEvent(DiaEventType type, Object recipient, Object payload) {
        this.type = type;
        this.recipient = recipient;
        this.payload = payload;
    }

    public DiaEvent(DiaEventType type, Object recipient) {
        this.type = type;
        this.recipient = recipient;
        this.payload = null;
    }

    public DiaEvent(DiaEventType type) {
        this.type = type;
        this.recipient = null;
        this.payload = null;
    }

    public DiaEvent() {
        this.type = DiaEventType.DEFAULT;
        this.recipient = null;
        this.payload = null;
    }
}
