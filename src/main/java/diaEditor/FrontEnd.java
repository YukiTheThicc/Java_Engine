package diaEditor;

import diamondEngine.diaUtils.DiaUtils;

public class FrontEnd {

    /**
     * Class that manages and contains all FrontEnd/Editor functionalities and resources
     */

    // ATTRIBUTES
    private static FrontEnd frontEnd = null;
    private ContainerEditor container;
    private DiaFrontSettings settings;

    // CONSTRUCTORS
    private FrontEnd () {
        this.container = new ContainerEditor();
        this.settings = new DiaFrontSettings();
    }

    // GETTERS & SETTERS
    public DiaFrontSettings getSettings() {
        return settings;
    }

    public void setSettings(DiaFrontSettings settings) {
        this.settings = settings;
    }

    // METHODS
    public static FrontEnd get() {
        if (FrontEnd.frontEnd == null) {
            FrontEnd.frontEnd = new FrontEnd();
        }
        return frontEnd;
    }

    public void start() {
        // Initialize general front end an Engine
        DiaUtils.get().init();
        this.settings.init();
        if (this.container != null) {
            this.container.init();
            this.container.run();
        }
    }
}