package sapphire;

import diamondEngine.diaUtils.DiaUtils;

import java.io.File;
import java.util.ArrayList;

public class Sapphire {

    /**
     * Class that manages and contains all FrontEnd/Editor functionalities and resources
     */

    // ATTRIBUTES
    private static Sapphire sapphire = null;
    private ContainerEditor container;
    private SapphireSettings settings;
    private ArrayList<File> openedFiles;

    // CONSTRUCTORS
    private Sapphire() {
        this.container = new ContainerEditor();
        this.settings = new SapphireSettings();
    }

    // GETTERS & SETTERS
    public SapphireSettings getSettings() {
        return settings;
    }

    public ArrayList<File> getOpenedFiles() {
        return openedFiles;
    }

    // METHODS
    public static Sapphire get() {
        if (Sapphire.sapphire == null) {
            Sapphire.sapphire = new Sapphire();
        }
        return sapphire;
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