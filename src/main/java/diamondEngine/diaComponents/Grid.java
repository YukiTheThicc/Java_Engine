package diamondEngine.diaComponents;

import diamondEngine.Diamond;
import diamondEngine.Environment;
import diamondEngine.diaRenderer.DebugRenderer;
import diamondEngine.diaUtils.DiaLogger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import sapphire.Sapphire;
import sapphire.imgui.SappImGui;
import sapphire.imgui.windows.GameViewWindow;

public class Grid extends Component {

    // ATTRIBUTES
    private int cellX;
    private int cellY;
    private int numHLines = 0;
    private int numVLines = 0;
    private float firstX;
    private float firstY;
    private float width;
    private float height;
    private boolean draw;
    private final Vector3f color = new Vector3f(0.688f, 0.688f, 0.688f);
    private final Vector3f colorR = new Vector3f(1f, 0F, 0f);
    private final Vector3f colorG = new Vector3f(0f, 1f, 0f);

    // CONSTRUCTORS
    public Grid(int cell) {
        super();
        this.cellX = cell;
        this.cellY = cell;
        this.draw = true;
    }

    public Grid(int cellX, int cellY) {
        super();
        this.cellX = cellX;
        this.cellY = cellY;
        this.draw = true;
    }

    // GETTERS & SETTERS
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
    public void update(float dt) {
        if (draw) {
            Camera camera = GameViewWindow.editorCamera;
            Vector2f cameraPos = camera.pos;
            Vector2f projectionSize = camera.getProjectionSize();

            DebugRenderer.addLine(new Vector2f(0, 0), new Vector2f(1, 1), colorR);

            if (camera.getZoom() <= 4) {

                firstX = ((float) Math.floor(cameraPos.x / cellX)) * cellY;
                firstY = ((float) Math.floor(cameraPos.y / cellY)) * cellY;

                numHLines = (int) ((projectionSize.y * camera.getZoom()) / cellY) + 2;
                numVLines = (int) ((projectionSize.x * camera.getZoom()) / cellX) + 2;

                width = (projectionSize.x * camera.getZoom()) + (cellX * 5);
                height = (projectionSize.y * camera.getZoom()) + (cellY * 5);

                int maxLines = Math.max(numVLines, numHLines);

                float x = 0;
                float y = 0;
                for (int i = 0; i < maxLines; i++) {
                    x = firstX + (cellX * i);
                    y = firstY + (cellY * i);

                    if (i < numHLines) {
                        DebugRenderer.addLine(new Vector2f(firstX, y), new Vector2f(width + firstX, y), color);
                    }

                    if (i < numVLines) {
                        DebugRenderer.addLine(new Vector2f(x, firstY), new Vector2f(x, height + firstY), color);
                    }
                }
            }
        }
    }

    @Override
    public void imgui() {
        cellX = SappImGui.dragInt(Sapphire.getLiteral("cell_width"), cellX);
        cellY = SappImGui.dragInt(Sapphire.getLiteral("cell_height"), cellY);
        SappImGui.textLabel("numHLines", "" + numHLines);
        SappImGui.textLabel("numVLines", "" + numVLines);
        SappImGui.textLabel("firstX", "" + firstX);
        SappImGui.textLabel("firstY", "" + firstY);
        SappImGui.textLabel("width", "" + width);
        SappImGui.textLabel("height", "" + height);
    }
}
