package sapphire.eventsSystem.events;

import sapphire.eventsSystem.eventTypes.SappEventType;

public class SappEvent {

    // ATTRIBUTES
    public SappEventType type;

    // CONSTRUCTORS
    public SappEvent(SappEventType type) {
        this.type = type;
    }

    public SappEvent() {
        this.type = SappEventType.User_event;
    }
}
