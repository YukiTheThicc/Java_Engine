package diamondEngine;

import diamondEngine.diaComponents.Component;

import java.util.ArrayList;

public class Entity extends DiamondObject{

    public static final String GENERATED_NAME = "GENERATED_ENTITY";

    // ATTRIBUTES
    private long uid;
    private String name;
    private ArrayList<Component> components;
    private boolean toSerialize;

    // CONSTRUCTORS
    public Entity(Environment env) {
        super(env);
        this.name = GENERATED_NAME;
        this.components = new ArrayList<>();
        this.toSerialize = true;
    }

    public Entity(String name, Environment env) {
        super(env);
        this.name = name;
        this.components = new ArrayList<>();
        this.toSerialize = true;
    }

    public Entity(String name, Environment env, boolean toSerialize) {
        super(env);
        this.name = name;
        this.components = new ArrayList<>();
        this.toSerialize = toSerialize;
    }

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

    public String getName() {
        return name;
    }

    //METHODS
    public void addComponent(Component component) {
        if (component != null) {
            components.add(component);
        }
    }

    public void update(float dt) {
        for (Component c : components) {
            c.update(dt);
        }
    }
}
