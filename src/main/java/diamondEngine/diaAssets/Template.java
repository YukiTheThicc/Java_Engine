package diamondEngine.diaAssets;

import diamondEngine.DiamondObject;
import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.diaComponents.Component;

import java.util.ArrayList;
import java.util.List;

public class Template {

    // ATTRIBUTES
    private String name;
    private List<Component> components;

    // CONSTRUCTORS
    public Template(String name) {
        this.name = name;
        this.components = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // METHODS
}
