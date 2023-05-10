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
    private File log;
    private SimpleDateFormat sdf;
    private HashMap<DiaLoggerLevel, String> literals;
    private ArrayList<DiaLoggerObserver> observers;
    private ArrayList<String> earlyEntryBuffer;
    private ArrayList<DiaLoggerLevel> earlyLevelBuffer;
    private ArrayList<String> entryBuffer;
    private ArrayList<DiaLoggerLevel> levelBuffer;
    private DiaLoggerLevel currentLevel;
    private boolean dirty;
    private boolean isRunning;

    // CONSTRUCTORS
    private DiaLogger() {
        this.setName("DiaLogger");
    }

    // GETTERS & SETTERS
    public static DiaLoggerLevel getCurrentLevel() {
        return DiaLogger.diaLogger.currentLevel;
    }

    public static void changeLevel(DiaLoggerLevel currentLevel) {
        DiaLogger.diaLogger.currentLevel = currentLevel;
    }

    public static File getLog() {
        return DiaLogger.diaLogger.log;
    }

    public static void changeLog(String log) {
        try {
            String dir = log.substring(0, log.lastIndexOf("/") + 1);
            if (log.split("/").length > 1) {
                Files.createDirectories(Paths.get(dir));
            }
            DiaLogger.diaLogger.log = new File(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addObserver(DiaLoggerObserver observer) {
        DiaLogger.diaLogger.observers.add(observer);
        // If no observer was added previously the buffers are dumped to the observers
        if (DiaLogger.diaLogger.observers.size() == 1) {
            diaLogger.dumpLogBuffers();
        }
    }

    // METHODS
    public static void init() {
        if (diaLogger == null) {
            diaLogger = new DiaLogger();
            diaLogger.log = new File("log.txt");
            diaLogger.literals = new HashMap<>();
            diaLogger.sdf = new SimpleDateFormat("hh:mm:ss.SSS");
            diaLogger.observers = new ArrayList<>();
            diaLogger.entryBuffer = new ArrayList<>();
            diaLogger.levelBuffer = new ArrayList<>();
            diaLogger.earlyEntryBuffer = new ArrayList<>();
            diaLogger.earlyLevelBuffer = new ArrayList<>();
            diaLogger.currentLevel = DiaLoggerLevel.DEBUG;
            diaLogger.dirty = true; // Es otaku y no se lava nunca, lo inicializamos siempre en manchao sisi

            // Clear log file for new session
            // REVISE: Maybe better to keep ALL log entries for other sessions or creating log file depending on the day
            try {
                PrintWriter writer = new PrintWriter(DiaLogger.diaLogger.log);
                writer.print("");
                writer.close();
            } catch (IOException e) {
                System.err.println("FAILED WHILE CLEARING LOG: '" + DiaLogger.diaLogger.log.getAbsolutePath() + "'");
            }

            DiaLogger.diaLogger.literals.put(DiaLoggerLevel.INFO, "INFO");
            DiaLogger.diaLogger.literals.put(DiaLoggerLevel.DEBUG, "DEBUG");
            DiaLogger.diaLogger.literals.put(DiaLoggerLevel.WARN, "WARNING");
            DiaLogger.diaLogger.literals.put(DiaLoggerLevel.ERROR, "ERROR");
            DiaLogger.diaLogger.literals.put(DiaLoggerLevel.CRITICAL, "CRITICAL");
            DiaLogger.diaLogger.literals.put(DiaLoggerLevel.SAPP_DEBUG, "SAPP_DEBUG");

            diaLogger.start();
            diaLogger.isRunning = true;
        }
    }

    private static String getTime() {
        return DiaLogger.diaLogger.sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * Logs a message with DEBUG level of severity.
     *
     * @param message Message to log.
     */
    public static void log(Class<?> callerClass, String message) {
        DiaLogger.log(callerClass, message, DiaLoggerLevel.DEBUG);
    }

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
    public static void log(Class<?> callerClass, String message, DiaLoggerLevel level) {
        diaLogger.entryBuffer.add("[" + DiaLogger.getTime() + "][" + level + "][" + callerClass.getSimpleName() + "] " + message);
        diaLogger.levelBuffer.add(level);
        diaLogger.dirty = true;
    }

    public static void log(String message, DiaLoggerLevel level) {
        diaLogger.entryBuffer.add("[" + DiaLogger.getTime() + "][" + level + "] " + message);
        diaLogger.levelBuffer.add(level);
        diaLogger.dirty = true;
    }

    /**
     * Adds a log entry to the no-observer buffers.
     *
     * @param text  Message of the log
     * @param level Level of the log
     */
    private void addLogToBuffers(String text, DiaLoggerLevel level) {
        diaLogger.earlyEntryBuffer.add(text);
        diaLogger.earlyLevelBuffer.add(level);
    }

    /**
     * Dumps all the entries stored on the buffers to the logs observers.
     */
    private void dumpLogBuffers() {
        if (diaLogger.earlyEntryBuffer.size() == diaLogger.earlyLevelBuffer.size()) {
            for (DiaLoggerObserver observer : diaLogger.observers) {
                for (int i = 0; i < diaLogger.earlyEntryBuffer.size(); i++) {
                    observer.newEntry(diaLogger.earlyEntryBuffer.get(i), diaLogger.earlyLevelBuffer.get(i));
                }
            }
        } else {
            log(DiaLogger.class, "Unable to dump log buffers into observers, entry and level buffers are of different sizes: " + diaLogger.entryBuffer.size() + " / " + diaLogger.levelBuffer.size(), DiaLoggerLevel.ERROR);
        }
    }

    private void logEntry(String message, DiaLoggerLevel level) {
        if (DiaLogger.diaLogger.log != null) {
            if (DiaLogger.diaLogger.currentLevel.ordinal() >= level.ordinal()) {

                // Notify observers of log entry
                if (diaLogger.observers != null && !diaLogger.observers.isEmpty()) {
                    for (DiaLoggerObserver observer : DiaLogger.diaLogger.observers) {
                        observer.newEntry(message, level);
                    }
                } else {
                    // If there are no observers the log entries are stored on buffers to be dumped later
                    DiaLogger.diaLogger.addLogToBuffers(message, level);
                }

                try {
                    String levelLit = DiaLogger.diaLogger.literals.get(level);
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DiaLogger.diaLogger.log, true)));

                    if (levelLit == null) {
                        out.println("[" + DiaLogger.getTime() + "][" + DiaLogger.diaLogger.literals.get(DiaLoggerLevel.ERROR) + "] Trying to log with unknown level");
                    }
                    out.println(message);
                    out.close();

                } catch (IOException e) {
                    System.err.println("FAILED WHILE TRYING TO LOG TO: '" + DiaLogger.diaLogger.log.getAbsolutePath() + "'");
                }
            }
        } else {
            assert false;
        }
    }

    public static void close() {
        DiaLogger.diaLogger.isRunning = false;
        DiaLogger.diaLogger.interrupt();
        try {
            DiaLogger.diaLogger.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            DiaLogger.log(DiaLogger.class, "Running logger thread...");
            while (isRunning) {
                if (dirty) {
                    if (entryBuffer.size() == levelBuffer.size()) {
                        for (int i = 0; i < entryBuffer.size(); i++) {
                            logEntry(entryBuffer.get(i), levelBuffer.get(i));
                        }
                    } else {
                        assert false;
                    }
                    entryBuffer.clear();
                    levelBuffer.clear();
                    dirty = false;
                } else {
                    sleep(250);
                }
            }
        } catch (InterruptedException e) {
            assert false;
        }
    }
}
