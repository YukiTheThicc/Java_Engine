package diamondEngine.diaUtils;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class DiaUtils {

    private static SimpleDateFormat sdf;

    // METHODS
    public static void init() {
        DiaUtils.sdf = new SimpleDateFormat("hh:mm:ss.SSS");
        DiaLogger.log("Initializing Diamond utilities...");
    }

    public static String getTime() {
        return DiaUtils.sdf.format(Calendar.getInstance().getTime());
    }

    public static String getOS() {
        return System.getProperty("os.name");
    }

    /**
     * Opens native OS to save a file. Returns null when the file has not been selected (cancelled or failed)
     * @return String with the path for the new file or null.
     */
    public static File saveFile() {
        return saveFile("");
    }

    /**
     * Opens native OS to save a file. Returns null when the file has not been selected (cancelled or failed)
     * @return String with the path for the new file or null.
     */
    public static File saveFile(String defaultFile) {

        DiaLogger.log("Creating file...");
        String path = tinyfd_saveFileDialog("", defaultFile, null, "");
        File file = null;
        if (path != null && !path.isEmpty()) {
            file = new File(path);
            try {
                if (file.createNewFile()) {
                    DiaLogger.log("Overwriting file '" + file.getAbsolutePath() + "'");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    /**
     * Saves into an array of lines into a file. Overrides all data from said file.
     * @param file File in which the data is going to be stored
     * @param lines Array of Strings representing the lines to write
     */
    public static void saveToFile(File file, String[] lines) {
        if (file != null && file.exists() && file.isFile()) {
            if (lines != null && lines.length > 0) {
                try {
                    DiaLogger.log("Trying save to file...");
                    Files.write(file.toPath(), Arrays.asList(lines), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    DiaLogger.log("Failed save to file: '" + file.getAbsolutePath() + "'", DiaLoggerLevel.ERROR);
                }
            } else {
                DiaLogger.log("Null or empty lines", DiaLoggerLevel.ERROR);
            }
        } else {
            DiaLogger.log("Invalid file", DiaLoggerLevel.ERROR);
        }
    }

    /**
     * Reads all bytes from a file.
     * @param file File to read the bytes from
     * @return Not null array of bytes
     */
    public static byte[] readAllBytes(File file) {
        byte[] data = new byte[0];
        if (file.exists()) {
            try {
                data = Files.readAllBytes(Paths.get(file.getPath()));
            } catch (IOException e) {
                DiaLogger.log("Failed to load data from file '" + file.getPath() + "'", DiaLoggerLevel.ERROR);
            }
        }
        return data;
    }

    /**
     * Opens native OS multiple file selector.
     * @return Array of strings containing the paths of the selected files.
     */
    public static String[] selectFiles() {

        DiaLogger.log("Selecting files...");
        String result = tinyfd_openFileDialog("", null, null, null, true);
        if (result != null && !result.isEmpty()) {
            return result.split("\\|");
        }
        return null;
    }

    /**
     * Opens a single file selection dialog
     * @return String containing the path of the selected file.
     */
    public static String selectFile() {

        DiaLogger.log("Selecting files...");
        String result = tinyfd_openFileDialog("", null, null, null, false);
        if (result != null && !result.isEmpty()) {
            return result;
        }
        return null;
    }

    /**
     * Opens a single directory selection dialog
     * @param title Title for the dialog window
     * @param defaultPath Default path from which the file dialog is going to open
     * @return String containing the path of the selected file.
     */
    public static String selectDirectory(String title, String defaultPath) {

        DiaLogger.log("Selecting files...");
        String result = tinyfd_selectFolderDialog(title, defaultPath);
        if (result != null && !result.isEmpty()) {
            return result;
        }
        return null;
    }

    /**
     * Looks for all files within the specified directory.
     * @param directory Directory in which files are going to be searched.
     * @return List of retrieved files.
     */
    public static ArrayList<File> getFilesInDir(String directory) {
        return getFilesInDir(directory, "");
    }

    /**
     * Looks for all files with the given extension within the specified directory.
     * @param directory Directory in which files are going to be searched.
     * @param extension Extension of the files to retrieve (without dot).
     * @return ArrayList of retrieved files.
     */
    public static ArrayList<File> getFilesInDir(String directory, String extension) {

        File dir = new File(directory);
        ArrayList<File> retrievedFiles = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileExtension = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
                    if (extension != null && !extension.isEmpty() && extension.equals(fileExtension)) {
                        retrievedFiles.add(file);
                    }
                }
            }
        }

        return retrievedFiles;
    }

    /**
     * Gets all folders within the specified directory;
     * @param directory Directory from which to look
     * @return List if found folders
     */
    public static ArrayList<File> getFoldersInDir(String directory) {

        File dir = new File(directory);
        ArrayList<File> retrievedFiles = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    retrievedFiles.add(file);
                }
            }
        }

        return retrievedFiles;
    }


    public static GLFWImage.Buffer loadGLFWImage(String path) {

        GLFWImage.Buffer imageBuffer = null;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {

            IntBuffer comp = stack.mallocInt(1);
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            buffer = stbi_load(new File("sapphire/icon.png").getAbsolutePath(), width, height, comp, 4);
            if (buffer != null) {

                GLFWImage image = GLFWImage.malloc();
                imageBuffer = GLFWImage.malloc(1);
                image.set(width.get(), height.get(), buffer);
                imageBuffer.put(0, image);
            }
        }

        return imageBuffer;
    }
}
