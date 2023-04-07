package diamondEngine;

import java.util.List;

public class DiaEnvironment {

    // ATTRIBUTES
    private DiaEnvironment parent;
    private List<DiaEnvironment> children;
    private List<DiaEntity> entities;
    private List<DiaEntity> components;
    private String name;
    private boolean isInitialized;

    // CONSTRUCTORS
    public DiaEnvironment(String name) {
        this.name = name;
        this.isInitialized = false;
    }

    // GETTERS & SETTERS
    public String getName() {
        return name;
    }

    public List<DiaEnvironment> getChildren() {
        return children;
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

    public void init() {
        isInitialized = true;
    }

    public void update() {

    }

    public void destroy() {

    }
}
