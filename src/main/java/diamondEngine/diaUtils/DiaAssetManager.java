package diamondEngine.diaUtils;

import diamondEngine.diaRenderer.Shader;

import java.util.HashMap;
import java.util.Map;

public class DiaAssetManager {

    private static Map<String, Shader> shaders = new HashMap<>();

    public static Shader getShader(String name) {
        return shaders.getOrDefault(name, null);
    }

    public static void addShader(Shader shader) {

    }
}
