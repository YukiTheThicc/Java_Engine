package sapphire.eventsSystem.handlers;

import sapphire.eventsSystem.events.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.imgui.SappImGUILayer;

public class SettingsEventHandler implements SappObserver {

    // ATTRIBUTES
    private SappImGUILayer layer;

    // CONSTRUCTORS
    public SettingsEventHandler(SappImGUILayer layer) {
        this.layer = layer;
    }

    // METHODS
    @Override
    public void onNotify(SappEvent event) {

    }
}
