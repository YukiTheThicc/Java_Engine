package diamondEngine.diaEvents;

import diamondEngine.Entity;
import diamondEngine.Environment;

public class DiaEvent {

    // ATTRIBUTES
    public DiaEventType type;
    public Environment env;
    public Entity entity;
    public Object payload;

    // CONSTRUCTORS
    public DiaEvent(DiaEventType type, Environment env, Entity entity, Object payload) {
        this.type = type;
        this.env = env;
        this.entity = entity;
        this.payload = payload;
    }

    public DiaEvent(DiaEventType type, Environment env, Entity entity) {
        this.type = type;
        this.env = env;
        this.entity = entity;
        this.payload = null;
    }

    public DiaEvent(DiaEventType type, Environment env, Object payload) {
        this.type = type;
        this.env = env;
        this.entity = null;
        this.payload = payload;
    }

    public DiaEvent(DiaEventType type, Environment env) {
        this.type = type;
        this.env = env;
        this.entity = null;
        this.payload = null;
    }

    public DiaEvent(DiaEventType type, Entity entity) {
        this.type = type;
        this.env = null;
        this.entity = entity;
        this.payload = null;
    }

    public DiaEvent(DiaEventType type, Object payload) {
        this.type = type;
        this.env = null;
        this.entity = null;
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
