package diamondEngine.diaEvents;

import diamondEngine.diaUtils.DiaLoggerObserver;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;

import java.util.ArrayList;

public class DiaEvents {

    // ATTRIBUTES
    private static ArrayList<DiaObserver> observers = new ArrayList<>();

    // METHODS
    public static void addObserver(DiaObserver observer) {
        DiaEvents.observers.add(observer);
    }

    public static void notify(DiaEvent event) {
        for (DiaObserver observer : observers) {
            observer.onNotify(event);
        }
    }
}
