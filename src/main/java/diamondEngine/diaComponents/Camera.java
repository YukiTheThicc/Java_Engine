package diamondEngine.diaComponents;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    // ATTRIBUTES
    private Matrix4f pMatrix, vMatrix, invProj, invView;
    private int pWidth = 6;
    private int pHeight = 3;
    private Vector2f projectionSize = new Vector2f(pWidth, pHeight);
    private float zoom = 1.0f;
    public Vector2f pos;

    // CONSTRUCTORS
    public Camera(Vector2f pos) {
        this.pos = pos;
        this.pMatrix = new Matrix4f();
        this.vMatrix = new Matrix4f();
        this.invProj = new Matrix4f();
        this.invView = new Matrix4f();
        this.changeProjection();
    }

    // GETTERS & SETTERS
    public Matrix4f getpMatrix() {
        return this.pMatrix;
    }

    public Matrix4f getvMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.vMatrix.identity();
        this.vMatrix.lookAt(new Vector3f(pos.x, pos.y, 20.0f),
                cameraFront.add(pos.x, pos.y, 0.0f), cameraUp);
        this.vMatrix.invert(invView);
        return this.vMatrix;
    }

    public Matrix4f getInvProj() {
        return invProj;
    }

    public Matrix4f getInvView() {
        return invView;
    }

    public Vector2f getProjectionSize() {
        return projectionSize;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    // METHODS
    public void changeProjection() {
        pMatrix.identity();
        pMatrix.ortho(0.0f, projectionSize.x * this.zoom, 0.0f, projectionSize.y * this.zoom, 0.0f, 100.0f);
        pMatrix.invert(invProj);
    }

    public void addZoom(float toAdd) {
        this.zoom += toAdd;
    }
}
