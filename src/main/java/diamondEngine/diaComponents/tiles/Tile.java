package diamondEngine.diaComponents.tiles;

import org.joml.Vector2i;

public class Tile {

    // ATTRIBUTES
    private Vector2i offset;
    private int index;
    private boolean hasCollision;
    // private Shape2D collisionShape;

    // CONTRUCTORS
    public Tile(Vector2i offset, int index, boolean hasCollision) {
        this.offset = offset;
        this.index = index;
        this.hasCollision = hasCollision;
    }

    // GETTERS & SETTERS
    public Vector2i getOffset() {
        return offset;
    }

    public void setOffset(Vector2i offset) {
        this.offset = offset;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isHasCollision() {
        return hasCollision;
    }

    public void setHasCollision(boolean hasCollision) {
        this.hasCollision = hasCollision;
    }

    // METHODS
}
