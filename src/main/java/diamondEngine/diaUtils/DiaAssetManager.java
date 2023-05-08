package diamondEngine.diaUtils;

import diamondEngine.diaAudio.Sound;
import diamondEngine.diaRenderer.Shader;
import diamondEngine.diaRenderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DiaAssetManager {

    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

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

    public static Sound getSound(String name) {
        return sounds.getOrDefault(name, null);
    }

    public static Sound addSound(String soundFile, boolean loops) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            DiaAssetManager.sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }
}
