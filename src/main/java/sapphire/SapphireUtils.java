package sapphire;

import diamondEngine.diaUtils.DiaConsole;
import org.lwjgl.util.nfd.NFDPathSet;
import java.nio.ByteBuffer;

import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class SapphireUtils {

    public static String[] selectFiles() {

        NFDPathSet pathSet = NFDPathSet.create();
        int result = NFD_OpenDialogMultiple((ByteBuffer)null, (ByteBuffer)null, pathSet);
        String[] paths = null;

        switch (result) {
            case NFD_OKAY:

                long numFiles = NFD_PathSet_GetCount(pathSet);
                paths = new String[(int)numFiles];
                for (long i = 0; i < numFiles; i++) {
                    DiaConsole.log( "File dialog selected path #"+ (i + 1) + ": '" + NFD_PathSet_GetPath(pathSet, i) + "'", "debug");
                    paths[(int)i] = NFD_PathSet_GetPath(pathSet, i);
                }
                pathSet.free();
                break;
            case NFD_CANCEL:
                DiaConsole.log("User cancelled file dialog", "debug");
                break;
            default: // NFD_ERROR
                DiaConsole.log("Error while opening file dialog: " + NFD_GetError(), "error");
        }

        NFD_PathSet_Free(pathSet);
        return paths;
    }
}
