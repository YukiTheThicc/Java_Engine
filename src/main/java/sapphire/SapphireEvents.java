package sapphire;

import sapphire.events.SappObserver;
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
    }

    public static void addObserver(SappObserver observer) {
        observers.add(observer);
    }
}
