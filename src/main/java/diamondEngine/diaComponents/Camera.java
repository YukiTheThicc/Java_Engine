package diamondEngine.diaComponents;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    // ATTRIBUTES
    private Matrix4f pMatrix;
    private Matrix4f vMatrix;
    private Matrix4f invProj;
    private Matrix4f invView;
    private Vector3f front;
    private Vector3f up;
    private Vector2f pSize;
    public Vector2f pos;
    private float zoom = 1.0f;

    // CONSTRUCTORS
    public Camera(Vector2f pos, float pWidth, float pHeight) {
        this.pos = pos;
        this.pSize = new Vector2f(pWidth, pHeight);
        this.pMatrix = new Matrix4f();
        this.vMatrix = new Matrix4f();
        this.invProj = new Matrix4f();
        this.invView = new Matrix4f();
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.changeProjection();
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

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
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

    // METHODS
    public void changeProjection() {
        pMatrix.identity();
        pMatrix.ortho(0.0f, this.zoom * pSize.x, 0.0f, this.zoom * pSize.y, 0.0f, 100.0f);
        invProj = new Matrix4f(pMatrix).invert();
    }

    public void addZoom(float toAdd) {
        this.zoom += toAdd;
    }
}
