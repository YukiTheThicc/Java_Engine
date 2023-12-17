package diamondEngine.diaEvents;

import diamondEngine.diaEvents.hadlers.DefaultEnvEventHandler;

import java.util.ArrayList;

public class DiaEventSystem {

    // ATTRIBUTES
    private static final ArrayList<DiaObserver> observers = new ArrayList<>();
    private static final ArrayList<DiaEvent> queuedEvents = new ArrayList<>();

    // METHODS
    public static void init() {
        observers.add(new DefaultEnvEventHandler());
    }

    public static void addObserver(DiaObserver observer) {
        DiaEventSystem.observers.add(observer);
    }

    public static void removeObserver(DiaObserver observer) {
        observers.remove(observer);
    }

    public static void throwEvent(DiaEvent event) {
        queuedEvents.add(event);
    }

    public static void dispatchEvents() {
        for (DiaEvent event : queuedEvents) {
            for (DiaObserver observer : observers) {
                observer.onNotify(event);
                if (event.isHandled) break;
            }
        }
        queuedEvents.clear();
    }
}
