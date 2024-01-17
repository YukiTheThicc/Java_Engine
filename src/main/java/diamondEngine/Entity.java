package diamondEngine;

import diamondEngine.diaComponents.Component;
import diamondEngine.diaComponents.Transform;
import diamondEngine.diaUtils.diaLogger.DiaLogger;
import diamondEngine.diaUtils.diaLogger.DiaLoggerLevel;

import java.util.ArrayList;

public class Entity extends DiaObject {

    public static final String GENERATED_NAME = "GENERATED_ENTITY";

    // ATTRIBUTES
    private ArrayList<Component> components;

    // RUNTIME ATTRIBUTES
    private transient ArrayList<Component> componentsToRemove;
    private transient Transform transform;
    private transient boolean toSerialize;
    private transient boolean isDirty;

    // CONSTRUCTORS
    public Entity() {
        super();
        this.name = GENERATED_NAME;
        this.components = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
        this.toSerialize = true;
    }

    public Entity(String uuid) {
        super(uuid);
        this.name = GENERATED_NAME;
        this.components = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
        this.toSerialize = true;
    }

    public Entity(String uuid, String name) {
        super(uuid);
        this.name = name;
        this.components = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
        this.toSerialize = true;
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

    //METHODS
    public Entity copy() {
        Entity copied = new Entity();
        copied.setName(this.getName() + "_copy");
        for (Component c : components) {
            copied.addComponent(c.copy());
        }
        return copied;
    }

    public void addComponent(Component component) {
        if (component != null) {
            components.add(component);
            if (component instanceof Transform) {
                transform = (Transform) component;
            }
        }
    }

    public void removeComponent(Component c) {
        if (c != null) {
            componentsToRemove.add(c);
            isDirty = true;
        }
    }

    /**
     * Gets the current instance of the component class attached to this entity if it exists. Returns null if not.
     *
     * @param componentClass The class of the component
     * @param <T>            Class
     * @return Component instance from the specified class
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            try {
                if (componentClass.isAssignableFrom(c.getClass())) {
                    return componentClass.cast(c);
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
                DiaLogger.log(Entity.class, "Failed to cast component from entity with ID: " + this.getUuid() +
                        "\n" + e.getMessage(), DiaLoggerLevel.ERROR);
            }
        }
        return null;
    }

    public void update(float dt) {
        if (isDirty) {
            for (Component c : componentsToRemove) {
                components.remove(c);
            }
            componentsToRemove.clear();
        }
        for (Component c : components) {
            c.update(dt);
        }
    }

    public void destroy() {
        for (Component c : components) {
            c.destroy();
        }
    }
}
