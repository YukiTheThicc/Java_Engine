package diamondEngine.diaUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
}
