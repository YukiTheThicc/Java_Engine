package diamondEngine.diaEvents;

import diamondEngine.diaUtils.DiaLoggerObserver;

import java.util.ArrayList;

public class DiaEvents {

    // ATTRIBUTES
    private static ArrayList<DiaLoggerObserver> observers = new ArrayList<>();

    // METHODS
    public static void addObserver(DiaLoggerObserver observer) {
        DiaEvents.observers.add(observer);
    }

    public static void notifyLog() {
        // Notify observers
    }
}
