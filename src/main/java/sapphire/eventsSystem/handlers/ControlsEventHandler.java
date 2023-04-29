package sapphire.eventsSystem.handlers;

import diamondEngine.diaUtils.DiaLogger;
import sapphire.Sapphire;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;

public class ControlsEventHandler implements SappObserver {
    @Override
    public void onNotify(SappEvent event) {
        switch(event.type) {
            case Play:
                Sapphire.get().setDiaRunning(true);
                break;
            case Stop:
                Sapphire.get().setDiaRunning(false);
                break;
        }
    }
}
