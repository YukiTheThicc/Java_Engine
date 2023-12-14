package diamondEngine.diaUtils;

import diamondEngine.Entity;
import java.util.ArrayList;
import java.util.List;

public class DiaHierarchyNode {

    // ATTRIBUTES
    private String parent;
    private List<String> children;
    private final Entity entity;

    // CONSTRUCTORS
    public DiaHierarchyNode(Entity node) {
        this.parent = null;
        this.children = new ArrayList<>();
        this.entity = node;
    }

    // GETTERS & SETTERS
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<String> getChildren() {
        return children;
    }

    public Entity getEntity() {
        return entity;
    }

    // METHODS
    public void addChild(String entity) {
        children.add(entity);
    }

    public void removeChild(String entity) {
        children.remove(entity);
    }
}
