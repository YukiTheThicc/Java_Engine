package diamondEngine.diaUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DiaLogger extends Thread {

    // ATTRIBUTES
    private static DiaLogger diaLogger;
    private static File log;
    private static SimpleDateFormat sdf;
    private static HashMap<DiaLoggerLevel, String> literals;
    private static ArrayList<DiaLoggerObserver> observers;
    private static ArrayList<String> entryBuffer;
    private static ArrayList<DiaLoggerLevel> levelBuffer;
    private static DiaLoggerLevel currentLevel;
    private static boolean isLogging;
    private static boolean isInitialized;

    // GETTERS & SETTERS
    public static DiaLoggerLevel getCurrentLevel() {
        return currentLevel;
    }

    public static void changeLevel(DiaLoggerLevel currentLevel) {
        DiaLogger.currentLevel = currentLevel;
    }

    public static File getLog() {
        return log;
    }

    public void changeLog(String log) {
        try {
            String dir = log.substring(0, log.lastIndexOf("/") + 1);
            if (log.split("/").length > 1) {
                Files.createDirectories(Paths.get(dir));
            }
            DiaLogger.log = new File(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isIsLogging() {
        return isLogging;
    }

    public static void setIsLogging(boolean isLogging) {
        DiaLogger.isLogging = isLogging;
    }

    public static boolean isIsInitialized() {
        return isInitialized;
    }

    public static void setIsInitialized(boolean isInitialized) {
        DiaLogger.isInitialized = isInitialized;
    }

    public static void addObserver(DiaLoggerObserver observer) {
        DiaLogger.observers.add(observer);
        // If no observer was added previously the buffers are dumped to the observers
        if (DiaLogger.observers.size() == 1) {
            dumpLogBuffers();
        }
    }

    // METHODS
    public static void init() {
        DiaLogger.diaLogger = new DiaLogger();
        DiaLogger.log = new File("log.txt");
        DiaLogger.literals = new HashMap<>();
        DiaLogger.sdf = new SimpleDateFormat("hh:mm:ss.SSS");
        DiaLogger.observers = new ArrayList<>();
        DiaLogger.entryBuffer = new ArrayList<>();
        DiaLogger.levelBuffer = new ArrayList<>();
        DiaLogger.currentLevel = DiaLoggerLevel.DEBUG;
        DiaLogger.isLogging = true;
        DiaLogger.isInitialized = true;

        // Clear log file for new session
        // REVISE: Maybe better to keep ALL log entries for other sessions or creating log file depending on the day
        try {
            PrintWriter writer = new PrintWriter(DiaLogger.log);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            System.err.println("FAILED WHILE CLEARING LOG: '" + DiaLogger.log.getAbsolutePath() + "'");
        }

        DiaLogger.literals.put(DiaLoggerLevel.INFO, "INFO");
        DiaLogger.literals.put(DiaLoggerLevel.DEBUG, "DEBUG");
        DiaLogger.literals.put(DiaLoggerLevel.WARN, "WARNING");
        DiaLogger.literals.put(DiaLoggerLevel.ERROR, "ERROR");
        DiaLogger.literals.put(DiaLoggerLevel.CRITICAL, "CRITICAL");
        DiaLogger.log("--------New Session--------", DiaLoggerLevel.CRITICAL);

        diaLogger.start();
    }

    private static String getTime() {
        return DiaLogger.sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * Logs a message with DEBUG level of severity.
     *
     * @param message Message to log.
     */
    public static void log(String message) {
        DiaLogger.log(message, DiaLoggerLevel.DEBUG);
    }

    /**
     * Logs a message with the specified level of severity. Notifies attached observers of the new entry, complete with
     * time and log level.
     *
     * @param message Message to log.
     * @param level   Level of severity.
     */
    public static void log(String message, DiaLoggerLevel level) {

        if (DiaLogger.log != null) {
            if (DiaLogger.currentLevel.ordinal() >= level.ordinal()) {

                String toLog = "[" + DiaLogger.getTime() + "][" + level + "] " + message;
                // Notify observers of log entry
                if (observers != null && !observers.isEmpty()) {
                    for (DiaLoggerObserver observer : DiaLogger.observers) {
                        observer.newEntry(toLog, level);
                    }
                } else {
                    // If there are no observers the log entries are stored on buffers to be dumped later
                    DiaLogger.addLogToBuffers(toLog, level);
                }

                try {
                    String levelLit = DiaLogger.literals.get(level);
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DiaLogger.log, true)));

                    if (levelLit == null) {
                        out.println("[" + DiaLogger.getTime() + "][" + DiaLogger.literals.get(DiaLoggerLevel.ERROR) + "] Trying to log with unknown level");
                    }
                    out.println(toLog);
                    out.close();

                } catch (IOException e) {
                    System.err.println("FAILED WHILE TRYING TO LOG TO: '" + DiaLogger.log.getAbsolutePath() + "'");
                }
            }
        } else {
            assert false;
        }
    }

    /**
     * Adds a log entry to the no-observer buffers.
     * @param text Message of the log
     * @param level Level of the log
     */
    private static void addLogToBuffers(String text, DiaLoggerLevel level) {
        DiaLogger.entryBuffer.add(text);
        DiaLogger.levelBuffer.add(level);
    }

    /**
     * Dumps all the entries stored on the buffers to the logs observers.
     */
    private static void dumpLogBuffers() {
        if (DiaLogger.entryBuffer.size() == DiaLogger.levelBuffer.size()) {
            for (DiaLoggerObserver observer : DiaLogger.observers) {
                for (int i = 0; i < DiaLogger.entryBuffer.size(); i++) {
                    observer.newEntry(DiaLogger.entryBuffer.get(i), DiaLogger.levelBuffer.get(i));
                }
            }
        } else {
            log("Unable to dump log buffers into observers, entry and level buffers are of different sizes: " + DiaLogger.entryBuffer.size() + " / " + DiaLogger.levelBuffer.size(), DiaLoggerLevel.ERROR);
        }
    }

    @Override
    public void run() {
        DiaLogger.log("Running logger thread: " + diaLogger.getState());
    }
}
