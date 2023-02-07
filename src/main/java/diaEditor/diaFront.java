package diaEditor;

import diamondEngine.DiaConsole;

public class diaFront {

    public static void main(String[] args) {
        DiaConsole.printFrontEndStartUp();
        FrontEnd frontEnd = FrontEnd.get();
        frontEnd.start();
    }
}