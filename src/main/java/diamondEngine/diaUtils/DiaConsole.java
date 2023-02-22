package diamondEngine.diaUtils;

public class DiaConsole {

    public static void init() {
        DiaConsole.print("Initializing console...");
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public static void log(String message) {
        DiaConsole.log(message, "debug");
    }

    public static void log(String message, String log) {
        DiaConsole.print("[" + log.toUpperCase() + "][" + DiaUtils.get().getTime() + "] -> " + message);
    }
}
