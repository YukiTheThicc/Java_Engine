package diamondEngine.diaEvents;

import java.util.ArrayList;

public class DiaEventSystem {

    // ATTRIBUTES
    private static final ArrayList<DiaObserver> observers = new ArrayList<>();
    private static final ArrayList<DiaEvent> queuedEvents = new ArrayList<>();

    // METHODS
    public static void addObserver(DiaObserver observer) {
        DiaEventSystem.observers.add(observer);
    }

    public static void throwEvent(DiaEvent event) {
        queuedEvents.add(event);
    }

    public static void notifyObservers() {
        for (DiaEvent event : queuedEvents) {
            for (DiaObserver observer : observers) {
                observer.onNotify(event);
                if (event.isHandled) break;
            }
        }
    }
}
