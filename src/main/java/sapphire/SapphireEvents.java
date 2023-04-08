package sapphire;

import sapphire.events.SappEvent;
import sapphire.events.SappObserver;
import sapphire.events.handlers.EnvWindowEventHandler;
import sapphire.events.handlers.MenuEventHandler;

import java.util.ArrayList;

public class SapphireEvents {

    // ATTRIBUTES
    private static ArrayList<SappObserver> observers = new ArrayList<>();

    // GETTERS & SETTERS
    public static ArrayList<SappObserver> getObservers() {
        return observers;
    }

    // METHODS
    public static void init() {
        observers.add(new MenuEventHandler());
        observers.add(new EnvWindowEventHandler());
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
