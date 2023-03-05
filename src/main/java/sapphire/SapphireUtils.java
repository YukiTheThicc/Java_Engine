package sapphire;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import org.lwjgl.util.nfd.NFDPathSet;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class SapphireUtils {

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
                pathSet.free();
                break;
            case NFD_CANCEL:
                DiaLogger.log("User cancelled file dialog");
                break;
            default: // NFD_ERROR
                DiaLogger.log("Error while opening file dialog: " + NFD_GetError(), DiaLoggerLevel.ERROR);
        }

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
     * @return List of retrieved files.
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
                        DiaLogger.log("Found file with extension '" + extension + "': " + file.getPath());
                    }
                }
            }
        }

        return retrievedFiles;
    }
}
