package diamondEngine.diaComponents;

import diamondEngine.diaRenderer.DebugRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import sapphire.imgui.windows.GameViewWindow;

public class Grid extends Component {

    // ATTRIBUTES
    private int cellX;
    private int cellY;
    private boolean draw;

    // CONSTRUCTORS
    public Grid(int cell) {
        this.cellX = cell;
        this.cellY = cell;
        this.draw = false;
    }

    public Grid(int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.draw = false;
    }

    // GETTERS & SETTERS
    public int getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    // METHODS
    @Override
    public void update(float dt) {
        if (draw) {
            Camera camera = GameViewWindow.editorCamera;
            Vector2f cameraPos = camera.pos;
            Vector2f projectionSize = camera.getProjectionSize();

            if (camera.getZoom() <= 4) {
                float firstX = ((int) (cameraPos.x / cellX) - 1) * cellX;
                float firstY = ((int) (cameraPos.y / cellY) - 1) * cellY;

                int numHLines = (int) ((projectionSize.y * camera.getZoom()) / cellY) + 2;
                int numVLines = (int) ((projectionSize.x * camera.getZoom()) / cellX) + 2;

                float width = (int) (projectionSize.x * camera.getZoom()) + cellX * 2;
                float height = (int) (projectionSize.y * camera.getZoom()) + cellY * 2;

                Vector3f color = new Vector3f(0.66f, 0.66f, 0.66f);

                int maxLines = Math.max(numVLines, numHLines);

                float x = 0;
                float y = 0;
                int i = 0;
                for (i = 0; i < maxLines; i++) {
                    x = firstX + (cellX * i);
                    y = firstY + (cellY * i);

                    if (i < numHLines) {
                        DebugRenderer.addLine(new Vector2f(firstX, y), new Vector2f(width + firstX + 1, y), color);
                    }

                    if (i < numVLines) {
                        DebugRenderer.addLine(new Vector2f(x, firstY), new Vector2f(x, height + firstY + 1), color);
                    }
                }
            }
        }
    }
}
