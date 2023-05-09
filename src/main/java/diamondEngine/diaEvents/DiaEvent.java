package diamondEngine.diaEvents;

import diamondEngine.Environment;

public class DiaEvent {

    // ATTRIBUTES
    public DiaEventType type;
    public Environment env;
    public Object payload;

    // CONSTRUCTORS
    public DiaEvent(DiaEventType type, Environment env, Object payload) {
        this.type = type;
        this.env = env;
        this.payload = payload;
    }

    public DiaEvent(DiaEventType type, Environment env) {
        this.type = type;
        this.env = env;
        this.payload = null;
    }

    public DiaEvent(DiaEventType type, Object payload) {
        this.type = type;
        this.env = null;
        this.payload = payload;
    }

    public DiaEvent(DiaEventType type) {
        this.type = type;
        this.payload = null;
        this.env = null;
    }

    public DiaEvent() {
        this.type = DiaEventType.USER_EVENT;
        this.payload = null;
        this.env = null;
    }
}
