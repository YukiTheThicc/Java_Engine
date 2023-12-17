package sapphire.eventsSystem;

import diamondEngine.Entity;
import diamondEngine.Environment;

public class SappEvent {

    // ATTRIBUTES
    public SappEventType type;
    public Environment env;
    public Entity entity;
    public Object payload;
    public boolean isHandled = false;

    // CONSTRUCTORS
    public SappEvent(SappEventType type, Environment env, Entity entity, Object payload) {
        this.type = type;
        this.env = env;
        this.entity = entity;
        this.payload = payload;
    }

    public SappEvent(SappEventType type, Environment env, Entity entity) {
        this.type = type;
        this.env = env;
        this.entity = entity;
        this.payload = null;
    }

    public SappEvent(SappEventType type, Environment env) {
        this.type = type;
        this.env = env;
        this.entity = null;
        this.payload = null;
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
