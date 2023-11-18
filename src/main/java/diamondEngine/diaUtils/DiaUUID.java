package diamondEngine.diaUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiaUUID {

    private static final Pattern pattern = Pattern.compile("[0-9A-F]{8}-[0-9A-F]{7}", Pattern.CASE_INSENSITIVE);

    public static String generateUUID() {

        String timeFrag = String.format("%1$08X", System.currentTimeMillis());                  // Time fragment for the UUID
        String randomFrag = String.format("%1$07X", new Random().nextInt()).substring(0, 7);    // Random number fragment

        return timeFrag.substring(timeFrag.length() - 9, timeFrag.length() - 1) + "-" + randomFrag;
    }

    public static String checkUUID(String tentative) {
        Matcher matcher = pattern.matcher(tentative);
        if (matcher.find()) return tentative;
        return null;
    }
}
