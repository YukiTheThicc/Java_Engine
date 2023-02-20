package diamondEngine.diaUtils;

public class DiaConsole {

    public static void init() {
        DiaConsole.print("Inicializando consola");
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public static void printFrontEndStartUp() {
        DiaConsole.print(" _________________________________________________________________________________ ");
        DiaConsole.print("|                            INITIALIZING FRONT END                               |");
        DiaConsole.print("|_________________________________________________________________________________|");
    }

    public static void log(String message) {
        DiaConsole.log(message, "global");
    }

    public static void log(String message, String log) {
        DiaConsole.print("[" + log.toUpperCase() + "][" + DiaUtils.get().getTime() + "] -> " + message);
    }
}
