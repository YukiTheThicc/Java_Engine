package sapphire.eventsSystem;

import sapphire.eventsSystem.events.SappEvent;

public interface SappObserver {

    void onNotify(SappEvent event);
}
