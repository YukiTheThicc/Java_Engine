package diamondEngine.diaAssets;

import org.joml.Vector2f;

public class Sprite {

    // ATTRIBUTES
    private float width, height;
    private Texture texture = null;
    private Vector2f[] texCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    // CONSTRUCTORS
    public Sprite() {

    }

    // GETTERS & SETTERS
    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    // Methods
    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }
}
