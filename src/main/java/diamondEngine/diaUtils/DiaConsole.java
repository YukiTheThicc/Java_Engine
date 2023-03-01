package diamondEngine.diaUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DiaConsole {

    // CONSTANTS
    public static final int INFO = 4;
    public static final int DEBUG = 3;
    public static final int WARN = 2;
    public static final int ERROR = 1;
    public static final int CRITICAL = 0;

    // ATTRIBUTES
    private static File log;
    private static SimpleDateFormat sdf;
    private static int currentLevel;
    private static HashMap<Integer, String> literals;
    public static boolean isLogging;

    // GETTERS & SETTERS
    public int getCurrentLevel() {
        return currentLevel;
    }

    public void changeLevel(int currentLevel) {
        DiaConsole.currentLevel = currentLevel;
    }

    public File getLog() {
        return log;
    }

    public void changeLog(String log) {
        try {
            String dir = log.substring(0, log.lastIndexOf("/") + 1);
            if (log.split("/").length > 1) {
                Files.createDirectories(Paths.get(dir));
            }
            DiaConsole.log = new File(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // METHODS
    public static void init(boolean isLogging) {
        DiaConsole.sdf = new SimpleDateFormat("hh:mm:ss.SSS");
        DiaConsole.currentLevel = DiaConsole.DEBUG;
        DiaConsole.log = new File("log.txt");
        DiaConsole.isLogging = isLogging;

        // Clear log file for new session
        // REVISE: Maybe better to keep ALL log entries for other sessions or creating log file depending on the day
        try {
            PrintWriter writer = new PrintWriter(DiaConsole.log);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            System.err.println("FAILED WHILE CLEARING LOG: '" + DiaConsole.log.getAbsolutePath() + "'");
        }
        DiaConsole.literals = new HashMap<>();
        DiaConsole.literals.put(DiaConsole.INFO, "INFO");
        DiaConsole.literals.put(DiaConsole.DEBUG, "DEBUG");
        DiaConsole.literals.put(DiaConsole.WARN, "WARNING");
        DiaConsole.literals.put(DiaConsole.ERROR, "ERROR");
        DiaConsole.literals.put(DiaConsole.CRITICAL, "CRITICAL");
        DiaConsole.log("--------New Session--------", DiaConsole.CRITICAL);
    }

    private static String getTime() {
        return DiaConsole.sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * Logs a message with DEBUG level of severity.
     *
     * @param message Message to log.
     */
    public static void log(String message) {
        DiaConsole.log(message, DiaConsole.DEBUG);
    }

    /**
     * Logs a message with the specified level of severity.
     *
     * @param message Message to log.
     * @param level   Level of severity.
     */
    public static void log(String message, int level) {
        if (DiaConsole.log != null) {
            if (DiaConsole.currentLevel >= level) {
                try {

                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DiaConsole.log, true)));
                    String levelLit = DiaConsole.literals.get(level);
                    if (levelLit == null) {
                        out.println("[" + DiaConsole.getTime() + "][" + DiaConsole.literals.get(DiaConsole.ERROR) + "] Trying to log with unknown level");
                    }
                    out.println("[" + DiaConsole.getTime() + "][" + levelLit + "] " + message);
                    out.close();

                } catch (IOException e) {
                    System.err.println("FAILED WHILE TRYING TO LOG TO: '" + DiaConsole.log.getAbsolutePath() + "'");
                }
            }
        } else {
            assert false;
        }
    }
}
