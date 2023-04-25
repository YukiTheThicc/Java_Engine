package diamondEngine.diaUtils;

import diamondEngine.diaRenderer.Shader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DiaAssetManager {

    private static Map<String, Shader> shaders = new HashMap<>();

    public static Shader getShader(String name) {
        return shaders.getOrDefault(name, null);
    }

    public static Shader addShader(String name, String path) {
        File file = new File(path);
        if (DiaAssetManager.shaders.containsKey(file.getAbsolutePath())) {
            return DiaAssetManager.shaders.get(name);
        } else {
            Shader shader = new Shader(name, path);
            shader.compile();
            DiaAssetManager.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }
}
