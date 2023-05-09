package diamondEngine.diaEvents;

import sapphire.eventsSystem.SappEvent;

public interface DiaObserver {

    void onNotify(DiaEvent event);
}
