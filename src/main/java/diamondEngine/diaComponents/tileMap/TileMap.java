package diamondEngine.diaComponents.tileMap;

import diamondEngine.Camera;
import diamondEngine.Diamond;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaRenderer.DebugRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import sapphire.imgui.windows.GameViewWindow;

import java.util.ArrayList;
import java.util.List;

public class TileMap implements Component {

    // ATTRIBUTES
    private TileSet tileSet;
    private List<Tile> tiles;
    private int cellX;
    private int cellY;
    private boolean drawGrid;

    // RUNTIME ATTRIBUTES
    private transient Vector3f gridColor;

    // CONSTRUCTORS
    public TileMap(int cell) {
        super();
        this.tileSet = null;
        this.tiles = new ArrayList<>();
        this.cellX = cell;
        this.cellY = cell;
        this.drawGrid = true;
        this.gridColor = new Vector3f(0.333f, 0.333f, 0.333f);
    }

    public TileMap(int cellX, int cellY) {
        super();
        this.tileSet = null;
        this.tiles = new ArrayList<>();
        this.cellX = cellX;
        this.cellY = cellY;
        this.drawGrid = true;
        this.gridColor = new Vector3f(0.333f, 0.333f, 0.333f);
    }

    // GETTERS & SETTERS
    public TileSet getTileSet() {
        return tileSet;
    }

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public float getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public float getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    // METHODS
    @Override
    public void init() {
        this.gridColor = new Vector3f(0.444f, 0.444f, 0.444f);
    }

    @Override
    public void update(float dt) {
        // The tileSet grid is drawn if it is set to be visible
        drawGrid();
    }

    @Override
    public Component copy() {
        return new TileMap(cellX, cellY);
    }

    @Override
    public void destroy() {

    }

    private void drawGrid() {
        if (drawGrid) {
            Camera camera = GameViewWindow.editorCamera;
            Vector2f cameraPos = camera.pos;
            Vector2f pSize = camera.getPSizeActual();

            if (camera.zoom <= 4) {

                // RUNTIME ATTRIBUTES
                float cellNX = Diamond.getCurrentEnv().getAspectRatio() * cellX / Diamond.getCurrentEnv().getFrameX();
                float cellNY = (float) cellY / Diamond.getCurrentEnv().getFrameY();
                int numVLines = (int) (pSize.x / cellNX) + 3;
                int numHLines = (int) (pSize.y / cellNY) + 3;
                float width = cellNY * 2 + pSize.x;
                float height = cellNY * 2 + pSize.y;
                float firstX = ((int) (cameraPos.x / cellNX) - 1) * cellNX;
                float firstY = ((int) (cameraPos.y / cellNY) - 1) * cellNY;
                int maxLines = Math.max(numVLines, numHLines);

                float x = 0;
                float y = 0;
                for (int i = 0; i < maxLines; i++) {
                    x = firstX + (cellNX * i);
                    y = firstY + (cellNY * i);
                    if (i < numHLines) {
                        DebugRenderer.addLine(new Vector2f(firstX, y), new Vector2f(width + firstX, y), gridColor);
                    }
                    if (i < numVLines) {
                        DebugRenderer.addLine(new Vector2f(x, firstY), new Vector2f(x, height + firstY), gridColor);
                    }
                }
            }
        }
    }
}
