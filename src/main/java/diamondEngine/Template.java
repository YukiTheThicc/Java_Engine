package diamondEngine;

import diamondEngine.diaComponents.Component;

import java.util.ArrayList;
import java.util.List;

public class Template {

    // ATTRIBUTES
    private final long uid;
    private String name;
    private String entityName;
    private List<Component> components;

    // CONSTRUCTORS
    public Template(String name) {
        this.uid = Diamond.genId();
        this.name = name;
        this.components = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityName() {
        return entityName;
    }

    // METHODS
    public Entity createEntity() {

        Entity newEntity = new Entity();
        for (Component c : components) {
            newEntity.addComponent(c.copy());
        }
        return newEntity;
    }

    public void copyFromEntity(Entity entity) {
        this.entityName = entity.getName();
        this.components.clear();
        for (Component c : entity.getComponents()) {
            components.add(c.copy());
        }
    }
}
