package diamondEngine.diaRenderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line {

    // ATTRIBUTES
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private int lifetime;

    // CONSTRUCTORS
    public Line(Vector2f form, Vector2f to, Vector3f color, int lifetime) {
        this.from = form;
        this.to = to;
        this.color = color;
        this.lifetime = lifetime;
    }

    public Line(Vector2f form, Vector2f to) {
        this.from = form;
        this.to = to;
        this.color = new Vector3f(0, 0, 0);
        this.lifetime = -1;
    }

    // GETTERS & SETTERS
    public Vector2f getFrom() {
        return from;
    }

    public Vector2f getTo() {
        return to;
    }

    public Vector3f getColor() {
        return color;
    }

    // METHODS
    public int beginFrame() {
        this.lifetime--;
        return this.lifetime;
    }

    public float lengthSquared() {
        return new Vector2f(to).sub(from).lengthSquared();
    }
}
