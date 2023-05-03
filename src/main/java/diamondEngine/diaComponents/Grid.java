package diamondEngine.diaComponents;

import diamondEngine.Diamond;
import diamondEngine.diaRenderer.DebugRenderer;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import sapphire.Sapphire;
import sapphire.imgui.SappImGui;
import sapphire.imgui.windows.GameViewWindow;

public class Grid extends Component {

    // ATTRIBUTES
    private int cellX;
    private int cellY;
    private float cellNX;
    private float cellNY;
    private int numHLines = 0;
    private int numVLines = 0;
    private boolean draw;
    private final Vector3f color = new Vector3f(0.333f, 0.333f, 0.333f);

    // CONSTRUCTORS
    public Grid(int cell) {
        super();
        this.cellX = cell;
        this.cellY = cell;
        this.cellNX = (float) cellX / Diamond.currentEnv.getFrameX();
        this.cellNY = (float) cellY / Diamond.currentEnv.getFrameY();
        this.draw = true;
    }

    public Grid(int cellX, int cellY) {
        super();
        this.cellX = cellX;
        this.cellY = cellY;
        this.cellNX = (float) cellX / Diamond.currentEnv.getFrameX();
        this.cellNY = (float) cellY / Diamond.currentEnv.getFrameY();
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
            Vector2f pSize = camera.getPSize();

            if (camera.getZoom() <= 16) {

                float firstX = ((float) Math.floor(cameraPos.x / cellNX)) * cellNX;
                float firstY = ((float) Math.floor(cameraPos.y / cellNY)) * cellNY;
                float width = ((pSize.x * camera.getZoom()) / cellNX) + 2;
                float height = ((pSize.y * camera.getZoom()) / cellNY) + 2;
                numVLines = (int) (camera.getZoom() / cellNX);
                numHLines = (int) (numVLines * Diamond.currentEnv.getAspectRatio());

                int maxLines = Math.max(numVLines, numHLines);

                float x = 0;
                float y = 0;
                for (int i = 0; i < maxLines; i++) {
                    x = firstX + (cellNX * i);
                    y = firstY + (cellNY * i);

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

    public void cameraChanged(int cellX, int cellY) {
        this.cellY = cellY;
        this.cellX = cellX;
        cellNX = (float) cellX / Diamond.currentEnv.getFrameX();
        cellNY = (float) cellY / Diamond.currentEnv.getFrameY();
    }

    @Override
    public void imgui() {
        ImInt cellX = new ImInt(this.cellX);
        ImInt cellY = new ImInt(this.cellY);
        if (SappImGui.dragInt(Sapphire.getLiteral("cell_height"), cellY)) {
            cameraChanged(cellX.get(), cellY.get());
        }
        if (SappImGui.dragInt(Sapphire.getLiteral("cell_width"), cellX)) {
            cameraChanged(cellX.get(), cellY.get());
        }
        SappImGui.textLabel("numHLines", "" + numHLines);
        SappImGui.textLabel("numVLines", "" + numVLines);
        SappImGui.textLabel("Debug lines", "" + DebugRenderer.getLinesSize());
    }
}
