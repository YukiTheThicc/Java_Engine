package sapphire.eventsSystem;

import sapphire.eventsSystem.handlers.*;
import sapphire.imgui.SappImGuiLayer;
import java.util.ArrayList;

public class SappEventSystem {

    // ATTRIBUTES
    private static final ArrayList<SappObserver> observers = new ArrayList<>();
    private static final ArrayList<SappEvent> queuedEvents = new ArrayList<>();

    // GETTERS & SETTERS
    public static ArrayList<SappObserver> getObservers() {
        return observers;
    }

    public static void removeObserver(SappObserver observer) {
        observers.remove(observer);
    }

    // METHODS
    public static void init(SappImGuiLayer layer) {
        observers.add(new WindowsEventHandler());
        observers.add(new MenuEventHandler(layer));
    }

    public static void addObserver(SappObserver observer) {
        observers.add(observer);
    }

    public static void throwEvent(SappEvent event) {
        queuedEvents.add(event);
    }

    public static void dispatchEvents() {
        for (SappEvent event : queuedEvents) {
            for (SappObserver observer : observers) {
                observer.onNotify(event);
                if (event.isHandled) break;
            }
        }
        queuedEvents.clear();
    }
}
