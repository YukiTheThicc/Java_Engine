package diamondEngine.diaRenderer;

import diamondEngine.diaUtils.DiaAssetManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import sapphire.imgui.windows.GameViewWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugRenderer {

    // ATTRIBUTES
    private static final int MAX_LINES = 4096;
    private static final List<Line> lines = new ArrayList<>();
    private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static final Shader shader = DiaAssetManager.addShader("debugLine2D", "diamond/res/shaders/debugLine2D.glsl");
    private static int vaoID;
    private static int vboID;
    private static boolean started = false;

    // METHODS
    public static void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glLineWidth(2.0f);
    }

    public static void beginFrame() {
        if (!started) {
            start();
            started = true;
        }
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0) {
                lines.remove(i);
                i--;
            }
        }
    }

    // =================================================================================================================
    //  ADD LINES
    // =================================================================================================================
    public static void addLine(Vector2f from, Vector2f to) {
        addLine(from, to, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color) {
        addLine(from, to, color, 1);
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (lines.size() >= MAX_LINES) return;
        DebugRenderer.lines.add(new Line(from, to, color, lifetime));
    }

    public static void draw() {
        if (lines.size() <= 0) return;
        int index = 0;
        for (Line line : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f pos = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                vertexArray[index] = pos.x;
                vertexArray[index + 1] = pos.y;
                vertexArray[index + 2] = -10.0f;

                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        shader.use();
        shader.uploadMat4f("uProjection", GameViewWindow.editorCamera.getpMatrix());
        shader.uploadMat4f("uView", GameViewWindow.editorCamera.getvMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_LINES, 0 , lines.size());

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }
}
