package diaEditor;

import diamondEngine.diaUtils.DiaConsole;

public class DiaFront {

    public static void main(String[] args) {
        DiaConsole.printFrontEndStartUp();
        FrontEnd frontEnd = FrontEnd.get();
        frontEnd.start();
    }
}