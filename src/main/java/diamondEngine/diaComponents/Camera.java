package diamondEngine.diaComponents;

import diamondEngine.Diamond;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera extends Component{

    // ATTRIBUTES
    public Vector2f pos;
    public float zoom = 1.0f;
    private final Vector3f front;
    private final Vector3f up;
    private final Vector2f pSize;
    private boolean isActive = false;

    // RUNTIME ATTRIBUTES
    private transient final Vector2f pSizeActual;
    private transient Matrix4f pMatrix;
    private transient final Matrix4f vMatrix;
    private transient Matrix4f invProj;
    private transient Matrix4f invView;

    // CONSTRUCTORS
    public Camera(Vector2f pos, float pWidth, float pHeight) {
        this.pos = pos;
        this.pSize = new Vector2f(pWidth, pHeight);
        this.pSizeActual = new Vector2f(pSize.x, pSize.y);
        this.pMatrix = new Matrix4f();
        this.vMatrix = new Matrix4f();
        this.invProj = new Matrix4f();
        this.invView = new Matrix4f();
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.update(0);
    }

    // GETTERS & SETTERS
    public Matrix4f getProjMatrix() {
        return this.pMatrix;
    }

    public Matrix4f getViewMatrix() {
        vMatrix.identity();
        Vector3f cameraFront = new Vector3f(front);
        vMatrix.lookAt(new Vector3f(pos.x, pos.y, 20.0f), cameraFront.add(pos.x, pos.y, 0.0f), up);
        invView = new Matrix4f(vMatrix).invert();
        return vMatrix;
    }

    public Matrix4f getInvProj() {
        return invProj;
    }

    public Matrix4f getInvView() {
        return invView;
    }

    public Vector2f getPSize() {
        return pSize;
    }

    public Vector3f getFront() {
        return front;
    }

    public Vector3f getUp() {
        return up;
    }

    public Vector2f getPSizeActual() {
        return pSizeActual;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive() {
        isActive = true;
    }

    // METHODS
    private void calculateActualPSize() {
        this.pSizeActual.x = pSize.x * Diamond.getCurrentEnv().getAspectRatio() / Diamond.getCurrentEnv().getWinSizeAdjustX();
        this.pSizeActual.y = pSize.y / Diamond.getCurrentEnv().getWinSizeAdjustY();
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {
        if (isActive) {
            pMatrix.identity();
            calculateActualPSize();
            pMatrix.ortho(0.0f, this.zoom * pSizeActual.x, 0.0f, this.zoom * pSizeActual.y, 0.0f, 100.0f);
            invProj = new Matrix4f(pMatrix).invert();
        }
    }

    @Override
    public Component copy() {
        return null;
    }
}
