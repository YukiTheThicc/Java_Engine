package diamondEngine.diaEvents;

import sapphire.eventsSystem.SappEvent;

public interface DiaObserver {
    
    DiaEventCategory category = null;

    void onNotify(DiaEvent event);
}
