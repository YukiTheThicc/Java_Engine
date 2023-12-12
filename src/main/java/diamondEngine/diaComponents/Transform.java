package diamondEngine.diaComponents;

import imgui.type.ImFloat;
import imgui.type.ImInt;
import org.joml.Vector2f;
import sapphire.Sapphire;
import sapphire.imgui.SappImGuiUtils;

public class Transform extends Component {

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
    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
        to.rotation = this.rotation;
        to.zIndex = this.zIndex;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void inspect() {
        SappImGuiUtils.drawVec2Control(Sapphire.getLiteral("position"), this.position);
        SappImGuiUtils.drawVec2Control(Sapphire.getLiteral("scale"), this.scale);
        ImFloat newRotation = new ImFloat(this.rotation);
        if (SappImGuiUtils.dragFloat(Sapphire.getLiteral("rotation"), newRotation)) this.rotation = newRotation.get();
        ImInt newZIndex = new ImInt(this.zIndex);
        if (SappImGuiUtils.dragInt(Sapphire.getLiteral("zindex"), newZIndex)) this.zIndex = newZIndex.get();
    }

    @Override
    public Component copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale), this.rotation, this.zIndex);
    }

    @Override
    public void destroy() {

    }
}
