package sapphire.eventsSystem.handlers;

import diamondEngine.Diamond;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaUtils.DiaLogger;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;

public class EnvWindowEventHandler implements SappObserver {
    @Override
    public void onNotify(SappEvent event) {
        switch(event.type) {
            case New_root_env:
            case Add_env:
                DiaLogger.log("Clicked on Add_env");
                Diamond.get().addEmptyEnvironment();
                break;
            case Add_component:
                if (event.env != null && event.payload instanceof Component) {
                    event.env.addComponent((Component) event.payload);
                    DiaLogger.log("adding grid");
                }
                break;
        }
    }
}
