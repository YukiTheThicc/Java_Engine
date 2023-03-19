package diamondEngine;

import java.util.ArrayList;

public class DiaEnvironment {

    // ATTRIBUTES
    private DiaEnvironment parent;
    private ArrayList<DiaEnvironment> children;
    private ArrayList<DiaEntity> entities;
    private boolean isInitialized;

    // CONSTRUCTORS
    public DiaEnvironment() {
        isInitialized = false;
    }

    // GETTERS & SETTERS
    public ArrayList<DiaEnvironment> getChildren() {
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
