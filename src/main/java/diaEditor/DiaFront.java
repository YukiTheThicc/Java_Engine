package diaEditor;

import diamondEngine.DiaConsole;

public class DiaFront {

    public static void main(String[] args) {
        DiaConsole.printFrontEndStartUp();
        FrontEnd frontEnd = FrontEnd.get();
        frontEnd.start();
    }
}