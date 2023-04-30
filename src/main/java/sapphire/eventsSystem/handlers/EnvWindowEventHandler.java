package sapphire.eventsSystem.handlers;

import diamondEngine.Diamond;
import diamondEngine.diaComponents.Component;
import sapphire.Sapphire;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.imgui.SappDrawable;

public class EnvWindowEventHandler implements SappObserver {
    @Override
    public void onNotify(SappEvent event) {
        switch(event.type) {
            case New_root_env:
            case Add_env:
                Diamond.get().addEmptyEnvironment();
                break;
            case Add_component:
                if (event.env != null && event.payload instanceof Component) {
                    event.env.addComponent((Component) event.payload);
                }
                break;
            case Selected_object:
                if (event.payload instanceof SappDrawable) Sapphire.setActiveObject((SappDrawable) event.payload);
                break;
        }
    }
}
