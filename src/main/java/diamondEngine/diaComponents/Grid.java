package diamondEngine.diaComponents;

import diamondEngine.Diamond;
import diamondEngine.diaRenderer.DebugRenderer;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import sapphire.Sapphire;
import sapphire.imgui.SappImGuiUtils;
import sapphire.imgui.windows.GameViewWindow;

public class Grid extends Component {

    // ATTRIBUTES
    private int cellX;
    private int cellY;
    private boolean draw;

    // RUNTIME ATTRIBUTES
    private transient float cellNX;
    private transient float cellNY;
    private transient int numHLines = 0;
    private transient int numVLines = 0;
    private transient float width = 0;
    private transient float height = 0;
    private transient Vector3f color;

    // CONSTRUCTORS
    public Grid(int cell) {
        super();
        this.cellX = cell;
        this.cellY = cell;
        this.draw = true;
        this.cellNX = (float) cellX / Diamond.getCurrentEnv().getFrameX();
        this.cellNY = (float) cellY / Diamond.getCurrentEnv().getFrameY();
        this.color = new Vector3f(0.333f, 0.333f, 0.333f);
    }

    public Grid(int cellX, int cellY) {
        super();
        this.cellX = cellX;
        this.cellY = cellY;
        this.draw = true;
        this.cellNX = (float) cellX / Diamond.getCurrentEnv().getFrameX();
        this.cellNY = (float) cellY / Diamond.getCurrentEnv().getFrameY();
        this.color = new Vector3f(0.333f, 0.333f, 0.333f);
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
    public void init() {
        this.cellNX = (float) cellX / Diamond.getCurrentEnv().getFrameX();
        this.cellNY = (float) cellY / Diamond.getCurrentEnv().getFrameY();
        this.color = new Vector3f(0.333f, 0.333f, 0.333f);
    }

    @Override
    public void update(float dt) {
        if (draw) {
            Camera camera = GameViewWindow.editorCamera;
            Vector2f cameraPos = camera.pos;
            Vector2f pSize = camera.getPSize();
            float adjustX = Diamond.getCurrentEnv().getWinSizeAdjustX();
            float adjustY = Diamond.getCurrentEnv().getWinSizeAdjustY();

            if (camera.zoom <= 4) {

                numHLines = (int) ((camera.zoom / cellNY) * pSize.y / adjustY) + 3;
                numVLines = (int) ((camera.zoom / cellNX) * pSize.x / adjustX) + 3;
                width = cellNX * 2 + camera.zoom * pSize.x / adjustX;
                height = cellNY * 2 + camera.zoom * pSize.y / adjustY;
                float firstX = ((int) (cameraPos.x / cellNX) - 1) * cellNX;
                float firstY = ((int) (cameraPos.y / cellNY) - 1) * cellNY;
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
        cellNX = (float) cellX / Diamond.getCurrentEnv().getFrameX();
        cellNY = (float) cellY / Diamond.getCurrentEnv().getFrameY();
    }

    @Override
    public void imgui() {
        SappImGuiUtils.textLabel("UUID", this.getUuid());
        ImInt cellX = new ImInt(this.cellX);
        ImInt cellY = new ImInt(this.cellY);
        if (SappImGuiUtils.dragInt(Sapphire.getLiteral("cell_height"), cellY)) {
            cameraChanged(cellX.get(), cellY.get());
        }
        if (SappImGuiUtils.dragInt(Sapphire.getLiteral("cell_width"), cellX)) {
            cameraChanged(cellX.get(), cellY.get());
        }
        if (SappImGuiUtils.checkboxLabel(Sapphire.getLiteral("draw_grid"), draw)) {
            this.draw = !draw;
        }
        SappImGuiUtils.textLabel("Cells", (numHLines - 1) * (numVLines - 1) + " (" + (numVLines - 1) + "x" + (numHLines - 1) + ")");
    }

    @Override
    public Component copy() {
        return new Grid(cellX, cellY);
    }
}
