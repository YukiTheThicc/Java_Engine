package diamondEngine.diaUtils;

import diamondEngine.Entity;
import java.util.ArrayList;
import java.util.List;

public class DiaHierarchyNode {

    // ATTRIBUTES
    private DiaHierarchyNode parent;
    private List<DiaHierarchyNode> children;
    private transient final Entity entity;

    // CONSTRUCTORS
    public DiaHierarchyNode(Entity node) {
        this.parent = null;
        this.children = new ArrayList<>();
        this.entity = node;
    }

    // GETTERS & SETTERS
    public DiaHierarchyNode getParent() {
        return parent;
    }

    public void setParent(DiaHierarchyNode parent) {
        this.parent = parent;
    }

    public List<DiaHierarchyNode> getChildren() {
        return children;
    }

    public Entity getEntity() {
        return entity;
    }

    // METHODS
    public void addChild(DiaHierarchyNode node) {
        children.add(node);
    }

    public void removeChild(DiaHierarchyNode node) {
        children.remove(node);
    }
}
