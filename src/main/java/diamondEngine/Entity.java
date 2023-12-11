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
    private transient Transform transform;
    private transient boolean toSerialize;
    private transient boolean isDirty;
    private transient ArrayList<Component> toRemove;

    // CONSTRUCTORS
    public Entity() {
        super();
        this.name = GENERATED_NAME;
        this.components = new ArrayList<>();
        this.toRemove = new ArrayList<>();
        this.toSerialize = true;
    }

    public Entity(String uuid) {
        super(uuid);
        this.name = GENERATED_NAME;
        this.components = new ArrayList<>();
        this.toRemove = new ArrayList<>();
        this.toSerialize = true;
    }

    public Entity(String uuid, String name) {
        super(uuid);
        this.name = name;
        this.components = new ArrayList<>();
        this.toRemove = new ArrayList<>();
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
            component.setOwner(this.getUuid());
            component.setParent(this.getParent());
            components.add(component);
            if (this.getParent() != null) {
                this.getParent().registerObject(component);
            }
            if (component instanceof Transform) {
                this.transform = (Transform) component;
            }
        }
    }

    public void removeComponent(String uuid) {
        for (Component c : components) {
            if (c.getUuid().equals(uuid)) {
                toRemove.add(c);
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
            for (Component c : toRemove) {
                components.remove(c);
                this.getParent().unRegisterObject(c);
            }
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
                this.getParent().setModified();
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
