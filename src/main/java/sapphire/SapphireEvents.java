package sapphire;

import sapphire.eventsSystem.events.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.eventsSystem.handlers.EnvWindowEventHandler;
import sapphire.eventsSystem.handlers.ControlsEventHandler;
import sapphire.eventsSystem.handlers.SettingsEventHandler;
import sapphire.imgui.SappImGUILayer;
import java.util.ArrayList;

public class SapphireEvents {

    // ATTRIBUTES
    private static ArrayList<SappObserver> observers = new ArrayList<>();

    // GETTERS & SETTERS
    public static ArrayList<SappObserver> getObservers() {
        return observers;
    }

    // METHODS
    public static void init(SappImGUILayer layer) {
        observers.add(new ControlsEventHandler());
        observers.add(new EnvWindowEventHandler());
        observers.add(new SettingsEventHandler(layer));
    }

    public static void addObserver(SappObserver observer) {
        observers.add(observer);
    }

    public static void notify(SappEvent event) {
        for (SappObserver observer : observers) {
            observer.onNotify(event);
        }
    }
}
