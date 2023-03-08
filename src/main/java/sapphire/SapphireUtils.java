package sapphire;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.nfd.NFDPathSet;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class SapphireUtils {

    /**
     * Opens native OS to create a file.
     * @return String with the path for the new file
     */
    public static String createFile() {

        PointerBuffer pb = PointerBuffer.allocateDirect(2048);
        int result = NFD_SaveDialog((ByteBuffer)null, (ByteBuffer)null, pb);
        String path = null;

        switch (result) {
            case NFD_OKAY:
                path = pb.getStringUTF8();

                break;
            case NFD_CANCEL:
                DiaLogger.log("User cancelled file dialog");
                break;
            default: // NFD_ERROR
                DiaLogger.log("Error while opening file dialog: " + NFD_GetError(), DiaLoggerLevel.ERROR);
        }

        pb.free();
        return path;
    }

    /**
     * Opens native OS multiple file selector.
     * @return Array of strings containing the paths of the selected files.
     */
    public static String[] selectFiles() {

        NFDPathSet pathSet = NFDPathSet.create();
        int result = NFD_OpenDialogMultiple((ByteBuffer)null, (ByteBuffer)null, pathSet);
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

        pathSet.free();
        NFD_PathSet_Free(pathSet);
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
     * @param extension Extension of the files to retrieve.
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
