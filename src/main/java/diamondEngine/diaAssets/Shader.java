package diamondEngine.diaAssets;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    // ATTRIBUTES
    private int programId;
    private boolean inUse = false;
    private String name;
    private String vertex;
    private String fragment;

    // CONSTRUCTORS
    /**
     * Creates a shader object from a single file. It expects to find both the vertex and fragment shader sources on the
     * file. Identify each program within the file by putting a comment like '#type vertex' and '#type fragment' before
     * the start of each program.
     *
     * @param name Name that identifies the shader
     * @param path Path of the file with the shaders sources
     */
    public Shader(String name, String path) {

        this.name = name;
        this.programId = -1;
        try {
            String source = new String(Files.readAllBytes(Paths.get(path)));
            String[] sources = source.split("(#type)( )+([a-zA-Z])+");

            // Find the first pattern after #type
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after #type
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertex = sources[1];
            } else if (firstPattern.equals("fragment")) {
                fragment = sources[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertex = sources[2];
            } else if (secondPattern.equals("fragment")) {
                fragment = sources[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Failed shader file opening for: '" + path + "'";
        }
    }

    /**
     * Creates a shader object form independent vertex and fragment source files.
     *
     * @param name Name that identifies the shader
     * @param vertexPath Path of the file that contains the vertex shader source
     * @param fragmentPath Path of the file that contains the fragment shader source
     */
    public Shader(String name, String vertexPath, String fragmentPath) {

        this.name = name;
        try {
            this.vertex = new String(Files.readAllBytes(Paths.get(vertexPath)));
        } catch (IOException e) {
            e.printStackTrace();
            DiaLogger.log(Shader.class, name + ": Failed while trying to load vertex shader from '" + vertexPath + "'", DiaLoggerLevel.ERROR);
        }

        try {
            this.fragment = new String(Files.readAllBytes(Paths.get(fragmentPath)));
        } catch (IOException e) {
            e.printStackTrace();
            DiaLogger.log(Shader.class, name + ": Failed while trying to load vertex shader from '" + fragmentPath + "'", DiaLoggerLevel.ERROR);
        }
    }

    /**
     * Compile the shader. If compilation fails at some point, the program id will be set to -1.
     */
    public void compile() {

        int vertexId, fragmentId;
        boolean failed = false;

        // Vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, vertex);
        glCompileShader(vertexId);
        if (glGetShaderi(vertexId, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            DiaLogger.log(Shader.class, "Failed vertex shader compilation for '" + name + "': " + glGetShaderInfoLog(vertexId, length), DiaLoggerLevel.ERROR);
            failed = true;
        }

        // Fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, fragment);
        glCompileShader(fragmentId);
        if (glGetShaderi(fragmentId, GL_COMPILE_STATUS) == GL_FALSE) {
            int length = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            DiaLogger.log(Shader.class, "Failed fragment shader compilation for '" + name + "': " + glGetShaderInfoLog(vertexId, length), DiaLoggerLevel.ERROR);
            failed = true;
        }

        if (!failed) {
            programId = glCreateProgram();
            glAttachShader(programId, vertexId);
            glAttachShader(programId, fragmentId);
            glLinkProgram(programId);

            if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
                programId = -1;
                int length = glGetProgrami(programId, GL_INFO_LOG_LENGTH);
                DiaLogger.log(Shader.class, "Failed while trying to link shader program '" + name + "': \n" + glGetProgramInfoLog(programId, length), DiaLoggerLevel.ERROR);
            }
        }
    }

    /**
     * Use this shader. If the shader is not compiled or already in use it won't do anything.
     */
    public void use() {
        if (!inUse && this.programId > 0) {
            inUse = true;
            glUseProgram(this.programId);
        }
    }

    /**
     * Detach this shader.
     */
    public void detach() {
        inUse = false;
        glUseProgram(0);
    }

    public void uploadMat4f(String name, Matrix4f mat) {
        int location = glGetUniformLocation(programId, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        glUniformMatrix4fv(location, false, matBuffer);
    }

    public void uploadMat3f(String name, Matrix3f mat) {
        int location = glGetUniformLocation(programId, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix3fv(location, false, matBuffer);
    }

    public void uploadVec4f(String name, Vector4f vec) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String name, Vector3f vec) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform3f(location, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String name, Vector2f vec) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform2f(location, vec.x, vec.y);
    }

    public void uploadFloat(String name, float value) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform1f(location, value);
    }

    public void uploadInt(String name, int value) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform1i(location, value);
    }

    public void uploadTexture(String name, int slot) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform1i(location, slot);
    }

    public void uploadIntArray(String name, int[] array) {
        int location = glGetUniformLocation(programId, name);
        use();
        glUniform1iv(location, array);
    }
}
