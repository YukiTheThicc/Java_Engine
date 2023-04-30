package diamondEngine;

import diamondEngine.diaComponents.Component;

import java.util.ArrayList;

public class DiaEntity {

    // ATTRIBUTES
    private long uid;
    private ArrayList<Component> components;
    private boolean toSerialize;

    // CONSTRUCTORS

    // GETTERS & SETTERS
    public boolean isToSerialize() {
        return toSerialize;
    }

    public void setToSerialize(boolean toSerialize) {
        this.toSerialize = toSerialize;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public long getUid() {
        return uid;
    }

    //METHODS
    public void addComponent(Component component) {
        if (component != null) {
            components.add(component);
        }
    }

    public void update(float dt) {

    }
}
