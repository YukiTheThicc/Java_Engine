package diamondEngine.diaUtils;

public interface DiaLoggerObserver {

    void newEntry(String message, DiaLoggerLevel level);
}
