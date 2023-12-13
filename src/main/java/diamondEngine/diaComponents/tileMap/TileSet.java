package diamondEngine.diaComponents.tileMap;

import diamondEngine.diaAssets.Texture;

import java.util.ArrayList;
import java.util.List;

public class TileSet {

    // ATTRIBUTES
    private Texture texture;
    private int numHTiles;
    private int numVTiles;
    private List<Tile> tiles;

    // CONTRUCTORS
    public TileSet(Texture texture, int numHTiles, int numVTiles) {
        this.texture = texture;
        this.numHTiles = numHTiles;
        this.numVTiles = numVTiles;
        this.tiles = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getNumHTiles() {
        return numHTiles;
    }

    public void setNumHTiles(int numHTiles) {
        this.numHTiles = numHTiles;
    }

    public int getNumVTiles() {
        return numVTiles;
    }

    public void setNumVTiles(int numVTiles) {
        this.numVTiles = numVTiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    // METHODS

}
