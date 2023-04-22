package sapphire.eventsSystem.handlers;

import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.imgui.SappImGUILayer;

public class SettingsEventHandler implements SappObserver {

    // ATTRIBUTES
    private final SappImGUILayer layer;

    // CONSTRUCTORS
    public SettingsEventHandler(SappImGUILayer layer) {
        this.layer = layer;
    }

    // METHODS
    @Override
    public void onNotify(SappEvent event) {

    }


}
