package sapphire.eventsSystem;

import diamondEngine.DiaEnvironment;

public class SappEvent {

    // ATTRIBUTES
    public SappEventType type;
    public DiaEnvironment env;
    public Object payload;

    // CONSTRUCTORS
    public SappEvent(SappEventType type, DiaEnvironment env, Object payload) {
        this.type = type;
        this.env = env;
        this.payload = payload;
    }

    public SappEvent(SappEventType type, DiaEnvironment env) {
        this.type = type;
        this.env = env;
        this.payload = null;
    }

    public SappEvent(SappEventType type, Object payload) {
        this.type = type;
        this.env = null;
        this.payload = payload;
    }

    public SappEvent(SappEventType type) {
        this.type = type;
        this.payload = null;
        this.env = null;
    }

    public SappEvent() {
        this.type = SappEventType.User_event;
        this.payload = null;
        this.env = null;
    }
}
