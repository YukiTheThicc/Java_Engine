package diamondEngine.diaUtils.diaLogger;

public interface DiaLoggerObserver {

    void newEntry(String message, DiaLoggerLevel level);
}
