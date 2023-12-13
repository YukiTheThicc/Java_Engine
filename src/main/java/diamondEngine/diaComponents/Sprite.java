package diamondEngine.diaComponents;

import diamondEngine.diaAssets.Texture;
import org.joml.Vector2f;

public class Sprite implements Component{

    // ATTRIBUTES
    private final Texture texture;
    private final int spriteWidth;
    private final int spriteHeight;
    private final int numSprites;
    private final int spacing;
    private Vector2f[] texCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    // CONTRUCTORS
    public Sprite(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numSprites = numSprites;
        this.spacing = spacing;
    }

    /**
     *  Creates a new SpriteSheet with the specified texture, the number of sprites is calculated by the size of the texture
     *  in relation to the sprite size and spacing
     */
    public Sprite(Texture texture, int spriteWidth, int spriteHeight, int spacing) {
        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spacing = spacing;
        int spriteSheetX = texture.getWidth() / (spriteWidth + spacing);
        int spriteSheetY = texture.getHeight() / (spriteHeight + spacing);
        this.numSprites = spriteSheetX * spriteSheetY;
    }

    public Sprite() {
        this.texture = null;
        this.spriteWidth = 0;
        this.spriteHeight = 0;
        this.spacing = 0;
        this.numSprites = 0;
    }

    // GETTERS & SETTERS
    public Texture getTexture() {
        return texture;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int getNumSprites() {
        return numSprites;
    }

    public int getSpacing() {
        return spacing;
    }

    // METHODS
    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
