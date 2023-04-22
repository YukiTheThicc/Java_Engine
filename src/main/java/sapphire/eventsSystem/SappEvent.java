package sapphire.eventsSystem;

public class SappEvent {

    // ATTRIBUTES
    public SappEventType type;
    public Object payload;

    // CONSTRUCTORS
    public SappEvent(SappEventType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public SappEvent(SappEventType type) {
        this.type = type;
        this.payload = null;
    }

    public SappEvent() {
        this.type = SappEventType.User_event;
        this.payload = null;
    }
}
