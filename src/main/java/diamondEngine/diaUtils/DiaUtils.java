package diamondEngine.diaUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DiaUtils {

    private static DiaUtils diaUtils = null;
    private SimpleDateFormat sdf;

    // CONSTRUCTORS
    private DiaUtils() {

    }

    // GETTERS & SETTERS

    // METHODS
    public void init() {
        this.sdf = new SimpleDateFormat("hh:mm:ss.SSS");
    }

    public static DiaUtils get() {
        if (diaUtils == null) {
            diaUtils = new DiaUtils();
        }
        return diaUtils;
    }

    public String getTime() {
        return this.sdf.format(Calendar.getInstance().getTime());
    }
}
