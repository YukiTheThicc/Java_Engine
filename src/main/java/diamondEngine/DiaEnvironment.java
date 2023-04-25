package diamondEngine;

import diamondEngine.diaComponents.Component;

import java.util.ArrayList;
import java.util.List;

public class DiaEnvironment {

    // ATTRIBUTES
    private DiaEnvironment parent;
    private List<DiaEnvironment> children;
    private List<DiaEntity> entities;
    private List<Component> components;
    private String name;
    private boolean isInitialized;

    // CONSTRUCTORS
    public DiaEnvironment(String name) {
        this.parent = null;
        this.name = name;
        this.isInitialized = false;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public DiaEnvironment(String name, DiaEnvironment parent) {
        this.parent = parent;
        this.name = name;
        this.isInitialized = false;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public String getName() {
        return name;
    }

    public List<DiaEnvironment> getChildren() {
        return children;
    }

    public DiaEnvironment getParent() {
        return parent;
    }

    public List<DiaEntity> getEntities() {
        return entities;
    }

    public List<Component> getComponents() {
        return components;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    // METHODS
    public void addChild(DiaEnvironment environment) {
        if (environment != null) {
            if (!environment.isInitialized) environment.init();
            children.add(environment);
        }
    }

    public void addComponent(Component component) {
        if (component != null) {
            components.add(component);
        }
    }


    public void init() {
        isInitialized = true;
    }

    public void update() {

    }

    public void destroy() {

    }
}
