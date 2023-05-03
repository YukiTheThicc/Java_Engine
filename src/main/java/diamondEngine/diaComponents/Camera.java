package diamondEngine.diaComponents;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    // ATTRIBUTES
    private Matrix4f pMatrix, vMatrix, invProj, invView;
    private Vector2f pSize;
    private float zoom = 1.0f;
    public Vector2f pos;

    // CONSTRUCTORS
    public Camera(Vector2f pos, float pWidth, float pHeight) {
        this.pos = pos;
        this.pSize = new Vector2f(pWidth, pHeight);
        this.pMatrix = new Matrix4f();
        this.vMatrix = new Matrix4f();
        this.invProj = new Matrix4f();
        this.invView = new Matrix4f();
        this.changeProjection();
    }

    // GETTERS & SETTERS
    public Matrix4f getProjMatrix() {
        return this.pMatrix;
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        vMatrix.identity();
        vMatrix.lookAt(new Vector3f(pos.x, pos.y, 20.0f),
                cameraFront.add(pos.x, pos.y, 0.0f), cameraUp);
        invView =  new Matrix4f(vMatrix).invert();
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
