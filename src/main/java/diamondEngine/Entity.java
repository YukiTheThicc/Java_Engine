package diamondEngine;

import diamondEngine.diaComponents.Component;
import diamondEngine.diaComponents.Transform;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import imgui.ImGui;
import imgui.type.ImString;
import sapphire.Sapphire;
import sapphire.imgui.SappImGuiUtils;

import java.util.ArrayList;

public class Entity extends DiaObject {

    public static final String GENERATED_NAME = "GENERATED_ENTITY";

    // ATTRIBUTES
    private String name;
    private ArrayList<Component> components;
    private ArrayList<Entity> nestedEntities;

    // RUNTIME ATTRIBUTES
    private transient ArrayList<Component> componentsToRemove;
    private transient ArrayList<Entity> entitiesToRemove;
    private transient Transform transform;
    private transient boolean toSerialize;
    private transient boolean isDirty;

    // CONSTRUCTORS
    public Entity() {
        super();
        this.name = GENERATED_NAME;
        this.components = new ArrayList<>();
        this.nestedEntities = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.toSerialize = true;
    }

    public Entity(String uuid) {
        super(uuid);
        this.name = GENERATED_NAME;
        this.components = new ArrayList<>();
        this.nestedEntities = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.toSerialize = true;
    }

    public Entity(String uuid, String name) {
        super(uuid);
        this.name = name;
        this.components = new ArrayList<>();
        this.nestedEntities = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
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

    public ArrayList<Entity> getNestedEntities() {
        return nestedEntities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //METHODS
    public Entity copy() {
        Entity copied = new Entity();
        copied.setName(name + "_copy");
        for (Component c : components) {
            copied.addComponent(c.copy());
        }
        return copied;
    }

    public void addComponent(Component component) {
        if (component != null) {
            component.setOwner(getUuid());
            component.setEnv(getEnv());
            components.add(component);
            if (getEnv() != null) {
                getEnv().registerObject(component);
                getEnv().setModified();
            }
            if (component instanceof Transform) {
                transform = (Transform) component;
            }
        }
    }

    public void addNestedEntity(Entity entity) {
        if (entity != null && getParent() != entity && !nestedEntities.contains(entity)) {

            // Handle removing of nested entity from its parent list
            DiaObject parent = entity.getParent();
            if (parent != null) {
                if (parent instanceof Environment) {
                    ((Environment) parent).removeEntity(entity);
                } else if (parent instanceof Entity) {
                    ((Entity) parent).removeNestedEntity(getUuid());
                }
            }

            entity.setParent(this);
            entity.setEnv(this.getEnv());
            nestedEntities.add(entity);

            if (getEnv() != null) getEnv().setModified();
        }
    }

    public void removeComponent(String uuid) {
        for (Component c : components) {
            if (c.getUuid().equals(uuid)) {
                componentsToRemove.add(c);
                isDirty = true;
            }
        }
    }

    public void removeNestedEntity(String uuid) {
        for (Entity e : nestedEntities) {
            if (e.getUuid().equals(uuid)) {
                entitiesToRemove.add(e);
                isDirty = true;
            }
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
                this.getEnv().unRegisterObject(c);
            }
            componentsToRemove.clear();
            for (Entity e : entitiesToRemove) {
                nestedEntities.remove(e);
            }
            entitiesToRemove.clear();
        }
        for (Component c : components) {
            c.update(dt);
        }
    }

    /**
     * Context menu for a component
     *
     * @param c Component for which the menu is being drawn
     */
    private void componentContextMenu(Component c) {
        if (ImGui.beginPopupContextItem(c.getUuid())) {
            if (ImGui.menuItem(Sapphire.getLiteral("remove"))) {
                this.removeComponent(c.getUuid());
            }
            ImGui.endPopup();
        }
    }

    @Override
    public void inspect() {

        SappImGuiUtils.textLabel("UUID", this.getUuid());
        ImString newName = new ImString(name, 256);
        if (SappImGuiUtils.inputText(Sapphire.getLiteral("name"), newName)) {
            if (Sapphire.get().getProject() != null && !newName.isEmpty()) {
                name = newName.get();
                this.getEnv().setModified();
            }
        }
        for (Component c : components) {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName())) {
                componentContextMenu(c);
                c.inspect();
            } else {
                componentContextMenu(c);
            }
        }
    }
}
