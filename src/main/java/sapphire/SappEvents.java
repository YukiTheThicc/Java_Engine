package sapphire;

import diamondEngine.diaEvents.DiaEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.eventsSystem.handlers.*;
import sapphire.imgui.SappImGUILayer;
import java.util.ArrayList;

public class SappEvents {

    // ATTRIBUTES
    private static final ArrayList<SappObserver> observers = new ArrayList<>();

    // GETTERS & SETTERS
    public static ArrayList<SappObserver> getObservers() {
        return observers;
    }

    // METHODS
    public static void init(SappImGUILayer layer) {
        observers.add(new WindowsEventHandler());
        observers.add(new MenuEventHandler(layer));
        DiaEvents.addObserver(new DiamondEventsHandler());
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
