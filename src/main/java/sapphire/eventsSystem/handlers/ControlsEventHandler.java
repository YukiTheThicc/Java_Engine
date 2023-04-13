package sapphire.eventsSystem.handlers;

import diamondEngine.diaUtils.DiaLogger;
import sapphire.eventsSystem.events.SappEvent;
import sapphire.eventsSystem.SappObserver;

public class ControlsEventHandler implements SappObserver {
    @Override
    public void onNotify(SappEvent event) {
        switch(event.type) {
            case Play:
                DiaLogger.log("Clicked on Play");
                break;
            case Stop:
                DiaLogger.log("Clicked on Stop");
                break;
            case View_Port_Settings:
                DiaLogger.log("Clicked on View_Port_Settings");
                break;
        }
    }
}
