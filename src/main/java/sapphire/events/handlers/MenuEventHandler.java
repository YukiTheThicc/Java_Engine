package sapphire.events.handlers;

import diamondEngine.Diamond;
import diamondEngine.diaUtils.DiaLogger;
import sapphire.events.SappEvent;
import sapphire.events.SappObserver;

public class MenuEventHandler implements SappObserver {
    @Override
    public void onNotify(SappEvent event) {
        switch(event.type) {
            case Play:
                DiaLogger.log("Clicked on play");
                break;
            case Stop:
                DiaLogger.log("Clicked on Stop");
                break;
            case Save_env:
                DiaLogger.log("Clicked on Save_env");
                break;
        }
    }
}
