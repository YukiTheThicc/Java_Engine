package sapphire;

import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.eventsSystem.handlers.*;
import sapphire.imgui.SappImGuiLayer;
import java.util.ArrayList;

public class SappEvents {

    // ATTRIBUTES
    private static final ArrayList<SappObserver> observers = new ArrayList<>();

    // GETTERS & SETTERS
    public static ArrayList<SappObserver> getObservers() {
        return observers;
    }

    // METHODS
    public static void init(SappImGuiLayer layer) {
        observers.add(new WindowsEventHandler());
        observers.add(new MenuEventHandler(layer));
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
