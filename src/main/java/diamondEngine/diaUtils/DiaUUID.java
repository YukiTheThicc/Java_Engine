package diamondEngine.diaUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiaUUID {

    private static final Pattern pattern = Pattern.compile("[0-9A-F]{6}-[0-9A-F]{8}", Pattern.CASE_INSENSITIVE);

    public static String generateUUID() {

        String fullTimeFrag = String.format("%1$014X", System.currentTimeMillis());   // Time fragment for the UUID
        String timeFrag = fullTimeFrag.substring(fullTimeFrag.length() - 7, fullTimeFrag.length() - 1);   // Time fragment for the UUID
        String randomFrag = String.format("%1$014X", new Random().nextLong()).substring(0, 8);    // Random number fragment

        return timeFrag + "-" + randomFrag;
    }

    public static String checkUUID(String tentative) {
        Matcher matcher = pattern.matcher(tentative);
        String toReturn = "";
        if (matcher.find()) {
            toReturn = tentative;
        } else {
            toReturn = generateUUID();
        }
        return toReturn;
    }
}
