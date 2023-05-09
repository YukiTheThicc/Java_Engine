package sapphire.eventsSystem.handlers;

import diamondEngine.diaEvents.DiaEvent;
import diamondEngine.diaEvents.DiaObserver;
import diamondEngine.diaUtils.DiaLogger;

public class DiamondEventsHandler implements DiaObserver {

    @Override
    public void onNotify(DiaEvent event) {
        switch (event.type) {
            case ASSET_ADDED:
                DiaLogger.log("Asset added");
                break;
        }
    }
}
