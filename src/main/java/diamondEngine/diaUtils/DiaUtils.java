package diamondEngine.diaUtils;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.nfd.NFDPathSet;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static org.lwjgl.util.nfd.NativeFileDialog.*;

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
     * Opens native OS to create a file with the specified default extension.
     * @param filters Default extesion
     * @return String with the path for the new file
     */
    public static File createFile(String filters) {

        String defaultPath = new File("log").getAbsolutePath();
        PointerBuffer pb = PointerBuffer.allocateDirect(2048);
        int result = NFD_SaveDialog(filters, defaultPath, pb);
        File file = null;

        switch (result) {
            case NFD_OKAY:
                file = new File(pb.getStringUTF8());
                break;
            case NFD_CANCEL:
                DiaLogger.log("User cancelled file dialog");
                break;
            default: // NFD_ERROR
                DiaLogger.log("Error while opening file dialog: " + NFD_GetError(), DiaLoggerLevel.ERROR);
        }

        pb.free();
        return file;
    }

    /**
     * Opens native OS to create a file. No extension is set.
     * @return String with the path for the new file
     */
    public static File createFile() {

        DiaLogger.log("Creating file...");
        String filters = "log";
        String defaultPath = new File("log").getAbsolutePath() + "." + filters;
        PointerBuffer pb = PointerBuffer.allocateDirect(2048);
        int result = NFD_SaveDialog(filters, defaultPath, pb);
        File file = null;

        switch (result) {
            case NFD_OKAY:
                file = new File(pb.getStringUTF8());
                try {
                    Files.createFile(file.toPath());
                } catch (IOException e) {
                    DiaLogger.log("Failed to create file with path: " + file.getAbsolutePath(), DiaLoggerLevel.ERROR);
                }
                break;
            case NFD_CANCEL:
                DiaLogger.log("User cancelled file dialog");
                break;
            default: // NFD_ERROR
                DiaLogger.log("Error while opening file dialog: " + NFD_GetError(), DiaLoggerLevel.ERROR);
        }

        pb.free();
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
        NFDPathSet pathSet = NFDPathSet.create();
        int result = NFD_OpenDialogMultiple("", "", pathSet);
        String[] paths = null;

        switch (result) {
            case NFD_OKAY:
                long numFiles = NFD_PathSet_GetCount(pathSet);
                paths = new String[(int)numFiles];
                for (long i = 0; i < numFiles; i++) {
                    DiaLogger.log( "File dialog selected path #"+ (i + 1) + ": '" + NFD_PathSet_GetPath(pathSet, i) + "'");
                    paths[(int)i] = NFD_PathSet_GetPath(pathSet, i);
                }
                break;
            case NFD_CANCEL:
                DiaLogger.log("User cancelled file dialog");
                break;
            default: // NFD_ERROR
                DiaLogger.log("Error while opening file dialog: " + NFD_GetError(), DiaLoggerLevel.ERROR);
        }

        NFD_PathSet_Free(pathSet);
        pathSet.free();
        return paths;
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
}
