package sapphire;

import diamondEngine.diaUtils.DiaConsole;

public class SapphireMain {

    public static void main(String[] args) {
        DiaConsole.printFrontEndStartUp();
        Sapphire sapphire = Sapphire.get();
        sapphire.start();
    }
}