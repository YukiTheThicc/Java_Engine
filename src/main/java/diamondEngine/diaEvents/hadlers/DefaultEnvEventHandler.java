package diamondEngine.diaEvents.hadlers;

import diamondEngine.diaEvents.DiaEvent;
import diamondEngine.diaEvents.DiaObserver;
import diamondEngine.diaUtils.DiaLogger;

public class DefaultEnvEventHandler implements DiaObserver {

    @Override
    public void onNotify(DiaEvent event) {
        switch (event.type) {
            case ENTITY_ADDED:
                DiaLogger.log("Entity added");
                break;
            case ENTITY_REMOVED:
                DiaLogger.log("Entity removed");
                break;
            case HIERARCHY_UPDATED:
                DiaLogger.log("Hierarchy updated");
                break;
        }
    }
}
