package sapphire.eventsSystem;

import diamondEngine.Environment;

public class SappEvent {

    // ATTRIBUTES
    public SappEventType type;
    public Environment env;
    public Object payload;

    // CONSTRUCTORS
    public SappEvent(SappEventType type, Environment env, Object payload) {
        this.type = type;
        this.env = env;
        this.payload = payload;
    }

    public SappEvent(SappEventType type, Environment env) {
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
