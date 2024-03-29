package diamondEngine.diaComponents;

import org.joml.Vector2f;

public class Transform implements Component {

    // ATTRIBUTES
    public Vector2f position;
    public Vector2f scale;
    public float rotation;
    public int zIndex;

    // CONSTRUCTORS
    public Transform() {
        super();
        this.position = new Vector2f();
        this.scale = new Vector2f();
        this.rotation = 0.0f;
        this.zIndex = 0;
    }

    public Transform(Vector2f position) {
        super();
        this.position = position;
        this.scale = new Vector2f();
        this.rotation = 0.0f;
        this.zIndex = 0;
    }

    public Transform(Vector2f position, Vector2f scale) {
        super();
        this.position = position;
        this.scale = scale;
        this.rotation = 0.0f;
        this.zIndex = 0;
    }

    public Transform(Vector2f position, Vector2f scale, float rotation) {
        super();
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.zIndex = 0;
    }

    public Transform(Vector2f position, Vector2f scale, float rotation, int zIndex) {
        super();
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.zIndex = zIndex;
    }

    // METHODS
    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale), this.rotation, this.zIndex);
    }

    @Override
    public void destroy() {

    }

    public void copyTo(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
        to.rotation = this.rotation;
        to.zIndex = this.zIndex;
    }
}
