package sapphire.events.handlers;

import diamondEngine.Diamond;
import diamondEngine.diaUtils.DiaLogger;
import sapphire.events.SappEvent;
import sapphire.events.SappObserver;

public class EnvWindowEventHandler implements SappObserver {
    @Override
    public void onNotify(SappEvent event) {
        switch(event.type) {
            case New_root_env:
                DiaLogger.log("Clicked on New_root_env");
                Diamond.get().addEmptyEnvironment();
                break;
            case Add_env:
                DiaLogger.log("Clicked on Add_env");
                break;
            case Add_entity:
                DiaLogger.log("Clicked on Add_entity");
                break;
        }
    }
}
